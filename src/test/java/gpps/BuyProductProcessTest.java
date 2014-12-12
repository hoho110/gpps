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
import gpps.model.Task;
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
import gpps.service.exception.InsufficientBalanceException;
import gpps.service.exception.InsufficientProductException;
import gpps.service.exception.ProductSoldOutException;
import gpps.service.exception.UnreachBuyLevelException;
import gpps.service.thirdpay.Transfer.LoanJson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class BuyProductProcessTest extends TestSupport{
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

	@BeforeClass
	public static void setUpBeforeClass() {
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
	}
	@Test
	public void buyProductProcess() throws Exception{
		Random random=new Random();
		long currenttime=System.currentTimeMillis();
		//创建Lender、充值100w
		Lender lender=createLender(""+currenttime+random.nextInt(2));
		
		LenderAccount lenderAccount=lenderAccountDao.find(lender.getAccountId());
		Assert.assertEquals(0, lenderAccount.getFreeze().compareTo(new BigDecimal(0)));
		Assert.assertEquals(0, lenderAccount.getUsed().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getTotal().compareTo(new BigDecimal(0)));
		Assert.assertEquals(0, lenderAccount.getTotalincome().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getUsable().compareTo(new BigDecimal(0)));
		
		accountService.changeCashStreamState(accountService.rechargeLenderAccount(lender.getAccountId(), new BigDecimal(100*10000), "充值"),CashStream.STATE_SUCCESS);
		
		
		lenderAccount=lenderAccountDao.find(lender.getAccountId());
		Assert.assertEquals(0, lenderAccount.getFreeze().compareTo(new BigDecimal(0)));
		Assert.assertEquals(0, lenderAccount.getUsed().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getTotal().compareTo(new BigDecimal(1000000)));
		Assert.assertEquals(0, lenderAccount.getTotalincome().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getUsable().compareTo(new BigDecimal(1000000)));
		
		//创建Borrower、充值100w
		Borrower borrower=createBorrower(""+currenttime+random.nextInt(2));
		accountService.changeCashStreamState(accountService.rechargeBorrowerAccount(borrower.getAccountId(), new BigDecimal(100*10000), "充值"),CashStream.STATE_SUCCESS);
		//流标
		FinancingRequest financingRequest=new FinancingRequest();
		financingRequest.setApplyFinancingAmount(1000000);
		financingRequest.setGovermentOrderName("申请融资订单(流标)");
		mockLogin(borrower);
		borrowerService.applyFinancing(financingRequest);
		GovermentOrder order=new GovermentOrder();
		order.setBorrowerId(borrower.getId());
		order.setFinancingRequestId(financingRequest.getId());
		order.setTitle("融资订单1(流标)");
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
		Integer submitId=submitService.buy(product.getId(), 30*10000);
		Integer cashStreamId = accountService.freezeLenderAccount(lender.getAccountId(), new BigDecimal(30*10000), submitId, "购买");
		submitService.confirmBuy(submitId);
		//				cashStreamDao.updateLoanNo(cashStreamId, loanNo,null);
		accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		

		lenderAccount=lenderAccountDao.find(lender.getAccountId());
		Assert.assertEquals(0, lenderAccount.getFreeze().compareTo(new BigDecimal(300000)));
		Assert.assertEquals(0, lenderAccount.getUsed().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getTotal().compareTo(new BigDecimal(1000000)));
		Assert.assertEquals(0, lenderAccount.getTotalincome().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getUsable().compareTo(new BigDecimal(700000)));
		
		quitFinancing(order.getId());
		 lenderAccount=lenderAccountDao.find(lender.getAccountId());
		Assert.assertEquals(0, lenderAccount.getFreeze().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getUsed().compareTo(BigDecimal.ZERO));
		Assert.assertEquals("1000000.00", lenderAccount.getTotal().toString());
		Assert.assertEquals("0.00", lenderAccount.getTotalincome().toString());
		Assert.assertEquals("1000000.00", lenderAccount.getUsable().toString());
		
		
		//还款
		financingRequest=new FinancingRequest();
		financingRequest.setApplyFinancingAmount(1000000);
		financingRequest.setGovermentOrderName("申请融资订单");
		mockLogin(borrower);
		borrowerService.applyFinancing(financingRequest);
		order=new GovermentOrder();
		order.setBorrowerId(borrower.getId());
		order.setFinancingRequestId(financingRequest.getId());
		order.setTitle("融资订单1");
		order.setFinancingStarttime(currenttime+24L*3600*1000);
		order.setFinancingEndtime(currenttime+24L*2*3600*1000);
		order.setIncomeStarttime(currenttime+24L*3*3600*1000);
		orderService.create(order);
		product=new Product();
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
		submitId=submitService.buy(product.getId(), 10*10000);
		cashStreamId = accountService.freezeLenderAccount(lender.getAccountId(), new BigDecimal(10*10000), submitId, "购买");
		submitService.confirmBuy(submitId);
//		cashStreamDao.updateLoanNo(cashStreamId, loanNo,null);
		accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		
		lenderAccount=lenderAccountDao.find(lender.getAccountId());
		Assert.assertEquals(0, lenderAccount.getFreeze().compareTo(new BigDecimal(100000)));
		Assert.assertEquals(0, lenderAccount.getUsed().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getTotal().compareTo(new BigDecimal(1000000)));
		Assert.assertEquals(0, lenderAccount.getTotalincome().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getUsable().compareTo(new BigDecimal(900000)));
//		

		submitId=submitService.buy(product.getId(), 20*10000);
		cashStreamId = accountService.freezeLenderAccount(lender.getAccountId(), new BigDecimal(20*10000), submitId, "购买");
		submitService.confirmBuy(submitId);
//		cashStreamDao.updateLoanNo(cashStreamId, loanNo,null);
		accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		
		
		lenderAccount=lenderAccountDao.find(lender.getAccountId());
		Assert.assertEquals(0, lenderAccount.getFreeze().compareTo(new BigDecimal(300000)));
		Assert.assertEquals(0, lenderAccount.getUsed().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getTotal().compareTo(new BigDecimal(1000000)));
		Assert.assertEquals(0, lenderAccount.getTotalincome().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getUsable().compareTo(new BigDecimal(700000)));
		
		
		startRepaying(order.getId());
		
		BorrowerAccount baccount = borrowerAccountDao.find(borrower.getAccountId());
		Assert.assertEquals(0, baccount.getFreeze().compareTo(new BigDecimal(0)));
		Assert.assertEquals(0, baccount.getTotal().compareTo(new BigDecimal(1300000)));
		Assert.assertEquals(0, baccount.getUsable().compareTo(new BigDecimal(1300000)));
		
		
		lenderAccount=lenderAccountDao.find(lender.getAccountId());
		Assert.assertEquals(0, lenderAccount.getFreeze().compareTo(new BigDecimal(0)));
		Assert.assertEquals(0, lenderAccount.getUsed().compareTo(new BigDecimal(300000)));
		Assert.assertEquals(0, lenderAccount.getTotal().compareTo(new BigDecimal(1000000)));
		Assert.assertEquals(0, lenderAccount.getTotalincome().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getUsable().compareTo(new BigDecimal(700000)));
		
		
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
			baccount = borrowerAccountDao.find(borrower.getAccountId());
			switch(i){
			case 0:
				Assert.assertEquals(0, baccount.getFreeze().compareTo(new BigDecimal("101336.28")));
				Assert.assertEquals(0, baccount.getTotal().compareTo(new BigDecimal(1300000)));
				Assert.assertEquals(0, baccount.getUsable().compareTo(new BigDecimal(1300000).subtract(new BigDecimal("101336.28"))));
				break;
			case 1:
				Assert.assertEquals(0, baccount.getFreeze().compareTo(new BigDecimal("101336.28")));
				Assert.assertEquals(0, baccount.getTotal().compareTo(new BigDecimal("1198663.72")));
				Assert.assertEquals(0, baccount.getUsable().compareTo(new BigDecimal("1097327.44")));
				break;
			case 2:
				Assert.assertEquals(0, baccount.getFreeze().compareTo(new BigDecimal("101336.31")));
				Assert.assertEquals(0, baccount.getTotal().compareTo(new BigDecimal("1097327.44")));
				Assert.assertEquals(0, baccount.getUsable().compareTo(new BigDecimal("995991.13")));
				break;
			}
			payBackDao.changeCheckResult(payBack.getId(), PayBack.CHECK_SUCCESS);
			checkAndRepay(payBack.getId());
			
			
			baccount = borrowerAccountDao.find(borrower.getAccountId());
			switch(i){
			case 0:
				Assert.assertEquals(0, baccount.getFreeze().compareTo(new BigDecimal("0")));
				Assert.assertEquals(0, baccount.getTotal().compareTo(new BigDecimal("1198663.72")));
				Assert.assertEquals(0, baccount.getUsable().compareTo(new BigDecimal("1198663.72")));
				break;
			case 1:
				Assert.assertEquals(0, baccount.getFreeze().compareTo(new BigDecimal("0")));
				Assert.assertEquals(0, baccount.getTotal().compareTo(new BigDecimal("1097327.44")));
				Assert.assertEquals(0, baccount.getUsable().compareTo(new BigDecimal("1097327.44")));
				break;
			case 2:
				Assert.assertEquals(0, baccount.getFreeze().compareTo(new BigDecimal("0")));
				Assert.assertEquals(0, baccount.getTotal().compareTo(new BigDecimal("995991.13")));
				Assert.assertEquals(0, baccount.getUsable().compareTo(new BigDecimal("995991.13")));
				break;
			}
			lenderAccount = lenderAccountDao.find(lender.getAccountId());
			switch(i){
			case 0:
				
				Assert.assertEquals(0, lenderAccount.getFreeze().compareTo(new BigDecimal(0)));
				Assert.assertEquals(0, lenderAccount.getUsed().compareTo(new BigDecimal("200663.73")));
				Assert.assertEquals(0, lenderAccount.getTotal().compareTo(new BigDecimal("1002000.01")));
				Assert.assertEquals(0, lenderAccount.getTotalincome().compareTo(new BigDecimal("2000.01")));
				Assert.assertEquals(0, lenderAccount.getUsable().compareTo(new BigDecimal("801336.28")));
				break;
			case 1:
				
				Assert.assertEquals(0, lenderAccount.getFreeze().compareTo(new BigDecimal(0)));
				Assert.assertEquals(0, lenderAccount.getUsed().compareTo(new BigDecimal("100665.21")));
				Assert.assertEquals(0, lenderAccount.getTotal().compareTo(new BigDecimal("1003337.77")));
				Assert.assertEquals(0, lenderAccount.getTotalincome().compareTo(new BigDecimal("3337.77")));
				Assert.assertEquals(0, lenderAccount.getUsable().compareTo(new BigDecimal("902672.56")));
				break;
			case 2:
				Assert.assertEquals(0, lenderAccount.getFreeze().compareTo(new BigDecimal(0)));
				Assert.assertEquals(0, lenderAccount.getUsed().compareTo(new BigDecimal(0)));
				Assert.assertEquals(0, lenderAccount.getTotal().compareTo(new BigDecimal("1004008.87")));
				Assert.assertEquals(0, lenderAccount.getTotalincome().compareTo(new BigDecimal("4008.87")));
				Assert.assertEquals(0, lenderAccount.getUsable().compareTo(new BigDecimal("1004008.87")));
				break;
			}
			
			
		}
		orderService.closeComplete(order.getId());
		
		lenderAccount=lenderAccountDao.find(lender.getAccountId());
		Assert.assertEquals(0, lenderAccount.getFreeze().compareTo(BigDecimal.ZERO));
		Assert.assertEquals(0, lenderAccount.getUsed().compareTo(BigDecimal.ZERO));
		Assert.assertEquals("1004008.87", lenderAccount.getTotal().toString());
		Assert.assertEquals("4008.87", lenderAccount.getTotalincome().toString());
		Assert.assertEquals("1004008.87", lenderAccount.getUsable().toString());
		
		BorrowerAccount borrowerAccount=borrowerAccountDao.find(borrower.getAccountId());
		Assert.assertEquals(0, borrowerAccount.getFreeze().compareTo(BigDecimal.ZERO));
		Assert.assertEquals("995991.13", borrowerAccount.getTotal().toString());
		Assert.assertEquals("995991.13", borrowerAccount.getUsable().toString());
		
		checkPayBack(product);
	}
	public static void checkPayBack(Product product) throws CheckException
	{
		BigDecimal amount=BigDecimal.ZERO;
		List<PayBack> payBacks=payBackService.findAll(product.getId());
		for(PayBack pb:payBacks)
		{
			if(pb.getState()==PayBack.STATE_REPAYING)
				throw new CheckException("之前还有执行中的还款");
			if(pb.getState()!=PayBack.STATE_FINISHREPAY)
				continue;
			CashStreamSum sum=cashStreamDao.sumPayBack(pb.getId());
			if(sum.getChiefAmount().compareTo(pb.getChiefAmount())!=0||sum.getInterest().compareTo(pb.getInterest())!=0)
				throw new CheckException("还款[id:"+pb.getId()+"]金额计算不符");
			amount.add(pb.getChiefAmount());
		}
		if(amount.compareTo(product.getRealAmount())!=0)
			throw new CheckException("还款总额与产品不符");
	}
	public void quitFinancing(Integer orderId) throws IllegalConvertException, IllegalOperationException, ExistWaitforPaySubmitException, CheckException {
		GovermentOrder order=null;
		try
		{
			order=orderService.applyFinancingOrder(orderId);
			if(order!=null)
			{
				List<Product> products=order.getProducts();
				if(products!=null&&products.size()>0)
				{
					List<Product> temp=new ArrayList<Product>();
					temp.addAll(products);
					for(Product product:temp)
					{
						int count=submitDao.countByProductAndStateWithPaged(product.getId(), Submit.STATE_WAITFORPAY);
						if(count>0)
							throw new ExistWaitforPaySubmitException("还有"+count+"个待支付的提交,请等待上述提交全部结束，稍后开始流标");
						//校验 Product实际融资额=所有Lender的支付资金流之和
						CashStreamSum sum=cashStreamDao.sumProduct(product.getId(), CashStream.ACTION_FREEZE);
						if(sum.getChiefAmount().negate().compareTo(product.getRealAmount())!=0)
							throw new CheckException("冻结提交总金额与产品实际融资金额不符");
						
						productDao.changeState(product.getId(), Product.STATE_QUITFINANCING,System.currentTimeMillis());
						order.getProducts().remove(product);
						product.setState(Product.STATE_QUITFINANCING);

						executeQuitFinancingTask(product);
					}
				}
				orderDao.changeState(orderId, GovermentOrder.STATE_QUITFINANCING,System.currentTimeMillis());
//				order=financingOrders.remove(orderId.toString());
				order.setState(GovermentOrder.STATE_QUITFINANCING);
			}
		}finally
		{
			orderService.releaseFinancingOrder(order);
		}
	}
	private void executeQuitFinancingTask(Product product) throws IllegalConvertException
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
			accountService.unfreezeLenderAccount(freezeCS.getLenderAccountId(),freezeCS.getChiefamount().negate(),freezeCS.getSubmitId(), "解冻");
			
		}
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
				if(i==(submits.size()-1))
				{
					lenderChiefAmount=totalChiefAmount;
				}
				else
					lenderChiefAmount=payBack.getChiefAmount().multiply(submit.getAmount()).divide(product.getRealAmount(), 2, BigDecimal.ROUND_UP);
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
			Integer cashStreamId=accountService.storeChange(payBack.getBorrowerAccountId(),payBack.getId(),totalChiefAmount,totalInterest, "存零");
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		}
	}
	private Borrower createBorrower(String tel)
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
	private Lender createLender(String tel)
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
	private void mockLogin(Object user)
	{
		MockHttpServletRequest request =  new MockHttpServletRequest("POST","/test.do");
		ServletRequestAttributes attributes=new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attributes);
		HttpSession session=request.getSession();
		session.setAttribute(IBorrowerService.SESSION_ATTRIBUTENAME_USER, user);
	}
}
