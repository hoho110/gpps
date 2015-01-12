package gpps.service;

import java.util.HashMap;
import java.util.Map;

import gpps.service.message.IMessageService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class MessageSendTest {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	protected static IMessageService messageService = context.getBean(IMessageService.class);
	public static void main(String args[]) throws Exception{
		cashout_lender();
		cashout_borrower();
		validateCode_lender();
		validateCode_borrower();
		financingSuccess_Lender();
		financingSuccess_Borrower();
		financingFail_Lender();
		financingFail_Borrower();
		
		payback_Lender();
		payback_Borrower();
		lpayback_Lender();
		lpayback_Borrower();
		toAfford_Lender();
		investSuccess_Borrower();
		investFail_Borrower();
		investSuccess_Request();
		investFail_Request();
		startFinance_Borrower();
		remindPayback_Borrower();
		System.exit(0);
	}
	
	public static void cashout_lender() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_AMOUNT, "120000");
		param.put(IMessageService.PARAM_FEE, "300");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_CASHOUTSUCCESS, IMessageService.USERTYPE_LENDER, 6000, param);
	}
	
	public static void cashout_borrower() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_AMOUNT, "400000");
		param.put(IMessageService.PARAM_FEE, "1000");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_CASHOUTSUCCESS, IMessageService.USERTYPE_BORROWER, 6000, param);
	}
	
	public static void validateCode_lender() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_PHONE, "13811179462");
		param.put(IMessageService.PARAM_VALIDATE_CODE, "12321");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_SENDVALIDATECODE, IMessageService.USERTYPE_LENDER, 6000, param);
	}
	
	public static void validateCode_borrower() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_PHONE, "13426329462");
		param.put(IMessageService.PARAM_VALIDATE_CODE, "12321");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_SENDVALIDATECODE, IMessageService.USERTYPE_BORROWER, 6000, param);
	}
	
	public static void financingSuccess_Lender() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		param.put(IMessageService.PARAM_PRODUCT_SERIES_NAME, "均衡型");
		param.put(IMessageService.PARAM_AMOUNT, "20000");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_FINANCINGSUCCESS, IMessageService.USERTYPE_LENDER, 6000, param);
	}
	
	public static void financingSuccess_Borrower() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_FINANCINGSUCCESS, IMessageService.USERTYPE_BORROWER, 6000, param);
	}
	
	public static void financingFail_Lender() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		param.put(IMessageService.PARAM_PRODUCT_SERIES_NAME, "均衡型");
		param.put(IMessageService.PARAM_AMOUNT, "20000");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_FINANCINGFAIL, IMessageService.USERTYPE_LENDER, 6000, param);
	}
	
	public static void financingFail_Borrower() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_FINANCINGFAIL, IMessageService.USERTYPE_BORROWER, 6000, param);
	}
	
	
	
	public static void payback_Lender() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		param.put(IMessageService.PARAM_PRODUCT_SERIES_NAME, "均衡型");
		param.put(IMessageService.PARAM_AMOUNT, "20000");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_PAYBACKSUCCESS, IMessageService.USERTYPE_LENDER, 6000, param);
	}
	
	public static void payback_Borrower() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		param.put(IMessageService.PARAM_PRODUCT_SERIES_NAME, "均衡型");
		param.put(IMessageService.PARAM_AMOUNT, "120000");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_PAYBACKSUCCESS, IMessageService.USERTYPE_BORROWER, 6000, param);
	}
	
	
	public static void lpayback_Lender() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		param.put(IMessageService.PARAM_PRODUCT_SERIES_NAME, "均衡型");
		param.put(IMessageService.PARAM_AMOUNT, "20000");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_LASTPAYBACKSUCCESS, IMessageService.USERTYPE_LENDER, 6000, param);
	}
	
	public static void lpayback_Borrower() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		param.put(IMessageService.PARAM_PRODUCT_SERIES_NAME, "均衡型");
		param.put(IMessageService.PARAM_AMOUNT, "120000");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_LASTPAYBACKSUCCESS, IMessageService.USERTYPE_BORROWER, 6000, param);
	}
	
	public static void toAfford_Lender() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		param.put(IMessageService.PARAM_PRODUCT_SERIES_NAME, "均衡型");
		param.put(IMessageService.PARAM_AMOUNT, "20000");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_TOAFFORD, IMessageService.USERTYPE_LENDER, 6000, param);
	}
	
	
	public static void investSuccess_Borrower() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_COMPANYINVESTSUCCESS, IMessageService.USERTYPE_BORROWER, 6000, param);
	}
	
	public static void investFail_Borrower() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_COMPANYINVESTFAIL, IMessageService.USERTYPE_BORROWER, 6000, param);
	}
	
	
	public static void investSuccess_Request() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_REQUESTINVESTSUCCESS, IMessageService.USERTYPE_BORROWER, 6000, param);
	}
	
	public static void investFail_Request() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_REQUESTINVESTFAIL, IMessageService.USERTYPE_BORROWER, 6000, param);
	}
	
	
	public static void startFinance_Borrower() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_STARTFINANCE, IMessageService.USERTYPE_BORROWER, 6000, param);
	}
	
	
	
	public static void remindPayback_Borrower() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put(IMessageService.PARAM_ORDER_NAME, "测试订单一");
		param.put(IMessageService.PARAM_PRODUCT_SERIES_NAME, "均衡型");
		param.put(IMessageService.PARAM_AMOUNT, "20000");
		messageService.sendMessage(IMessageService.MESSAGE_TYPE_REMIND_PAYBACK, IMessageService.USERTYPE_BORROWER, 6000, param);
	}
}
