package gpps.service;

import java.math.BigDecimal;
import java.util.List;

import gpps.TestSupport;
import gpps.dao.IBorrowerAccountDao;
import gpps.dao.IBorrowerDao;
import gpps.dao.ICashStreamDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.ILenderAccountDao;
import gpps.dao.ILenderDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.dao.ISubmitDao;
import gpps.model.Borrower;
import gpps.model.CashStream;
import gpps.model.GovermentOrder;
import gpps.model.Lender;
import gpps.model.LenderAccount;
import gpps.model.Product;
import gpps.model.ProductSeries;
import gpps.model.Submit;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.IllegalOperationException;
import gpps.service.exception.InsufficientBalanceException;
import gpps.service.exception.InsufficientProductException;
import gpps.service.exception.ProductSoldOutException;
import gpps.service.exception.UnreachBuyLevelException;
import gpps.service.impl.AccountServiceImpl;

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

	@BeforeClass
	public static void setUpBeforeClass() {
		// System.out.println("打印服务");
		// for(String name:context.getBeanDefinitionNames())
		// {
		// System.out.println(name);
		// }
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
	}
	@Test
	public void buyProductProcess(){
		int productExpectAmount=15*10000;
		//创建贷款人
		long start=System.currentTimeMillis();
		Lender lender_a=createLender("lender_a_"+start,"1"+start);
//		Lender lender_a=createLender("calis123","1"+start);
		Lender lender_b=createLender("lender_b_"+start,"2"+start);
		//充值
		try {
			accountService.changeCashStreamState(accountService.rechargeLenderAccount(lender_a.getAccountId(), new BigDecimal(10*10000+0.25), "充值"),CashStream.STATE_SUCCESS);
			accountService.changeCashStreamState(accountService.rechargeLenderAccount(lender_b.getAccountId(), new BigDecimal(20*10000), "充值"), CashStream.STATE_SUCCESS);
		} catch (IllegalConvertException e) {
			Assert.fail(e.getMessage());
		}
		//创建借款人及审核通过
		Borrower borrower=createBorrower("borrower_"+start,"9"+start);
		try {
			mockLogin(borrower);
			borrowerService.applyForFunding();
			borrowerService.passFundingApplying(borrower.getId());
		} catch (IllegalConvertException e) {
			Assert.fail(e.getMessage());
		}
		//创建订单
		GovermentOrder order=new GovermentOrder();
		order.setBorrowerId(borrower.getId());
		order.setTitle("淘宝借钱二期");
		order.setFinancingStarttime(System.currentTimeMillis());
		order.setFinancingEndtime(System.currentTimeMillis()+7L*24*3600*1000);
		order.setIncomeStarttime(System.currentTimeMillis()+7L*24*3600*1000);
//		order.setIncomeEndtime(System.currentTimeMillis()+107L*24*3600*1000);
		order=orderService.create(order);
		//订单审核通过
//		try {
//			orderService.passApplying(order.getId());
//		} catch (IllegalConvertException e) {
//			Assert.fail(e.getMessage());
//		}
		//创建产品
		ProductSeries productSeries=new ProductSeries();
		productSeries.setTitle("高利息高风险");
		productSeriesDao.create(productSeries);
		
		Product product=new Product();
		product.setExpectAmount(new BigDecimal(productExpectAmount));
		product.setProductseriesId(productSeries.getId());
		product.setGovermentorderId(order.getId());
		product.setLevelToBuy(Lender.LEVEL_VIP1);
		product.setPaybackmodel(0);
		product.setRate(new BigDecimal(0.15));
		productService.create(product);
		//开始融资
		try {
			orderService.startFinancing(order.getId());
		} catch (IllegalConvertException e) {
			Assert.fail(e.getMessage());
		}
		//测试购买异常情况
		for(int i=0;i<3;i++)
		{
			try {
				if(i==0)
				{
					mockLogin(lender_a);
					//a用户余额不足
					submitService.buy(product.getId(), 12*10000);
				}else if(i==1){
					mockLogin(lender_b);
					//b用户权限不够
					submitService.buy(product.getId(), 12*10000);
				}else if(i==2){
					lenderService.changeLevel(lender_b.getId(), Lender.LEVEL_VIP1);
					lender_b.setLevel(Lender.LEVEL_VIP1);
					mockLogin(lender_b);
					//产品余额不足
					submitService.buy(product.getId(), 20*10000);
				}
				Assert.fail();
			}catch (Exception e) {
				switch (i) {
				case 0:
					Assert.assertTrue(e instanceof InsufficientBalanceException);
					break;
				case 1:
					Assert.assertTrue(e instanceof UnreachBuyLevelException);
					break;
				case 2:
					Assert.assertTrue(e instanceof InsufficientProductException);
					break;
				default:
					Assert.fail(e.getMessage());
					break;
				}
			}
		}
		//正常购买
		try {
			mockLogin(lender_b);
			submitService.buy(product.getId(), 10*10000);
			
			productService.changeBuyLevel(product.getId(), Lender.LEVEL_COMMON);
			
			mockLogin(lender_a);
			submitService.buy(product.getId(), 4*10000);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		product=orderService.applyFinancingProduct(product.getId(), product.getGovermentorderId());
		Assert.assertEquals(14*10000,product.getRealAmount().doubleValue(),0);
		orderService.releaseFinancingProduct(product);
		product=productDao.find(product.getId());
		Assert.assertEquals(14*10000,product.getRealAmount().doubleValue(),0);
		
		try {
			orderService.startRepaying(order.getId());
		} catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalOperationException);
		}
		try {
			productService.startRepaying(product.getId());
		} catch (IllegalConvertException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertNull(orderService.applyFinancingProduct(product.getId(), product.getGovermentorderId()));
		
		try {
			//售完购买
			mockLogin(lender_b);
			submitService.buy(product.getId(), 10*10000);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ProductSoldOutException);
		} 
		
		try {
			orderService.startRepaying(order.getId());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertNull(orderService.applyFinancingOrder(order.getId()));
		//a原有100000.25,购买4w
		LenderAccount lenderAccount_a=lenderAccountDao.find(lender_a.getAccountId());
//		Assert.assertEquals(60000.25, lenderAccount_a.getUsable().doubleValue(),0);
//		Assert.assertEquals(40000, lenderAccount_a.getUsed().doubleValue(),0);
//		Assert.assertEquals(100000.25, lenderAccount_a.getTotal().doubleValue(),0);
//		Assert.assertEquals(0, lenderAccount_a.getFreeze().doubleValue(),0);
		
		LenderAccount lenderAccount_b=lenderAccountDao.find(lender_b.getAccountId());
		
		
		//清理数据
//		cashStreamDao.deleteByLenderAccountId(lender_a.getAccountId());
//		cashStreamDao.deleteByLenderAccountId(lender_b.getAccountId());
//		cashStreamDao.deleteByBorrowerAccountId(borrower.getAccountId());
//		
//		List<Submit> submits=submitDao.findAllByProduct(product.getId());
//		for(Submit submit:submits)
//		{
//			submitDao.delete(submit.getId());
//		}
//		productDao.delete(product.getId());
//		productSeriesDao.delete(productSeries.getId());
//		orderDao.delete(order.getId());
//		
//		lenderDao.delete(lender_a.getId());
//		lenderAccountDao.delete(lender_a.getAccountId());
//		lenderDao.delete(lender_b.getId());
//		lenderAccountDao.delete(lender_b.getAccountId());
//		
//		borrowerDao.delete(borrower.getId());
//		borrowerAccountDao.delete(borrower.getAccountId());
		
	}
	private Borrower createBorrower(String loginId,String tel)
	{
		try {
			MockHttpServletRequest request =  new MockHttpServletRequest("POST","/test.do");
			ServletRequestAttributes attributes=new ServletRequestAttributes(request);
			RequestContextHolder.setRequestAttributes(attributes);
			HttpSession session=borrowerService.getCurrentSession();
			borrowerService.sendMessageValidateCode();
			Borrower borrower=new Borrower();
			borrower.setEmail("test@calis.edu.cn");
			borrower.setIdentityCard("231550215402021533");
			borrower.setLoginId(loginId);
			borrower.setName("测试");
			borrower.setPassword("123123");
			borrower.setTel(tel);
			return borrowerService.register(borrower, (String)session.getAttribute(ILenderService.SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODE));
		} catch (Throwable e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return null;
	}
	private Lender createLender(String loginId,String tel)
	{
		try {
			MockHttpServletRequest request =  new MockHttpServletRequest("POST","/test.do");
			ServletRequestAttributes attributes=new ServletRequestAttributes(request);
			RequestContextHolder.setRequestAttributes(attributes);
			HttpSession session=lenderService.getCurrentSession();
			lenderService.sendMessageValidateCode();
			Lender lender=new Lender();
			lender.setEmail("test@calis.edu.cn");
			lender.setIdentityCard("231550215402021533");
			lender.setLoginId(loginId);
			lender.setName("测试");
			lender.setPassword("123123");
			lender.setTel(tel);
			return lenderService.register(lender, (String)session.getAttribute(ILenderService.SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODE));
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
