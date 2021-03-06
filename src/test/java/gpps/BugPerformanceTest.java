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
import gpps.model.ProductSeries;
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
//		createLender(10000);
		all();
//		System.out.println("所有用户的深层校验结果："+accountCheck());
		System.exit(-1);
	}
	public static void all()
	{
		Random random=new Random();
		long currenttime=System.currentTimeMillis();
		int lenderNum=300;
		int buyNum=100;
		try
		{
			List<Lender> lenders=new ArrayList<Lender>();
			for(int i=0;i<lenderNum;i++)
			{
				if(i%100==0)
					System.out.println("创建用户"+i+"个");
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
			financingRequest.setGovermentOrderName("测试性能申请融资订单一");
			
			FinancingRequest financingRequest2 = new FinancingRequest();
			financingRequest2.setApplyFinancingAmount(300000);
			financingRequest2.setGovermentOrderName("测试性能申请融资订单二");
			
			mockLogin(borrower);
			borrowerService.applyFinancing(financingRequest);
			borrowerService.applyFinancing(financingRequest2);
			
			GovermentOrder order=new GovermentOrder();
			order.setBorrowerId(borrower.getId());
			order.setFinancingRequestId(financingRequest.getId());
			order.setTitle("性能测试融资订单1");
			order.setFinancingStarttime(currenttime+24L*3600*1000);
			order.setFinancingEndtime(currenttime+24L*2*3600*1000);
			order.setIncomeStarttime(currenttime+24L*3*3600*1000);
			orderService.create(order);
			Product product=new Product();
			product.setExpectAmount(new BigDecimal(1000*10000));
			product.setGovermentorderId(order.getId());
			product.setIncomeEndtime(currenttime+24L*(3+90)*3600*1000);
			product.setLevelToBuy(0);
			product.setMiniAdd(100);
			product.setMinimum(100);
			product.setProductseriesId(1);
			product.setRate(new BigDecimal(0.12));
			
			productService.create(product);
			borrowerService.passFinancingRequest(financingRequest.getId());
			orderService.startFinancing(order.getId());
			
			GovermentOrder order2=new GovermentOrder();
			order2.setBorrowerId(borrower.getId());
			order2.setFinancingRequestId(financingRequest2.getId());
			order2.setTitle("性能测试融资订单2");
			order2.setFinancingStarttime(currenttime+24L*3600*1000);
			order2.setFinancingEndtime(currenttime+24L*2*3600*1000);
			order2.setIncomeStarttime(currenttime+24L*3*3600*1000);
			orderService.create(order2);
			Product product2=new Product();
			product2.setExpectAmount(new BigDecimal(1000*10000));
			product2.setGovermentorderId(order2.getId());
			product2.setIncomeEndtime(currenttime+24L*(3+90)*3600*1000);
			product2.setLevelToBuy(0);
			product2.setMiniAdd(100);
			product2.setMinimum(100);
			product2.setProductseriesId(1);
			product2.setRate(new BigDecimal(0.16));
			Product product21=new Product();
			product21.setExpectAmount(new BigDecimal(500*10000));
			product21.setGovermentorderId(order2.getId());
			product21.setIncomeEndtime(currenttime+24L*(3+90)*3600*1000);
			product21.setLevelToBuy(0);
			product21.setMiniAdd(100);
			product21.setMinimum(100);
			product21.setProductseriesId(2);
			product21.setRate(new BigDecimal(0.24));
			Product product22=new Product();
			product22.setExpectAmount(new BigDecimal(300*10000));
			product22.setGovermentorderId(order2.getId());
			product22.setIncomeEndtime(currenttime+24L*(3+90)*3600*1000);
			product22.setLevelToBuy(0);
			product22.setMiniAdd(100);
			product22.setMinimum(100);
			product22.setProductseriesId(3);
			product22.setRate(new BigDecimal(0.32));
			
			productService.create(product2);
			productService.create(product21);
			productService.create(product22);
			borrowerService.passFinancingRequest(financingRequest2.getId());
			orderService.startFinancing(order2.getId());
			
			logger.info("开始多线程购买");
			//创建多线程测试购买
			List<FutureTask<List<Long>>> futures=new ArrayList<FutureTask<List<Long>>>();
			for(Lender lender:lenders)
			{
				Random rdm = new Random();
				int rand = rdm.nextInt(100)+1;
				FutureTask<List<Long>> future = new FutureTask<List<Long>>(new BuyThread(rand*buyNum,lender,product, System.currentTimeMillis()));
				new Thread(future).start();
				futures.add(future);
				
				rand = rdm.nextInt(10)+1;
				FutureTask<List<Long>> future2 = new FutureTask<List<Long>>(new BuyThread(rand*buyNum,lender,product2, System.currentTimeMillis()));
				new Thread(future2).start();
				futures.add(future2);
				
				rand = rdm.nextInt(10)+1;
				FutureTask<List<Long>> future21 = new FutureTask<List<Long>>(new BuyThread(rand*buyNum,lender,product21, System.currentTimeMillis()));
				new Thread(future21).start();
				futures.add(future21);
				
				rand = rdm.nextInt(10)+1;
				FutureTask<List<Long>> future22 = new FutureTask<List<Long>>(new BuyThread(rand*buyNum,lender,product22, System.currentTimeMillis()));
				new Thread(future22).start();
				futures.add(future22);
			}
			
			
			long totalCost=0;
			long maxCost = 0;
			long minCost = 0;
			long num=0;
			for(FutureTask<List<Long>> future:futures)
			{
				List<Long> costs=future.get();
				num=num+costs.size();
				for(long cost:costs)
				{
					totalCost=totalCost+cost;
					if(cost>maxCost)
						maxCost=cost;
					if(cost<minCost)
						minCost=cost;
				}
			}
			logger.info("共购买"+num+"次，平均耗时："+totalCost/num+", 最大耗时："+maxCost+", 最小耗时： "+minCost);
			
			/*******************************流标********************************/
			quitFinancing(order.getId());
			/*******************************流标********************************/
			
			/*******************************还款********************************/
			startRepaying(order2.getId());
			
			
			while(true)
			{

			List<PayBack> payBacks = payBackService.findBorrowerCanBeRepayedPayBacks();
			if(payBacks==null || payBacks.isEmpty()){
				break;
			}
			
			mockLogin(borrower);
			for(PayBack pb:payBacks){
				payBackService.repay(pb.getId());
				payBackDao.changeCheckResult(pb.getId(), PayBack.CHECK_SUCCESS);
				checkAndRepay(pb.getId());
				System.out.println("执行"+pb.getProductId()+"产品还款，ID为："+pb.getId());
			}
			}
			
//			for(int i=0;i<payBacks.size();i++)
//			{
//				PayBack payBack=payBacks.get(i);
//				mockLogin(borrower);
//				payBackService.repay(payBack.getId());
//				payBackDao.changeCheckResult(payBack.getId(), PayBack.CHECK_SUCCESS);
//				checkAndRepay(payBack.getId());
//			}
			orderService.closeComplete(order2.getId());
			checkPayBack(product2);
			checkPayBack(product21);
			checkPayBack(product22);
			/*******************************还款********************************/
			
			
			System.out.println("所有用户的深层校验结果："+accountCheck());
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	public static void createLender(int lenderNum)
	{
		long currenttime=System.currentTimeMillis();
		for(int i=0;i<lenderNum;i++)
		{
			if(i%100==0)
				System.out.println("创建用户"+i+"个");
			Lender lender=createLender(""+currenttime+i);
		}
	}
	public static void checkPayBack(Product product) throws CheckException
	{
		product=productDao.find(product.getId());
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
			amount=amount.add(pb.getChiefAmount());
		}
		if(amount.compareTo(product.getRealAmount())!=0)
			throw new CheckException("还款总额与产品不符");
	}
	
	public static String productCheck(){
		return null;
//		checkPayBack(product);
	}
	
	public static String accountCheck()
	{
		StringBuilder sBuilder=new StringBuilder();
		try
		{
			int total=lenderDao.countAll();
			int DEFAULT_RECNUM=100;
			for(int i=0;i<(total/DEFAULT_RECNUM+(total%DEFAULT_RECNUM==0?0:1));i++)
			{
				List<Lender> lenders=lenderDao.findAll(i*DEFAULT_RECNUM, DEFAULT_RECNUM);
				if(lenders==null||lenders.size()==0)
					continue;
				for(Lender lender:lenders)
				{
//					if(StringUtil.isEmpty(lender.getThirdPartyAccount()))
//						continue;
					//与第三方验证
					//网贷平台子账户可用余额|总可用余额(子账户可用余额+公共账户可用余额)|子账户冻结余额”（例:100.00|200.00|10.00）
//					String text=thirdPaySupportService.balanceQuery(lender.getThirdPartyAccount());
//					if(StringUtil.isEmpty(text))
//					{
//						appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), "从第三方支付平台获取账户信息失败.");
//						continue;
//					}
					
					try{
						Thread.sleep(10);
					}catch(Exception e){
						
					}
					
					LenderAccount account=lenderAccountDao.find(lender.getAccountId());
//					String[] thirdAccount=text.split("\\|");
//					if(!compareAccount(thirdAccount[0], account.getUsable())||!compareAccount(thirdAccount[2], account.getFreeze()))
//					{
//						appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), 
//								"本地账户与第三方支付平台不符,本地可用|冻结金额为"+account.getUsable().toString()+"|"+account.getFreeze().toString()+";"
//								+"第三方可用|冻结金额为"+thirdAccount[0]+"|"+thirdAccount[2]);
//					}
					//验证：总金额=可用金额+冻结金额+已投资金额
					if(account.getFreeze().add(account.getUsable()).add(account.getUsed()).compareTo(account.getTotal())!=0)
					{
						appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), "账户金额错误,总金额不等于可用金额+冻结金额+已投资金额");
					}
					//可用金额=所有现金流之和
					CashStreamSum sum=cashStreamDao.sumCashStream(lender.getAccountId(), null, null);
					sum=(sum==null)?new CashStreamSum():sum;
					if(account.getUsable().compareTo(sum.getChiefAmount().add(sum.getInterest()))!=0)
					{
						appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), "可用金额与现金流验证错误,可用金额:"+account.getUsable().toString()+",现金流:"+sum);
					}
					//冻结金额=冻结+解冻
					List<Integer> actions=new ArrayList<Integer>();
					actions.add(CashStream.ACTION_FREEZE);
					actions.add(CashStream.ACTION_UNFREEZE);
					sum=cashStreamDao.sumCashStream(lender.getAccountId(), null, actions);
					sum=(sum==null)?new CashStreamSum():sum;
					if(account.getFreeze().negate().compareTo(sum.getChiefAmount().add(sum.getInterest()))!=0)
					{
						appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), "冻结金额与现金流验证错误,冻结金额:"+account.getFreeze().toString()+",现金流:"+sum);
					}
					//已投资金额=购买+回款（本金）
					actions=new ArrayList<Integer>();
					actions.add(CashStream.ACTION_PAY);
					actions.add(CashStream.ACTION_REPAY);
					sum=cashStreamDao.sumCashStream(lender.getAccountId(), null, actions);
					sum=(sum==null)?new CashStreamSum():sum;
					if(account.getUsed().negate().compareTo(sum.getChiefAmount())!=0)
					{
						appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), "已用金额与现金流验证错误,已用金额:"+account.getFreeze().toString()+",现金流:"+sum.getChiefAmount());
					}
					//利息=回款（利息）
					actions=new ArrayList<Integer>();
					actions.add(CashStream.ACTION_REPAY);
					sum=cashStreamDao.sumCashStream(lender.getAccountId(), null, actions);
					sum=(sum==null)?new CashStreamSum():sum;
					if(account.getTotalincome().compareTo(sum.getInterest())!=0)
					{
						appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), "已收益金额与现金流验证错误,已收益金额:"+account.getTotalincome().toString()+",现金流:"+sum.getInterest());
					}
				}
			}
			
			total=borrowerDao.countAll();
			for(int i=0;i<(total/DEFAULT_RECNUM+(total%DEFAULT_RECNUM==0?0:1));i++)
			{
				List<Borrower> borrowers=borrowerDao.findAll(i*DEFAULT_RECNUM, DEFAULT_RECNUM);
				if(borrowers==null||borrowers.size()==0)
					continue;
				for(Borrower borrower:borrowers)
				{
//					if(StringUtil.isEmpty(borrower.getThirdPartyAccount()))
//						continue;
					//与第三方验证
					//网贷平台子账户可用余额|总可用余额(子账户可用余额+公共账户可用余额)|子账户冻结余额”（例:100.00|200.00|10.00）
//					String text=thirdPaySupportService.balanceQuery(borrower.getThirdPartyAccount());
//					if(StringUtil.isEmpty(text))
//					{
//						appendMsg(sBuilder, Lender.class, borrower.getId(), borrower.getThirdPartyAccount(), "从第三方支付平台获取账户信息失败.");
//						continue;
//					}
					BorrowerAccount account=borrowerAccountDao.find(borrower.getAccountId());
//					String[] thirdAccount=text.split("\\|");
//					if(!compareAccount(thirdAccount[0], account.getUsable())||!compareAccount(thirdAccount[2], account.getFreeze()))
//					{
//						appendMsg(sBuilder, Borrower.class, borrower.getId(), borrower.getThirdPartyAccount(), 
//								"本地账户与第三方支付平台不符,本地可用|冻结金额为"+account.getUsable().toString()+"|"+account.getFreeze().toString()+";"
//								+"第三方可用|冻结金额为"+thirdAccount[0]+"|"+thirdAccount[2]);
//					}
					//验证：总金额=可用金额+冻结金额
					if(account.getFreeze().add(account.getUsable()).compareTo(account.getTotal())!=0)
					{
						appendMsg(sBuilder, Borrower.class, borrower.getId(), borrower.getThirdPartyAccount(), "账户金额错误,总金额不等于可用金额+冻结金额");
					}
					//可用金额=(充值、取现、冻结、解冻)现金流之和+（支付、还款、存零）现金流之和取反
					List<Integer> actions=new ArrayList<Integer>();
					actions.add(CashStream.ACTION_FREEZE);
					actions.add(CashStream.ACTION_UNFREEZE);
					actions.add(CashStream.ACTION_CASH);
					actions.add(CashStream.ACTION_RECHARGE);
					CashStreamSum sum=cashStreamDao.sumCashStream(null, borrower.getAccountId(), actions);
					sum=(sum==null)?new CashStreamSum():sum;
					actions=new ArrayList<Integer>();
					actions.add(CashStream.ACTION_PAY);
					actions.add(CashStream.ACTION_REPAY);
					actions.add(CashStream.ACTION_STORECHANGE);
					CashStreamSum sum2=cashStreamDao.sumCashStream(null, borrower.getAccountId(), actions);
					sum2=(sum2==null)?new CashStreamSum():sum2;
					sum.setChiefAmount(sum.getChiefAmount().add(sum2.getChiefAmount().negate()));
					sum.setInterest(sum.getInterest().add(sum2.getInterest().negate()));
					if(account.getUsable().compareTo(sum.getChiefAmount().add(sum.getInterest()))!=0)
					{
						appendMsg(sBuilder, Borrower.class, borrower.getId(), borrower.getThirdPartyAccount(), "可用金额与现金流验证错误,可用金额:"+account.getUsable().toString()+",现金流:"+sum);
					}
					
					//冻结金额=冻结+解冻
					actions=new ArrayList<Integer>();
					actions.add(CashStream.ACTION_FREEZE);
					actions.add(CashStream.ACTION_UNFREEZE);
					sum=cashStreamDao.sumCashStream(null, borrower.getAccountId(), actions);
					sum=(sum==null)?new CashStreamSum():sum;
					if(account.getFreeze().negate().compareTo(sum.getChiefAmount().add(sum.getInterest()))!=0)
					{
						appendMsg(sBuilder, Borrower.class, borrower.getId(), borrower.getThirdPartyAccount(), "冻结金额与现金流验证错误,冻结金额:"+account.getFreeze().toString()+",现金流:"+sum);
					}
				}
			}
		}catch(Throwable e)
		{
			logger.error(e.getMessage(),e);
			sBuilder.append(e.getMessage());
		}
		return sBuilder.toString();
	}
	private static void appendMsg(StringBuilder sBuilder,Class cls,Integer id,String thirdPartyAccount,String msg)
	{
		sBuilder.append(cls.getSimpleName()).append("[").append("id:").append(id).append(",")
		.append("thirdPartyAccount:").append(thirdPartyAccount).append("]").append(" ").append(msg).append("\r\n");
	}
	public static void quitFinancing(Integer orderId) throws IllegalConvertException, IllegalOperationException, ExistWaitforPaySubmitException, CheckException {
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
	public static void executeQuitFinancingTask(Product product) throws IllegalConvertException
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
	static class BuyThread implements Callable<List<Long>>
	{
		int buyNum;
		Lender lender;
		Product product;
		long createtime;
		
		public BuyThread(int buyNum,Lender lender,Product product, long createtime)
		{
			this.buyNum=buyNum;
			this.lender=lender;
			this.product=product;
			this.createtime = createtime;
		}
		@Override
		public List<Long> call() throws Exception
		{
			List<Long> costs=new ArrayList<Long>(buyNum);
			//购买
			try
			{
				long currenttime=System.currentTimeMillis();
//				for(int i=0;i<buyNum;i++)
				for(int i=0;i<1;i++)
				{
					try{
					mockLogin(lender);
					Integer submitId=submitService.buy(product.getId(), buyNum);
					Integer cashStreamId = accountService.freezeLenderAccount(lender.getAccountId(), new BigDecimal(buyNum), submitId, "购买");
					submitService.confirmBuy(submitId);
		//			cashStreamDao.updateLoanNo(cashStreamId, loanNo,null);
					accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
					}catch(Exception e){
						logger.error(e.getMessage());
					}
//					long cost=System.currentTimeMillis()-currenttime;
					long cost=System.currentTimeMillis()-createtime;
					long executetime = System.currentTimeMillis()-currenttime;
					logger.info("lender,id="+lender.getId()+"执行第"+i+"次购买，调度耗时:"+cost+"ms"+", 本次执行耗时："+executetime+", 购买金额："+buyNum);
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
	public static void startRepaying(Integer orderId) throws IllegalConvertException,IllegalOperationException, ExistWaitforPaySubmitException, CheckException {
		GovermentOrder order=null;
		try
		{
			order=orderService.applyFinancingOrder(orderId);
			if(order!=null)
			{
				Borrower borrower=borrowerDao.find(order.getBorrowerId());
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
						//重新计算payback
						//删除
						payBackDao.deleteByProduct(product.getId());
						// 创建还款计划
						ProductSeries productSeries=productSeriesDao.find(product.getProductseriesId());
						List<PayBack> payBacks=payBackService.generatePayBacks(product.getRealAmount().intValue(), product.getRate().doubleValue(),productSeries.getType(), order.getIncomeStarttime(), product.getIncomeEndtime());
						for(PayBack payBack:payBacks)
						{
							payBack.setBorrowerAccountId(borrower.getAccountId());
							payBack.setProductId(product.getId());
							payBackDao.create(payBack);
						}
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
	public static void executePayTask(Product product) throws IllegalConvertException
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
	public static void checkAndRepay(Integer payBackId) throws Exception
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
	public static void executeRepayTask(PayBack payBack) throws Exception
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
	private static Borrower createBorrower(String tel)
	{
		try {
			MockHttpServletRequest request =  new MockHttpServletRequest("POST","/test.do");
			ServletRequestAttributes attributes=new ServletRequestAttributes(request);
			RequestContextHolder.setRequestAttributes(attributes);
			HttpSession session=borrowerService.getCurrentSession();
			borrowerService.sendMessageValidateCode("13322233333");
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
			lenderService.sendMessageValidateCode("13211177889");
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
