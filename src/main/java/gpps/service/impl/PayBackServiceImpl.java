package gpps.service.impl;

import gpps.constant.Pagination;
import gpps.dao.ICashStreamDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.dao.IStateLogDao;
import gpps.model.GovermentOrder;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.ProductSeries;
import gpps.model.StateLog;
import gpps.service.IGovermentOrderService;
import gpps.service.IPayBackService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.UnSupportRepayInAdvanceException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
	@Autowired
	IStateLogDao stateLogDao;
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
		PayBack payBack=payBackDao.find(paybackId);
		payBackDao.changeState(paybackId, state,System.currentTimeMillis());
		StateLog stateLog=new StateLog();
		stateLog.setSource(payBack.getState());
		stateLog.setTarget(state);
		stateLog.setType(StateLog.TYPE_PAYBACK);
		stateLog.setRefid(paybackId);
		stateLogDao.create(stateLog);
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
		long lastRepaytime=order.getIncomeStarttime();
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
		int days=getDays(lastCal, cal);
		if(days<0)
			days=0;
		payBack.setInterest(PayBack.BASELINE.multiply(product.getRate()).multiply(new BigDecimal(days)).divide(new BigDecimal(365),2,BigDecimal.ROUND_UP));
		payBack.setDeadline(cal.getTimeInMillis());
		payBack.setChiefAmount(PayBack.BASELINE);
		payBackDao.update(payBack);
	}

	@Override
	@Transactional
	public void delay(Integer payBackId) {
		PayBack payBack=payBackDao.find(payBackId);
		changeState(payBackId, PayBack.STATE_DELAY);
		try {
			productDao.changeState(payBack.getProductId(), Product.STATE_POSTPONE,System.currentTimeMillis());
		} catch (IllegalConvertException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<PayBack> generatePayBacks(int amount, double rate,
			int payBackModel, long from, long to) {
		List<PayBack> payBacks=new ArrayList<PayBack>();
		PayBack payBack=null;
		Calendar starttime=Calendar.getInstance();
		starttime.setTimeInMillis(from);
		starttime.set(starttime.get(Calendar.YEAR), starttime.get(Calendar.MONTH), starttime.get(Calendar.DATE), 0, 0, 0);
		Calendar endtime=Calendar.getInstance();
		endtime.setTimeInMillis(to);
		endtime.set(endtime.get(Calendar.YEAR), endtime.get(Calendar.MONTH), endtime.get(Calendar.DATE), 0, 0, 0);
		
		int monthNum=(endtime.get(Calendar.YEAR)-starttime.get(Calendar.YEAR))*12+(endtime.get(Calendar.MONTH)-starttime.get(Calendar.MONTH));
		if(endtime.get(Calendar.DAY_OF_MONTH)>starttime.get(Calendar.DAY_OF_MONTH))
			monthNum++;
		if(payBackModel==ProductSeries.TYPE_AVERAGECAPITALPLUSINTEREST)//等额本息
		{
			BigDecimal mRate=new BigDecimal(rate).divide(new BigDecimal(12),10,BigDecimal.ROUND_HALF_EVEN);//月利息
			//每月还款额
			BigDecimal amountEachMonth=new BigDecimal(amount).multiply(mRate).multiply(mRate.add(new BigDecimal(1)).pow(monthNum)).divide(mRate.add(new BigDecimal(1)).pow(monthNum).subtract(new BigDecimal(1)),2,BigDecimal.ROUND_HALF_EVEN);//每月归还额
			BigDecimal repayedChiefAmount=BigDecimal.ZERO;
			for(int i=0;i<monthNum;i++)
			{
				Calendar paybackCal;
				if(i+1==monthNum)
					paybackCal=endtime;
				else
				{
					paybackCal=(Calendar)(starttime.clone());
					paybackCal.add(Calendar.MONTH, i+1);
				}
				//计算第n月利息，  第n月还款利息＝（a×i－b）×（1＋i）^（n－1）＋b
				//贷款额为a，月利率为i，还款月数为n，每月还款额为b
				BigDecimal interest=new BigDecimal(amount).multiply(mRate).subtract(amountEachMonth).multiply(mRate.add(new BigDecimal(1)).pow(i)).add(amountEachMonth).setScale(2, BigDecimal.ROUND_HALF_EVEN);
				payBack=new PayBack();
				payBack.setInterest(interest);
				payBack.setState(PayBack.STATE_WAITFORREPAY);
				if(i+1==monthNum)
				{
					payBack.setType(PayBack.TYPE_LASTPAY);
					payBack.setChiefAmount(new BigDecimal(amount).subtract(repayedChiefAmount));
				}
				else
				{
					payBack.setType(PayBack.TYPE_INTERESTANDCHIEF);
					payBack.setChiefAmount(amountEachMonth.subtract(interest));
					repayedChiefAmount=repayedChiefAmount.add(payBack.getChiefAmount());
				}
				paybackCal.add(Calendar.DAY_OF_YEAR, 1);
				payBack.setDeadline(paybackCal.getTimeInMillis());
				payBacks.add(payBack);
			}
		}else if(payBackModel==ProductSeries.TYPE_FINISHPAYINTERESTANDCAPITAL||payBackModel==ProductSeries.TYPE_FIRSTINTERESTENDCAPITAL)
		{
			for(int i=0;i<monthNum;i++)
			{
				Calendar currentMonthStart=(Calendar)(starttime.clone());
				currentMonthStart.add(Calendar.MONTH, i);
				Calendar currentMonthEnd=null;
				if(i+1==monthNum)
					currentMonthEnd=(Calendar)(endtime.clone());
				else
				{
					currentMonthEnd=(Calendar)(starttime.clone());
					currentMonthEnd.add(Calendar.MONTH, i+1);
				}
				int days=getDays(currentMonthStart, currentMonthEnd);
				payBack=new PayBack();
				payBack.setInterest(new BigDecimal(amount).multiply(new BigDecimal(rate)).multiply(new BigDecimal(days)).divide(new BigDecimal(365),2,BigDecimal.ROUND_UP));
				payBack.setState(PayBack.STATE_WAITFORREPAY);
				if(i+1==monthNum)
				{
					payBack.setChiefAmount(new BigDecimal(amount));
					payBack.setType(PayBack.TYPE_LASTPAY);
				}
				else
					payBack.setType(PayBack.TYPE_INTERESTANDCHIEF);
				currentMonthEnd.add(Calendar.DAY_OF_YEAR, 1);
				payBack.setDeadline(currentMonthEnd.getTimeInMillis());
				payBacks.add(payBack);
			}
		}
		return payBacks;
	}

	private int getDays(Calendar starttime,Calendar endtime)
	{
		if(starttime.get(Calendar.YEAR)==endtime.get(Calendar.YEAR))
			return endtime.get(Calendar.DAY_OF_YEAR)-starttime.get(Calendar.DAY_OF_YEAR);
		else {
			return starttime.getActualMaximum(Calendar.DAY_OF_YEAR)-starttime.get(Calendar.DAY_OF_YEAR)+endtime.get(Calendar.DAY_OF_YEAR);
		}
	}

	@Override
	public List<PayBack> generatePayBacks(Integer productId, int amount) {
		Product product=productDao.find(productId);
		List<PayBack> payBacks=payBackDao.findAllByProduct(productId);
		if(payBacks==null||payBacks.size()==0)
			return new ArrayList<PayBack>(0);
		for(PayBack payBack:payBacks)
		{
			payBack.setChiefAmount(payBack.getChiefAmount().multiply(new BigDecimal(amount)).divide(PayBack.BASELINE,2,BigDecimal.ROUND_DOWN));
			payBack.setInterest(payBack.getInterest().multiply(new BigDecimal(amount)).divide(PayBack.BASELINE,2,BigDecimal.ROUND_DOWN));
		}
		return payBacks;
	}

	@Override
	public Map<String, Object> findBorrowerPayBacks(int state, long starttime,
			long endtime,int offset,int recnum) {
		List<Integer> states = null;
		if(state!=-1)
		{
			states=new ArrayList<Integer>();
			states.add(state);
		}
		if(state==PayBack.STATE_FINISHREPAY)
			states.add(PayBack.STATE_REPAYING);
		int count=payBackDao.countByState(states, starttime, endtime);
		if(count==0)
			return Pagination.buildResult(null, count, offset, recnum);
		List<PayBack> payBacks=payBackDao.findByState(states, starttime, endtime, offset, recnum);
		for(PayBack payBack:payBacks)
		{
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
		}
		return Pagination.buildResult(payBacks, count, offset, recnum);
	}
}
