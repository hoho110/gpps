package gpps.service.impl;

import gpps.constant.Pagination;
import gpps.dao.IBorrowerDao;
import gpps.dao.ICashStreamDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.ILenderDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.dao.IStateLogDao;
import gpps.dao.ISubmitDao;
import gpps.model.Borrower;
import gpps.model.BorrowerAccount;
import gpps.model.CashStream;
import gpps.model.GovermentOrder;
import gpps.model.Lender;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.ProductSeries;
import gpps.model.StateLog;
import gpps.model.Submit;
import gpps.model.Task;
import gpps.service.CashStreamSum;
import gpps.service.IAccountService;
import gpps.service.IBorrowerService;
import gpps.service.IGovermentOrderService;
import gpps.service.IPayBackService;
import gpps.service.IProductService;
import gpps.service.ITaskService;
import gpps.service.exception.CheckException;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.IllegalOperationException;
import gpps.service.exception.InsufficientBalanceException;
import gpps.service.exception.UnSupportRepayInAdvanceException;
import gpps.service.thirdpay.Transfer.LoanJson;
import gpps.tools.StringUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
	@Autowired
	IBorrowerService borrowerService;
	@Autowired
	IGovermentOrderDao govermentOrderDao;
	@Autowired
	IProductService productService;
	@Autowired
	IAccountService accountService;
	@Autowired
	ITaskService taskService;
	@Autowired
	IGovermentOrderService orderService;
	@Autowired
	IBorrowerDao borrowerDao;
	@Autowired
	ISubmitDao submitDao;
	@Autowired
	ILenderDao lenderDao;
	Logger log=Logger.getLogger(PayBackServiceImpl.class);
	@Override
	public void create(PayBack payback) {
		payBackDao.create(payback);
		StateLog stateLog=new StateLog();
		stateLog.setCreatetime(System.currentTimeMillis());
		stateLog.setRefid(payback.getId());
		stateLog.setTarget(payback.getState());
		stateLog.setType(stateLog.TYPE_PAYBACK);
		stateLogDao.create(stateLog);
	}

	@Override
	public List<PayBack> findAll(Integer productId) {
		Product product=productDao.find(productId);
		List<PayBack> payBacks=payBackDao.findAllByProduct(productId);
		if(payBacks==null||payBacks.size()==0)
			return new ArrayList<PayBack>(0);
		for(PayBack payBack:payBacks)
		{
			if(product.getState()==Product.STATE_UNPUBLISH||product.getState()==Product.STATE_FINANCING||product.getState()==Product.STATE_QUITFINANCING)
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
//		payBack.setRealtime(cal.getTimeInMillis());
		payBack.setDeadline(cal.getTimeInMillis());
		payBack.setChiefAmount(PayBack.BASELINE);
		payBackDao.update(payBack);
	}

	@Override
	@Transactional
	public void delay(Integer payBackId) {
		PayBack payBack=payBackDao.find(payBackId);
		changeState(payBackId, PayBack.STATE_DELAY);
		productDao.changeState(payBack.getProductId(), Product.STATE_POSTPONE,System.currentTimeMillis());
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
		if(starttime.get(Calendar.YEAR)>endtime.get(Calendar.YEAR))
			return 0;
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
		Borrower borrower=borrowerService.getCurrentUser();
		int count=payBackDao.countByBorrowerAndState(borrower.getAccountId(),states, starttime, endtime);
		if(count==0)
			return Pagination.buildResult(null, count, offset, recnum);
		List<PayBack> payBacks=payBackDao.findByBorrowerAndState(borrower.getAccountId(),states, starttime, endtime, offset, recnum);
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
			payBack.setProduct(product);
			product.setGovermentOrder(govermentOrderDao.find(product.getGovermentorderId()));
			product.setProductSeries(productSeriesDao.find(product.getProductseriesId()));
		}
		return Pagination.buildResult(payBacks, count, offset, recnum);
	}

	@Override
	public List<PayBack> findBorrowerCanBeRepayedPayBacks() {
		Borrower borrower=borrowerService.getCurrentUser();
		List<PayBack> payBacks=payBackDao.findBorrowerWaitForRepayed(borrower.getAccountId());
		if(payBacks==null||payBacks.size()==0)
			return new ArrayList<PayBack>(0);
		List<PayBack> canBeRepayedPayBacks=new ArrayList<PayBack>();
		for(PayBack payBack:payBacks)
		{
			if(canRepay(payBack.getId()))
			{
				Product product=productDao.find(payBack.getProductId());
				payBack.setProduct(product);
				product.setGovermentOrder(govermentOrderDao.find(product.getGovermentorderId()));
				product.setProductSeries(productSeriesDao.find(product.getProductseriesId()));
				changeBorrowerPayBackLimit(payBack,product);
				canBeRepayedPayBacks.add(payBack);
			}
		}
		return canBeRepayedPayBacks;
	}
	@Override
	public List<PayBack> findBorrowerCanBeRepayedInAdvancePayBacks() {
		Borrower borrower=borrowerService.getCurrentUser();
		List<PayBack> payBacks=payBackDao.findBorrowerWaitForRepayed(borrower.getAccountId());
		if(payBacks==null||payBacks.size()==0)
			return new ArrayList<PayBack>(0);
		List<PayBack> canBeRepayedInAdvancePayBacks=new ArrayList<PayBack>();
		for(PayBack payBack:payBacks)
		{
			if(canRepayInAdvance(payBack.getId()))
			{
				Product product=productDao.find(payBack.getProductId());
				payBack.setProduct(product);
				product.setGovermentOrder(govermentOrderDao.find(product.getGovermentorderId()));
				product.setProductSeries(productSeriesDao.find(product.getProductseriesId()));
				changeBorrowerPayBackLimit(payBack,product);
				canBeRepayedInAdvancePayBacks.add(payBack);
			}
		}
		return canBeRepayedInAdvancePayBacks;
	}
	@Override
	public boolean canRepay(Integer payBackId) {
		PayBack payBack=find(payBackId);
		if(payBack.getState()!=PayBack.STATE_WAITFORREPAY)
			return false;
		//本产品是否为最后一次还款
		List<PayBack> payBacks=findAll(payBack.getProductId());
		for(PayBack pb:payBacks)
		{
			if(pb.getId()==(int)(payBack.getId()))
				continue;
			if(pb.getState()==PayBack.STATE_FINISHREPAY||pb.getState()==PayBack.STATE_REPAYING)
				continue;
			if(pb.getState()==PayBack.STATE_DELAY||pb.getDeadline()<payBack.getDeadline())
				return false;
		}
		Product product=productDao.find(payBack.getProductId());
		ProductSeries productSeries=productSeriesDao.find(product.getProductseriesId());
		if(productSeries.getType()==ProductSeries.TYPE_AVERAGECAPITALPLUSINTEREST)
			return true;
		List<Product> products=productDao.findByGovermentOrder(product.getGovermentorderId());
		for(Product pro:products)
		{
			if((int)(pro.getId())==(int)(product.getId()))
				continue;
			ProductSeries proSeries=productSeriesDao.find(pro.getProductseriesId());
			if(proSeries.getType()>=productSeries.getType())
				continue;
			payBacks=findAll(pro.getId());
			for(PayBack pb:payBacks)
			{
				if(pb.getState()==PayBack.STATE_FINISHREPAY||pb.getState()==PayBack.STATE_REPAYING)
					continue;
				if(pb.getState()==PayBack.STATE_DELAY||pb.getDeadline()<=payBack.getDeadline())
					return false;
			}
		}
		return true;
	}

	@Override
	public boolean canRepayInAdvance(Integer payBackId) {
		PayBack payBack=find(payBackId);
		if(payBack.getState()!=PayBack.STATE_WAITFORREPAY)
			return false;
		if(payBack.getType()!=PayBack.TYPE_LASTPAY)
			return false;
		if(payBack.getDeadline()<=System.currentTimeMillis())
			return false;
		Product product=productDao.find(payBack.getProductId());
		ProductSeries productSeries=productSeriesDao.find(product.getProductseriesId());
		if(productSeries.getType()==ProductSeries.TYPE_AVERAGECAPITALPLUSINTEREST)
			return false;
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
		if(cal.getTimeInMillis()>payBack.getDeadline())
			return false;
		GovermentOrder order=orderSerivce.findGovermentOrderByProduct(payBack.getProductId());
		List<Product> products=order.getProducts();
		//验证先还完稳健型或平衡型
		for(Product pro:products)
		{
			if(pro.getId()==(int)(product.getId()))
					continue;
			ProductSeries proSeries=productSeriesDao.find(pro.getProductseriesId());
			if(proSeries.getType()==ProductSeries.TYPE_AVERAGECAPITALPLUSINTEREST&&(pro.getState()==Product.STATE_REPAYING||pro.getState()==Product.STATE_POSTPONE))
				return false;
			else if(proSeries.getType()==ProductSeries.TYPE_FIRSTINTERESTENDCAPITAL&&(pro.getState()==Product.STATE_REPAYING||pro.getState()==Product.STATE_POSTPONE))
				return false;
		}
		List<PayBack> payBacks=findAll(product.getId());
		for(PayBack pb:payBacks)
		{
			if(pb.getId()==(int)(payBack.getId()))
				continue;
			if(pb.getState()==PayBack.STATE_FINISHREPAY||pb.getState()==PayBack.STATE_REPAYING)
				continue;
			if(pb.getState()==PayBack.STATE_DELAY||pb.getDeadline()<=cal.getTimeInMillis())
				return false;
		}
		return true;
	}

	@Override
	public List<PayBack> findBorrowerWaitForRepayed() {
		Borrower borrower=borrowerService.getCurrentUser();
		List<PayBack> payBacks=payBackDao.findBorrowerWaitForRepayed(borrower.getAccountId());
		if(payBacks==null||payBacks.size()==0)
			return new ArrayList<PayBack>(0);
		for(PayBack payBack:payBacks)
		{
			Product product=productDao.find(payBack.getProductId());
			payBack.setProduct(product);
			product.setGovermentOrder(govermentOrderDao.find(product.getGovermentorderId()));
			product.setProductSeries(productSeriesDao.find(product.getProductseriesId()));
			changeBorrowerPayBackLimit(payBack, product);
		}
		return payBacks;
	}

	@Override
	public void repay(Integer payBackId) throws IllegalStateException,IllegalOperationException, InsufficientBalanceException, IllegalConvertException {
		Borrower borrower=borrowerService.getCurrentUser();
		if((borrower.getAuthorizeTypeOpen()&Borrower.AUTHORIZETYPEOPEN_RECHARGE)==0)
			throw new IllegalOperationException("请先授权还款权限，然后再执行还款");
		PayBack payBack = find(payBackId);
		if(payBack.getState()!=PayBack.STATE_WAITFORREPAY)
			return;
		Product currentProduct = productService.find(payBack.getProductId());
		if (currentProduct.getState() != Product.STATE_REPAYING) 
			throw new IllegalStateException("该产品尚未进入还款阶段");
		// 验证还款顺序
		List<Product> products = productService.findByGovermentOrder(currentProduct.getGovermentorderId());
		for (Product product : products) {
			if (product.getId() == (int) (currentProduct.getId())) {
				List<PayBack> payBacks = findAll(product.getId());
				for (PayBack pb : payBacks) {
					if (pb.getState() == PayBack.STATE_FINISHREPAY || pb.getState() == PayBack.STATE_REPAYING)
						continue;
					if (pb.getDeadline() < payBack.getDeadline()) 
						throw new IllegalOperationException("请按时间顺序进行还款");
				}
				continue;
			}
			product.setProductSeries(productSeriesDao.find(product.getProductseriesId()));
			if (product.getProductSeries().getType() < currentProduct.getProductSeries().getType()) {
				List<PayBack> payBacks = findAll(product.getId());
				for (PayBack pb : payBacks) {
					if (pb.getState() == PayBack.STATE_FINISHREPAY || pb.getState() == PayBack.STATE_REPAYING)
						continue;
					if (pb.getDeadline() <= payBack.getDeadline()) 
						throw new IllegalOperationException("请先还完稳健型/平衡型产品再进行此次还款");
				}
			}
		}
		Integer cashStreamId = accountService.freezeBorrowerAccount(payBack.getBorrowerAccountId(), payBack.getChiefAmount().add(payBack.getInterest()), payBack.getId(), "冻结");
		log.debug("还款：amount=" + payBack.getChiefAmount().add(payBack.getInterest()) + ",cashStreamId=" + cashStreamId);
//		CashStream cashStream = cashStreamDao.find(cashStreamId);
		accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		changeState(payBack.getId(), PayBack.STATE_WAITFORCHECK);
	}
	private void changeBorrowerPayBackLimit(PayBack payBack,Product product)
	{
		if(product.getState()==Product.STATE_UNPUBLISH||product.getState()==Product.STATE_FINANCING||product.getState()==Product.STATE_QUITFINANCING)
		{
			payBack.setChiefAmount(payBack.getChiefAmount().multiply(product.getExpectAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
			payBack.setInterest(payBack.getInterest().multiply(product.getExpectAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
		}else
		{
			payBack.setChiefAmount(payBack.getChiefAmount().multiply(product.getRealAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
			payBack.setInterest(payBack.getInterest().multiply(product.getRealAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
		}
	}

	@Override
	@Transactional
	public void check(Integer payBackId) throws IllegalConvertException, IllegalOperationException {
		PayBack payback=find(payBackId);
		if(payback==null||payback.getState()!=PayBack.STATE_WAITFORCHECK)
			return;
		if(payback.getCheckResult()!=PayBack.CHECK_SUCCESS)
			throw new IllegalOperationException("请先验证成功再审核");
		// 增加还款任务
		changeState(payback.getId(), PayBack.STATE_REPAYING);
		Task task = new Task();
		task.setCreateTime(System.currentTimeMillis());
		task.setPayBackId(payback.getId());
		task.setProductId(payback.getProductId());
		task.setState(Task.STATE_INIT);
		task.setType(Task.TYPE_REPAY);
		taskService.submit(task);
		accountService.unfreezeBorrowerAccount(payback.getBorrowerAccountId(),payback.getChiefAmount().add(payback.getInterest()), payback.getId(), "解冻");
		if (payback.getType() == PayBack.TYPE_LASTPAY) {
			// TODO 金额正确，设置产品状态为还款完毕
			productService.finishRepay(payback.getProductId());
			Product product = productService.find(payback.getProductId());
			List<Product> allProducts = productService.findByGovermentOrder(product.getGovermentorderId());
			boolean isAllRepay = true;
			for (Product pro : allProducts) {
				if (pro.getState() != Product.STATE_FINISHREPAY) {
					isAllRepay = false;
					break;
				}
			}
			if (isAllRepay)
				orderService.closeFinancing(product.getGovermentorderId());
		}
	}

	@Override
	public List<PayBack> findWaitforCheckPayBacks() {
		List<PayBack> paybacks = payBackDao.findByProductsAndState(null, PayBack.STATE_WAITFORCHECK);
		for(PayBack payBack:paybacks)
		{
			Product product=productDao.find(payBack.getProductId());
			
			payBack.setChiefAmount(payBack.getChiefAmount().multiply(product.getRealAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
			payBack.setInterest(payBack.getInterest().multiply(product.getRealAmount()).divide(PayBack.BASELINE,2,BigDecimal.ROUND_UP));
			
			payBack.setProduct(product);
			product.setGovermentOrder(govermentOrderDao.find(product.getGovermentorderId()));
			product.setProductSeries(productSeriesDao.find(product.getProductseriesId()));
		}
		return paybacks;
	}
	@Override
	public void checkoutPayBack(Integer payBackId) throws CheckException {
		PayBack payBack=find(payBackId);
		if(payBack==null)
			return;
		List<Submit> submits=submitDao.findAllByProductAndState(payBack.getProductId(), Submit.STATE_COMPLETEPAY);
		if(submits==null||submits.size()==0)
			return;
		Product product=productDao.find(payBack.getProductId());
		BigDecimal totalChiefAmount=payBack.getChiefAmount();
		BigDecimal totalInterest=payBack.getInterest();
		List<BigDecimal> chiefAmounts=new ArrayList<BigDecimal>();
		List<BigDecimal> interestAmounts=new ArrayList<BigDecimal>();
		loop:for(int i=0;i<submits.size();i++)
		{
			Submit submit=submits.get(i);
			BigDecimal lenderChiefAmount=null;
			BigDecimal lenderInterest=null;
			if(payBack.getType()==PayBack.TYPE_LASTPAY)
			{
				List<CashStream> cashStreams=cashStreamDao.findRepayCashStream(submit.getId(), null);
				BigDecimal repayedChiefAmount=BigDecimal.ZERO;
				if(cashStreams!=null&&cashStreams.size()>0)
				{
					for(CashStream cashStream:cashStreams)
					{
						repayedChiefAmount=repayedChiefAmount.add(cashStream.getChiefamount());
					}
				}
				lenderChiefAmount=submit.getAmount().subtract(repayedChiefAmount);
			}
			else {
				lenderChiefAmount=payBack.getChiefAmount().multiply(submit.getAmount()).divide(product.getRealAmount(), 2, BigDecimal.ROUND_DOWN);
			}
			lenderInterest=payBack.getInterest().multiply(submit.getAmount()).divide(product.getRealAmount(), 2, BigDecimal.ROUND_DOWN);
			totalChiefAmount=totalChiefAmount.subtract(lenderChiefAmount);
			totalInterest=totalInterest.subtract(lenderInterest);
			chiefAmounts.add(lenderChiefAmount);
			interestAmounts.add(lenderInterest);
		}
		chiefAmounts.add(totalChiefAmount);
		interestAmounts.add(totalInterest);
		BigDecimal total=BigDecimal.ZERO;
		for(BigDecimal amount:chiefAmounts)
		{
			total=total.add(amount);
		}
		if(total.compareTo(payBack.getChiefAmount())!=0)
			throw new CheckException("当前还款金额计算不符");
		total=BigDecimal.ZERO;
		for(BigDecimal amount:interestAmounts)
		{
			total=total.add(amount);
		}
		if(total.compareTo(payBack.getInterest())!=0)
			throw new CheckException("当前还款金额计算不符");
		// 最后一次验证,验证之前的payback是否符合
		if(payBack.getType()==PayBack.TYPE_LASTPAY)
		{
			BigDecimal amount=BigDecimal.ZERO;
			List<PayBack> payBacks=findAll(payBack.getProductId());
			for(PayBack pb:payBacks)
			{
				if(pb.getType()==PayBack.TYPE_LASTPAY)
					continue;
				if(pb.getState()==PayBack.STATE_REPAYING)
					throw new CheckException("之前还有执行中的还款");
				if(pb.getState()!=PayBack.STATE_FINISHREPAY)
					continue;
				CashStreamSum sum=cashStreamDao.sumPayBack(pb.getId());
				if(sum.getChiefAmount().compareTo(pb.getChiefAmount())!=0||sum.getInterest().compareTo(pb.getInterest())!=0)
					throw new CheckException("还款[id:"+pb.getId()+"]金额计算不符");
				amount=amount.add(pb.getChiefAmount());
			}
			if(amount.add(payBack.getChiefAmount()).compareTo(product.getRealAmount())!=0)
				throw new CheckException("还款总额与产品不符");
		}
		payBackDao.changeCheckResult(payBackId, PayBack.CHECK_SUCCESS);
	}
}
