package gpps;

import gpps.dao.IBorrowerAccountDao;
import gpps.dao.IBorrowerDao;
import gpps.dao.ICashStreamDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.ILenderAccountDao;
import gpps.dao.ILenderDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.dao.ISubmitDao;
import gpps.model.Borrower;
import gpps.model.BorrowerAccount;
import gpps.model.CashStream;
import gpps.model.FinancingRequest;
import gpps.model.GovermentOrder;
import gpps.model.Lender;
import gpps.model.LenderAccount;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.Submit;
import gpps.service.CashStreamSum;
import gpps.service.IAccountService;
import gpps.service.IBorrowerService;
import gpps.service.IGovermentOrderService;
import gpps.service.ILenderService;
import gpps.service.IPayBackService;
import gpps.service.IProductService;
import gpps.service.ISubmitService;
import gpps.service.exception.CheckException;
import gpps.service.exception.ExistWaitforPaySubmitException;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.IllegalOperationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class BugPerformanceTest extends TestSupport{
	static ILenderService lenderService;
	static ILenderDao lenderDao;
	static ILenderAccountDao lenderAccountDao;
	static IAccountService accountService;
	static IBorrowerService borrowerService;
	static IBorrowerDao borrowerDao;
	static IBorrowerAccountDao borrowerAccountDao;
	static IGovermentOrderService orderService;
	static IGovermentOrderDao orderDao;
	static IProductSeriesDao productSeriesDao;
	static IProductService productService;
	static IProductDao productDao;
	static ICashStreamDao cashStreamDao;
	static ISubmitService submitService;
	static ISubmitDao submitDao;
	static IPayBackService payBackService;
	static IPayBackDao payBackDao;
	static Logger logger=Logger.getLogger("buytest");
	public static void main(String[] args)
	{
		lenderService = context.getBean(ILenderService.class);
		lenderDao = context.getBean(ILenderDao.class);
		lenderAccountDao = context.getBean(ILenderAccountDao.class);
		accountService = context.getBean(IAccountService.class);
		borrowerService = context.getBean(IBorrowerService.class);
		borrowerDao = context.getBean(IBorrowerDao.class);
		borrowerAccountDao = context.getBean(IBorrowerAccountDao.class);
		orderService = context.getBean(IGovermentOrderService.class);
		orderDao=context.getBean(IGovermentOrderDao.class);
		productSeriesDao=context.getBean(IProductSeriesDao.class);
		productService=context.getBean(IProductService.class);
		productDao=context.getBean(IProductDao.class);
		cashStreamDao=context.getBean(ICashStreamDao.class);
		submitService=context.getBean(ISubmitService.class);
		submitDao=context.getBean(ISubmitDao.class);
		payBackService=context.getBean(IPayBackService.class);
		payBackDao=context.getBean(IPayBackDao.class);
		
		Random random=new Random();
		long currenttime=System.currentTimeMillis();
		int lenderNum=50;
		int buyNum=4;
		try
		{
			List<Lender> lenders=new ArrayList<Lender>();
			for(int i=0;i<lenderNum;i++)
			{
				//创建Lender、充值100w
				Lender lender=createLender(""+currenttime+i);
//				logger.info("createLender耗时"+(System.currentTimeMillis()-currenttime));
				currenttime=System.currentTimeMillis();
				accountService.changeCashStreamState(accountService.rechargeLenderAccount(lender.getAccountId(), new BigDecimal(100*10000), "充值"),CashStream.STATE_SUCCESS);
//				logger.info("changeCashStreamState耗时"+(System.currentTimeMillis()-currenttime));
				lenders.add(lender);
			}
			
			//创建Borrower、充值10000w
			Borrower borrower=createBorrower(""+currenttime+random.nextInt(2));
			accountService.changeCashStreamState(accountService.rechargeBorrowerAccount(borrower.getAccountId(), new BigDecimal(10000*10000), "充值"),CashStream.STATE_SUCCESS);
			FinancingRequest financingRequest=new FinancingRequest();
			financingRequest.setApplyFinancingAmount(10000000);
			financingRequest.setGovermentOrderName("申请融资订单");
			mockLogin(borrower);
			borrowerService.applyFinancing(financingRequest);
			GovermentOrder order=new GovermentOrder();
			order.setBorrowerId(borrower.getId());
			order.setFinancingRequestId(financingRequest.getId());
			order.setTitle("融资订单1");
			order.setFinancingStarttime(currenttime+24L*3600*1000);
			order.setFinancingEndtime(currenttime+24L*2*3600*1000);
			order.setIncomeStarttime(currenttime+24L*3*3600*1000);
			orderService.create(order);
			Product product=new Product();
			product.setExpectAmount(new BigDecimal(1000*10000));
			product.setGovermentorderId(order.getId());
			product.setIncomeEndtime(currenttime+24L*(3+90)*3600*1000);
			product.setLevelToBuy(0);
			product.setMiniAdd(1);
			product.setMinimum(1);
			product.setProductseriesId(1);
			product.setRate(new BigDecimal(0.08));
			productService.create(product);
			borrowerService.passFinancingRequest(financingRequest.getId());
			orderService.startFinancing(order.getId());
			
			logger.info("开始多线程购买");
			//创建多线程测试购买
			List<FutureTask<List<Long>>> futures=new ArrayList<FutureTask<List<Long>>>();
			for(Lender lender:lenders)
			{
				FutureTask<List<Long>> future = new FutureTask<List<Long>>(new BuyThread(buyNum,lender,product));
				new Thread(future).start();
				futures.add(future);
			}
			long totalCost=0;
			long num=0;
			for(FutureTask<List<Long>> future:futures)
			{
				List<Long> costs=future.get();
				num=num+costs.size();
				for(long cost:costs)
					totalCost=totalCost+cost;
			}
			logger.info("共购买"+num+"次，平均耗时："+totalCost/num);
			System.exit(-1);
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	static class BuyThread implements Callable<List<Long>>
	{
		int buyNum;
		Lender lender;
		Product product;
		public BuyThread(int buyNum,Lender lender,Product product)
		{
			this.buyNum=buyNum;
			this.lender=lender;
			this.product=product;
		}
		@Override
		public List<Long> call() throws Exception
		{
			List<Long> costs=new ArrayList<Long>(buyNum);
			//购买
			try
			{
				long currenttime=System.currentTimeMillis();
				for(int i=0;i<buyNum;i++)
				{
					mockLogin(lender);
					Integer submitId=submitService.buy(product.getId(), 1);
					Integer cashStreamId = accountService.freezeLenderAccount(lender.getAccountId(), new BigDecimal(1), submitId, "购买");
					submitService.confirmBuy(submitId);
		//			cashStreamDao.updateLoanNo(cashStreamId, loanNo,null);
					accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
					long cost=System.currentTimeMillis()-currenttime;
					logger.info("lender,id="+lender.getId()+"执行第"+i+"次购买，耗时:"+cost+"ms");
					costs.add(cost);
					currenttime=System.currentTimeMillis();
				}
			}catch(Throwable e)
			{
				e.printStackTrace();
			}
			return costs;
		}
	}
	public void buyProductProcess() throws Exception{
		Random random=new Random();
		long currenttime=System.currentTimeMillis();
		//创建Lender、充值100w
		Lender lender=createLender(""+currenttime+random.nextInt(2));
		accountService.changeCashStreamState(accountService.rechargeLenderAccount(lender.getAccountId(), new BigDecimal(100*10000), "充值"),CashStream.STATE_SUCCESS);
		//创建Borrower、充值100w
		Borrower borrower=createBorrower(""+currenttime+random.nextInt(2));
		accountService.changeCashStreamState(accountService.rechargeBorrowerAccount(borrower.getAccountId(), new BigDecimal(100*10000), "充值"),CashStream.STATE_SUCCESS);
		
		FinancingRequest financingRequest=new FinancingRequest();
		financingRequest.setApplyFinancingAmount(1000000);
		financingRequest.setGovermentOrderName("申请融资订单");
		mockLogin(borrower);
		borrowerService.applyFinancing(financingRequest);
		GovermentOrder order=new GovermentOrder();
		order.setBorrowerId(borrower.getId());
		order.setFinancingRequestId(financingRequest.getId());
		order.setTitle("融资订单1");
		order.setFinancingStarttime(currenttime+24L*3600*1000);
		order.setFinancingEndtime(currenttime+24L*2*3600*1000);
		order.setIncomeStarttime(currenttime+24L*3*3600*1000);
		orderService.create(order);
		Product product=new Product();
		product.setExpectAmount(new BigDecimal(100*10000));
		product.setGovermentorderId(order.getId());
		product.setIncomeEndtime(currenttime+24L*(3+90)*3600*1000);
		product.setLevelToBuy(0);
		product.setMiniAdd(1);
		product.setMinimum(1);
		product.setProductseriesId(1);
		product.setRate(new BigDecimal(0.08));
		productService.create(product);
		borrowerService.passFinancingRequest(financingRequest.getId());
		orderService.startFinancing(order.getId());
		
		//购买
		mockLogin(lender);
		Integer submitId=submitService.buy(product.getId(), 10*10000);
		Integer cashStreamId = accountService.freezeLenderAccount(lender.getAccountId(), new BigDecimal(10*10000), submitId, "购买");
		submitService.confirmBuy(submitId);
//		cashStreamDao.updateLoanNo(cashStreamId, loanNo,null);
		accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
//		

		submitId=submitService.buy(product.getId(), 20*10000);
		cashStreamId = accountService.freezeLenderAccount(lender.getAccountId(), new BigDecimal(20*10000), submitId, "购买");
		submitService.confirmBuy(submitId);
//		cashStreamDao.updateLoanNo(cashStreamId, loanNo,null);
		accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		
		
		startRepaying(order.getId());
		
		List<PayBack> payBacks=payBackService.findAll(product.getId());
		Assert.assertEquals(3, payBacks.size());
		//对照网上的计算器http://finance.sina.com.cn/calc/money_loan.html
		for(int i=0;i<payBacks.size();i++)
		{
			PayBack payBack=payBacks.get(i);
			switch(i)
			{
				case 0:
					Assert.assertEquals("99336.27", payBack.getChiefAmount().toString());
					Assert.assertEquals("2000.01", payBack.getInterest().toString());
					break;
				case 1:
					Assert.assertEquals("99998.52", payBack.getChiefAmount().toString());
					Assert.assertEquals("1337.76", payBack.getInterest().toString());
					break;
				case 2:
					Assert.assertEquals("100665.21", payBack.getChiefAmount().toString());
					Assert.assertEquals("671.10", payBack.getInterest().toString());
					break;
			}
			mockLogin(borrower);
			payBackService.repay(payBack.getId());
			payBackDao.changeCheckResult(payBack.getId(), PayBack.CHECK_SUCCESS);
			checkAndRepay(payBack.getId());
		}
		orderService.closeComplete(order.getId());
		
		LenderAccount lenderAccount=lenderAccountDao.find(lender.getAccountId());
		Assert.assertEquals(0, lenderAccount.getFreeze().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getUsed().compareTo(BigDecimal.ZERO));
		Assert.assertEquals("1004008.87", lenderAccount.getTotal().toString());
		Assert.assertEquals("4008.87", lenderAccount.getTotalincome().toString());
		Assert.assertEquals("1004008.87", lenderAccount.getUsable().toString());
		
		BorrowerAccount borrowerAccount=borrowerAccountDao.find(borrower.getAccountId());
		Assert.assertEquals(0, borrowerAccount.getFreeze().compareTo(BigDecimal.ZERO));
		Assert.assertEquals("995991.13", borrowerAccount.getTotal().toString());
		Assert.assertEquals("995991.13", borrowerAccount.getUsable().toString());
	}
	private void startRepaying(Integer orderId) throws IllegalConvertException,IllegalOperationException, ExistWaitforPaySubmitException, CheckException {
		GovermentOrder order=null;
		try
		{
			order=orderService.applyFinancingOrder(orderId);
			if(order!=null)
			{
				List<Product> products=order.getProducts();
				if(products!=null&&products.size()>0)
				{
//					throw new IllegalOperationException("还有竞标中的产品，请先修改产品状态");
					List<Product> temp=new ArrayList<Product>();
					temp.addAll(products);
					for(Product product:temp)
					{
//						productService.startRepaying(product.getId());
						// 验证是否有待付款的Submit
						int count=submitDao.countByProductAndStateWithPaged(product.getId(), Submit.STATE_WAITFORPAY);
						if(count>0)
							throw new ExistWaitforPaySubmitException("还有"+count+"个待支付的提交,请等待上述提交全部结束，稍后开始还款");
						//校验 Product实际融资额=所有Lender的支付资金流之和
						CashStreamSum sum=cashStreamDao.sumProduct(product.getId(), CashStream.ACTION_FREEZE);
						if(sum.getChiefAmount().negate().compareTo(product.getRealAmount())!=0)
							throw new CheckException("冻结提交总金额与产品实际融资金额不符");
						//从竞标缓存中移除
						productDao.changeState(product.getId(), Product.STATE_REPAYING,System.currentTimeMillis());
						product=order.findProductById(product.getId());
						order.getProducts().remove(product);
						product.setState(Product.STATE_REPAYING);
						//审核转账
						executePayTask(product);
						
					}
				}
				orderDao.changeState(orderId, GovermentOrder.STATE_REPAYING,System.currentTimeMillis());
//				order=financingOrders.remove(orderId.toString());
				order.setState(GovermentOrder.STATE_REPAYING);
			}
		}finally
		{
			orderService.releaseFinancingOrder(order);
		}
	}
	private void executePayTask(Product product) throws IllegalConvertException
	{
		List<Submit> submits=submitDao.findAllByProductAndState(product.getId(), Submit.STATE_COMPLETEPAY);
		if(submits==null||submits.size()==0)
			return;
		GovermentOrder order=orderService.findGovermentOrderByProduct(product.getId());
		Borrower borrower=borrowerService.find(order.getBorrowerId());
		loop:for(Submit submit:submits)
		{
			CashStream freezeCS=null;
			List<CashStream> cashStreams=cashStreamDao.findSubmitCashStream(submit.getId());
			for(CashStream cashStream:cashStreams)
			{
				if(cashStream.getAction()==CashStream.ACTION_PAY&&cashStream.getState()==CashStream.STATE_SUCCESS)
				{
					continue loop;
				}
				if(cashStream.getAction()==CashStream.ACTION_FREEZE&&cashStream.getState()==CashStream.STATE_SUCCESS)
					freezeCS=cashStream;
			}
			if(freezeCS==null)
				continue;
			accountService.pay(freezeCS.getLenderAccountId(), borrower.getAccountId(),freezeCS.getChiefamount().negate(),freezeCS.getSubmitId(), "支付");
			
		}
	}
	private void checkAndRepay(Integer payBackId) throws Exception
	{
		PayBack payback=payBackService.find(payBackId);
		if(payback==null||payback.getState()!=PayBack.STATE_WAITFORCHECK)
			return;
		if(payback.getCheckResult()!=PayBack.CHECK_SUCCESS)
			throw new IllegalOperationException("请先验证成功再审核");
		accountService.unfreezeBorrowerAccount(payback.getBorrowerAccountId(),payback.getChiefAmount().add(payback.getInterest()), payback.getId(), "解冻");
		// 执行还款任务
		executeRepayTask(payback);
		payBackService.changeState(payback.getId(), PayBack.STATE_FINISHREPAY);
		if (payback.getType() == PayBack.TYPE_LASTPAY) {
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
	private void executeRepayTask(PayBack payBack) throws Exception
	{
		List<Submit> submits=submitDao.findAllByProductAndState(payBack.getProductId(), Submit.STATE_COMPLETEPAY);
		if(submits==null||submits.size()==0)
			return;
		Product product=productDao.find(payBack.getProductId());
		GovermentOrder order=orderDao.find(product.getGovermentorderId());
		Borrower borrower=borrowerDao.find(order.getBorrowerId());
		BigDecimal totalChiefAmount=payBack.getChiefAmount();
		BigDecimal totalInterest=payBack.getInterest();
		loop:for(int i=0;i<submits.size();i++)
		{
			Submit submit=submits.get(i);
//			if(interrupted)
//			{
//				List<CashStream> cashStreams=cashStreamDao.findRepayCashStream(submit.getId(), payBack.getId());
//				if(cashStreams!=null&&cashStreams.size()>0)
//				{
//					CashStream cashStream=cashStreams.get(0);
//					totalChiefAmount.subtract(cashStream.getChiefamount());
//					totalInterest.subtract(cashStream.getInterest());
//					logger.debug("还款任务["+task.getId()+"],Submit["+submit.getId()+"]已执行过。");
//					continue loop;
//				}
//			}
			Lender lender=lenderDao.find(submit.getLenderId());
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
			Integer cashStreamId=accountService.repay(lender.getAccountId(), payBack.getBorrowerAccountId(), lenderChiefAmount, lenderInterest, submit.getId(), payBack.getId(), "还款");
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		}
		BigDecimal change=totalChiefAmount.add(totalInterest);
		if(change.compareTo(BigDecimal.ZERO)>0)
		{
			//有余额则放入自有账户中
			Integer cashStreamId=accountService.storeChange(payBack.getBorrowerAccountId(),payBack.getId(),change, "存零");
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		}
	}
	private static Borrower createBorrower(String tel)
	{
		try {
			MockHttpServletRequest request =  new MockHttpServletRequest("POST","/test.do");
			ServletRequestAttributes attributes=new ServletRequestAttributes(request);
			RequestContextHolder.setRequestAttributes(attributes);
			HttpSession session=borrowerService.getCurrentSession();
			borrowerService.sendMessageValidateCode();
			Borrower borrower=new Borrower();
			borrower.setEmail(tel+"@calis.edu.cn");
			borrower.setIdentityCard(tel+"111111111111111111");
			borrower.setLoginId("B"+tel);
			borrower.setName("测试B"+tel);
			borrower.setPassword(tel);
			borrower.setLicense(tel);
			borrower.setTel(tel);
			borrower= borrowerService.register(borrower, (String)session.getAttribute(ILenderService.SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODE));
			mockLogin(borrower);
			borrowerService.applyForFunding();
			borrowerService.passFundingApplying(borrower.getId());
			borrowerService.registerThirdPartyAccount(borrower.getId(), tel, tel);
			borrowerDao.updateAuthorizeTypeOpen(borrower.getId(), Borrower.AUTHORIZETYPEOPEN_RECHARGE);
			return borrowerService.find(borrower.getId());
		} catch (Throwable e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return null;
	}
	private static Lender createLender(String tel)
	{
		try {
			MockHttpServletRequest request =  new MockHttpServletRequest("POST","/test.do");
			ServletRequestAttributes attributes=new ServletRequestAttributes(request);
			RequestContextHolder.setRequestAttributes(attributes);
			HttpSession session=lenderService.getCurrentSession();
			lenderService.sendMessageValidateCode();
			Lender lender=new Lender();
			lender.setEmail(tel+"@calis.edu.cn");
			lender.setLoginId("L"+tel);
			lender.setName("测试L"+tel);
			lender.setPassword(tel);
			lender.setTel(tel);
			lender= lenderService.register(lender, (String)session.getAttribute(ILenderService.SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODE));
			lenderService.registerSecondStep(lender.getName(), tel+"111111111111111111", lender.getSex(), "北京","百万" );
			lenderService.registerThirdPartyAccount(lender.getId(), tel, tel);
			return lenderService.find(lender.getId());
		} catch (Throwable e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return null;
	}
	private static void mockLogin(Object user)
	{
		MockHttpServletRequest request =  new MockHttpServletRequest("POST","/test.do");
		ServletRequestAttributes attributes=new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attributes);
		HttpSession session=request.getSession();
		session.setAttribute(IBorrowerService.SESSION_ATTRIBUTENAME_USER, user);
	}
}
