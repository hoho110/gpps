package gpps.service;

import java.math.BigDecimal;

import gpps.dao.IBorrowerAccountDao;
import gpps.dao.IBorrowerDao;
import gpps.dao.ICashStreamDao;
import gpps.model.Borrower;
import gpps.model.BorrowerAccount;
import gpps.model.CashStream;
import gpps.model.Lender;
import gpps.model.PayBack;
import gpps.model.Task;
import gpps.service.exception.ExistWaitforPaySubmitException;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.IllegalOperationException;
import gpps.service.exception.InsufficientBalanceException;

import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class BorrowerHandle {
	//创建借款人
	public static Borrower create(ApplicationContext context){
		IBorrowerDao borrowerDao = context.getBean(IBorrowerDao.class);
		IBorrowerAccountDao accountDao = context.getBean(IBorrowerAccountDao.class);
		
		BorrowerAccount account = new BorrowerAccount();
		accountDao.create(account);
		
		Borrower borrower=new Borrower();
		borrower.setEmail("test@calis.edu.cn");
		borrower.setIdentityCard("231550215402021533");
		borrower.setLoginId("test");
		borrower.setCompanyName("河北政采有限公司");
		borrower.setName("张三");
		borrower.setPassword("123123");
		borrower.setTel("1333333");
		borrower.setCreditValue(10000);
		borrower.setPrivilege(12);
		borrower.setAccountId(account.getId());
		borrowerDao.create(borrower);
		return borrower;
	}
	//充值
	public static void recharge(ApplicationContext context,Borrower borrower,int amount)
	{
		IAccountService accountService=context.getBean(IAccountService.class);
		try {
			accountService.changeCashStreamState(accountService.rechargeBorrowerAccount(borrower.getAccountId(), new BigDecimal(amount), "充值"),CashStream.STATE_SUCCESS);
		} catch (IllegalConvertException e) {
			e.printStackTrace();
		}
	}
	//产品开始还款
	public static void startProductRepay(ApplicationContext context,Integer productId)
	{
		IProductService productService = context.getBean(IProductService.class);
		try {
			productService.startRepaying(productId);
		} catch (IllegalConvertException e) {
			e.printStackTrace();
		} catch (ExistWaitforPaySubmitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//产品流标
	public static void quitProductFinancing(ApplicationContext context,Integer productId)
	{
		IProductService productService = context.getBean(IProductService.class);
		try {
			productService.quitFinancing(productId);
		} catch (IllegalConvertException e) {
			e.printStackTrace();
		}
	}
	public static void quitOrder(ApplicationContext context,Integer orderId)
	{
		IGovermentOrderService orderService=context.getBean(IGovermentOrderService.class);
		try {
			orderService.quitFinancing(orderId);
		} catch (IllegalConvertException e) {
			e.printStackTrace();
		} catch (IllegalOperationException e) {
			e.printStackTrace();
		}
	}
	//订单开始还款
	public static void startOrderRepay(ApplicationContext context,Integer orderId)
	{
		IGovermentOrderService orderService=context.getBean(IGovermentOrderService.class);
		try {
			orderService.startRepaying(orderId);
		} catch (IllegalConvertException e) {
			e.printStackTrace();
		} catch (IllegalOperationException e) {
			e.printStackTrace();
		}
	}
	//订单开始融资
	public static void startOrderFinancing(ApplicationContext context,Integer orderId)
	{
		IGovermentOrderService orderService=context.getBean(IGovermentOrderService.class);
		try {
			orderService.startFinancing(orderId);
		} catch (IllegalConvertException e1) {
			e1.printStackTrace();
		}
	}
	//一次还款
	public static void repay(ApplicationContext context,Integer paybackId)
	{
		IPayBackService payBackService=context.getBean(IPayBackService.class);
		IAccountService accountService=context.getBean(IAccountService.class);
		ICashStreamDao cashStreamDao=context.getBean(ICashStreamDao.class);
		ITaskService taskService=context.getBean(ITaskService.class);
		PayBack payBack=payBackService.find(paybackId);
		try {
			Integer cashStreamId = accountService.freezeBorrowerAccount(payBack.getBorrowerAccountId(), payBack.getChiefAmount().add(payBack.getInterest()), payBack.getId(), "还款");
			CashStream cashStream=cashStreamDao.find(cashStreamId);
			Task task=new Task();
			task.setCreateTime(System.currentTimeMillis());
			task.setPayBackId(cashStream.getPaybackId());
			task.setProductId(payBack.getProductId());
			task.setState(Task.STATE_INIT);
			task.setType(Task.TYPE_REPAY);
			taskService.submit(task);
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
			payBackService.changeState(cashStream.getPaybackId(), PayBack.STATE_FINISHREPAY);
		} catch (InsufficientBalanceException e) {
			e.printStackTrace();
		} catch (IllegalConvertException e) {
			e.printStackTrace();
		}
	}
	// 模拟登录
	private static void mockLogin(Object user) {
		MockHttpServletRequest request = new MockHttpServletRequest("POST",
				"/test.do");
		ServletRequestAttributes attributes = new ServletRequestAttributes(
				request);
		RequestContextHolder.setRequestAttributes(attributes);
		HttpSession session = request.getSession();
		session.setAttribute(IBorrowerService.SESSION_ATTRIBUTENAME_USER, user);
	}
}
