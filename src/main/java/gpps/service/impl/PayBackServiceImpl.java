package gpps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gpps.dao.ICashStreamDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.ProductSeries;
import gpps.service.IPayBackService;
import gpps.service.PayBackDetail;
import gpps.service.exception.UnSupportDelayException;
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
	public PayBack applyToDelay(Integer payBackId, long delayTo)
			throws UnSupportDelayException {
		PayBack payBack=find(payBackId);
		if(payBack.getType()!=PayBack.TYPE_LASTPAY)
			throw new UnSupportDelayException("只有最后一次还款允许延期");
		Product product=productDao.find(payBack.getProductId());
		ProductSeries productSeries=productSeriesDao.find(product.getProductseriesId());
		if(productSeries.getType()!=ProductSeries.TYPE_FINISHPAYINTERESTANDCAPITAL)
			throw new UnSupportDelayException("当前产品不支持延期");
		Calendar delayCalendar=Calendar.getInstance();
		delayCalendar.setTimeInMillis(delayTo);
		delayCalendar.set(delayCalendar.get(Calendar.YEAR), delayCalendar.get(Calendar.MONTH), delayCalendar.get(Calendar.DATE), 0, 0, 0);
		if(delayCalendar.getTimeInMillis()<=payBack.getDeadline())
			throw new UnSupportDelayException("延期时间非法");
		PayBack lastestPayBack=payBackDao.findLastest(payBack.getProductId());
		if(lastestPayBack.getState()!=PayBack.STATE_FINISHREPAY)
			throw new UnSupportDelayException("请先完成之前的还款");
		
		return null;
	}
}
