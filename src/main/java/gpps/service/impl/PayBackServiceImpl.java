package gpps.service.impl;

import gpps.dao.ICashStreamDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.model.GovermentOrder;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.ProductSeries;
import gpps.service.IGovermentOrderService;
import gpps.service.IPayBackService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.UnSupportRepayInAdvanceException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class PayBackServiceImpl implements IPayBackService {
	@Autowired
	IPayBackDao payBackDao;
	@Autowired
	ICashStreamDao cashStreamDao;
	@Autowired
	IProductDao productDao;
	@Autowired
	IProductSeriesDao productSeriesDao;
	@Autowired
	IGovermentOrderService orderSerivce;
	@Override
	public void create(PayBack payback) {
		payBackDao.create(payback);
	}

	@Override
	public List<PayBack> findAll(Integer productId) {
		Product product=productDao.find(productId);
		List<PayBack> payBacks=payBackDao.findAllByProduct(productId);
		if(payBacks==null||payBacks.size()==0)
			return new ArrayList<PayBack>(0);
		for(PayBack payBack:payBacks)
		{
			if(product.getState()==Product.STATE_FINANCING||product.getState()==Product.STATE_QUITFINANCING)
			{
				payBack.setChiefAmount(payBack.getChiefAmount().multiply(product.getExpectAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
				payBack.setInterest(payBack.getInterest().multiply(product.getExpectAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
			}else
			{
				payBack.setChiefAmount(payBack.getChiefAmount().multiply(product.getRealAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
				payBack.setInterest(payBack.getInterest().multiply(product.getRealAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
			}
		}
		return payBacks;
	}

	@Override
	public void changeState(Integer paybackId, int state) {
		payBackDao.changeState(paybackId, state,System.currentTimeMillis());
	}

	@Override
	public PayBack find(Integer id) {
		PayBack payBack=payBackDao.find(id);
		Product product=productDao.find(payBack.getProductId());
		if(product.getState()==Product.STATE_FINANCING||product.getState()==Product.STATE_QUITFINANCING)
		{
			payBack.setChiefAmount(payBack.getChiefAmount().multiply(product.getExpectAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
			payBack.setInterest(payBack.getInterest().multiply(product.getExpectAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
		}else
		{
			payBack.setChiefAmount(payBack.getChiefAmount().multiply(product.getRealAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
			payBack.setInterest(payBack.getInterest().multiply(product.getRealAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
		}
		return payBack;
	}

	@Override
	@Transactional
	public void applyRepayInAdvance(Integer payBackId)
			throws UnSupportRepayInAdvanceException {
		PayBack payBack=find(payBackId);
		if(payBack.getState()!=PayBack.STATE_WAITFORREPAY)
			return;
		if(payBack.getType()!=PayBack.TYPE_LASTPAY)
			throw new UnSupportRepayInAdvanceException("只有最后一次还款允许提前");
		Product product=productDao.find(payBack.getProductId());
		ProductSeries productSeries=productSeriesDao.find(product.getProductseriesId());
		if(productSeries.getType()==ProductSeries.TYPE_AVERAGECAPITALPLUSINTEREST)
			throw new UnSupportRepayInAdvanceException("当前产品不支持提前还贷");
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
		if(cal.getTimeInMillis()>payBack.getDeadline())
			throw new UnSupportRepayInAdvanceException("时间非法");
		GovermentOrder order=orderSerivce.findGovermentOrderByProduct(payBack.getProductId());
		List<Product> products=order.getProducts();
		//验证先还完稳健型或平衡型
		for(Product pro:products)
		{
			if(pro.getId()==(int)(product.getId()))
					continue;
			ProductSeries proSeries=productSeriesDao.find(pro.getProductseriesId());
			if(proSeries.getType()==ProductSeries.TYPE_AVERAGECAPITALPLUSINTEREST&&(pro.getState()==Product.STATE_REPAYING||pro.getState()==Product.STATE_POSTPONE))
				throw new UnSupportRepayInAdvanceException("必须先还完稳健型产品，才允许该产品提前还贷");
			else if(proSeries.getType()==ProductSeries.TYPE_FIRSTINTERESTENDCAPITAL&&(pro.getState()==Product.STATE_REPAYING||pro.getState()==Product.STATE_POSTPONE))
				throw new UnSupportRepayInAdvanceException("必须先还完平衡型产品，才允许该产品提前还贷");
		}
		List<PayBack> payBacks=findAll(product.getId());
		for(PayBack pb:payBacks)
		{
			if(pb.getId()==(int)(payBack.getId()))
				continue;
			if(pb.getState()==PayBack.STATE_FINISHREPAY||pb.getState()==PayBack.STATE_REPAYING)
				continue;
			if(pb.getState()==PayBack.STATE_DELAY||pb.getDeadline()<=cal.getTimeInMillis())
				throw new UnSupportRepayInAdvanceException("请先将前面的还款还清才允许提前还贷");
		}
		long lastRepaytime=0;
		for(PayBack pb:payBacks)
		{
			if(pb.getId()==(int)(payBack.getId()))
				continue;
			if(pb.getState()==PayBack.STATE_FINISHREPAY||pb.getState()==PayBack.STATE_REPAYING)
			{
				if(pb.getDeadline()>lastRepaytime)
					lastRepaytime=pb.getDeadline();
				continue;
			}
			changeState(pb.getId(), PayBack.STATE_INVALID);
		}
		//重新计算最后还款
		Calendar lastCal=Calendar.getInstance();
		lastCal.setTimeInMillis(lastRepaytime);
		int days=cal.get(Calendar.DAY_OF_YEAR)-lastCal.get(Calendar.DAY_OF_YEAR);
		payBack.setInterest(PayBack.BASELINE.multiply(product.getRate()).multiply(new BigDecimal(days)).divide(new BigDecimal(365),2,BigDecimal.ROUND_UP));
		payBack.setDeadline(cal.getTimeInMillis());
		payBackDao.update(payBack);
	}

	@Override
	@Transactional
	public void delay(Integer payBackId) {
		PayBack payBack=payBackDao.find(payBackId);
		changeState(payBackId, PayBack.STATE_DELAY);
		try {
			productDao.changeState(payBack.getProductId(), Product.STATE_POSTPONE);
		} catch (IllegalConvertException e) {
			e.printStackTrace();
		}
		
	}
}
