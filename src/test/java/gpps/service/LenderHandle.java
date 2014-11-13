//package gpps.service;
//
//import gpps.dao.ICashStreamDao;
//import gpps.model.CashStream;
//import gpps.model.Lender;
//import gpps.model.Submit;
//import gpps.service.exception.IllegalConvertException;
//import gpps.service.exception.InsufficientBalanceException;
//import gpps.service.exception.InsufficientProductException;
//import gpps.service.exception.ProductSoldOutException;
//import gpps.service.exception.UnreachBuyLevelException;
//
//import java.math.BigDecimal;
//
//import javax.servlet.http.HttpSession;
//
//import org.junit.Assert;
//import org.springframework.context.ApplicationContext;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//public class LenderHandle {
//	//创建用户
//	public static Lender createLender(ApplicationContext context,String loginId,String password,String tel)
//	{
//		ILenderService lenderService=context.getBean(ILenderService.class);
//		try {
//			MockHttpServletRequest request =  new MockHttpServletRequest("POST","/test.do");
//			ServletRequestAttributes attributes=new ServletRequestAttributes(request);
//			RequestContextHolder.setRequestAttributes(attributes);
//			HttpSession session=lenderService.getCurrentSession();
//			lenderService.sendMessageValidateCode();
//			Lender lender=new Lender();
//			lender.setEmail("test@calis.edu.cn");
//			lender.setIdentityCard("231550215402021533");
//			lender.setLoginId(loginId);
//			lender.setName("测试用户");
//			lender.setPassword(password);
//			lender.setTel(tel);
//			return lenderService.register(lender, (String)session.getAttribute(ILenderService.SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODE));
//		} catch (Throwable e) {
//			e.printStackTrace();
//			Assert.fail(e.getMessage());
//		}
//		return null;
//	}
//	//充值
//	public static void recharge(ApplicationContext context,Lender lender,int amount)
//	{
//		IAccountService accountService=context.getBean(IAccountService.class);
//		try {
//			accountService.changeCashStreamState(accountService.rechargeLenderAccount(lender.getAccountId(), new BigDecimal(amount), "充值"),CashStream.STATE_SUCCESS);
//		} catch (IllegalConvertException e) {
//			e.printStackTrace();
//		}
//	}
//	//修改用户级别
//	public static void changeLenderLevel(ApplicationContext context,Lender lender,int level)
//	{
//		ILenderService lenderService=context.getBean(ILenderService.class);
//		lenderService.changeLevel(lender.getId(), level);
//	}
//	//购买
//	public static Integer buy(ApplicationContext context,Lender lender,Integer productId,int amount)
//	{
//		ISubmitService submitService=context.getBean(ISubmitService.class);
//		mockLogin(lender);
//		try {
//			return submitService.buy(productId, amount);
//		} catch (InsufficientBalanceException e) {
//			e.printStackTrace();
//		} catch (ProductSoldOutException e) {
//			e.printStackTrace();
//		} catch (InsufficientProductException e) {
//			e.printStackTrace();
//		} catch (UnreachBuyLevelException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	//支付
//	public static void pay(ApplicationContext context,Lender lender,Integer submitId)
//	{
//		ISubmitService submitService=context.getBean(ISubmitService.class);
//		IAccountService accountService=context.getBean(IAccountService.class);
//		ICashStreamDao cashStreamDao=context.getBean(ICashStreamDao.class);
//		Submit submit=submitService.find(submitId);
//		try {
//			Integer cashStreamId=accountService.freezeLenderAccount(lender.getAccountId(), submit.getAmount(), submitId, "购买");
//			
//			//下面是第三方返回成功
//			CashStream cashStream=cashStreamDao.find(cashStreamId);
//			submitService.confirmBuy(cashStream.getSubmitId());
//			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
//			
//			//第三方返回失败
////			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_FAIL);
//		} catch (InsufficientBalanceException e) {
//			e.printStackTrace();
//		} catch (IllegalConvertException e) {
//			e.printStackTrace();
//		}
//	}
//	public static void failPay(ApplicationContext context,Lender lender,Integer submitId)
//	{
//		ISubmitService submitService=context.getBean(ISubmitService.class);
//		IAccountService accountService=context.getBean(IAccountService.class);
//		ICashStreamDao cashStreamDao=context.getBean(ICashStreamDao.class);
//		Submit submit=submitService.find(submitId);
//		try {
//			Integer cashStreamId=accountService.freezeLenderAccount(lender.getAccountId(), submit.getAmount(), submitId, "购买");
//			
//			//下面是第三方返回成功
////			CashStream cashStream=cashStreamDao.find(cashStreamId);
////			submitService.confirmBuy(cashStream.getSubmitId());
////			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
//			
//			//第三方返回失败
//			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_FAIL);
//		} catch (InsufficientBalanceException e) {
//			e.printStackTrace();
//		} catch (IllegalConvertException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	//模拟登录
//	private static void mockLogin(Object user)
//	{
//		MockHttpServletRequest request =  new MockHttpServletRequest("POST","/test.do");
//		ServletRequestAttributes attributes=new ServletRequestAttributes(request);
//		RequestContextHolder.setRequestAttributes(attributes);
//		HttpSession session=request.getSession();
//		session.setAttribute(IBorrowerService.SESSION_ATTRIBUTENAME_USER, user);
//	}
//}
