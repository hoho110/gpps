package gpps.service;

import gpps.dao.ICashStreamDao;
import gpps.model.CashStream;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.IllegalOperationException;
import gpps.service.thirdpay.IHttpClientService;
import gpps.service.thirdpay.ResultCodeException;
import gpps.tools.RsaHelper;
import gpps.tools.StringUtil;

import java.math.BigDecimal;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.google.gson.Gson;

public class CheckSingleCashStream {
	
	public static final String ACTION_REGISTACCOUNT="0";
	public static final String ACTION_RECHARGE="1";
	public static final String ACTION_TRANSFER="2";
	public static final String ACTION_CHECK="3";
	public static final String ACTION_CARDBINDING="4";
	public static final String ACTION_CASH="5";
	public static final String ACTION_AUTHORIZE="6";
	public static final String ACTION_ORDERQUERY="7";
	public static final String ACTION_BALANCEQUERY="8";
	private static Map<String, String> urls=new HashMap<String, String>();
	static {
		urls.put(ACTION_REGISTACCOUNT, "/loan/toloanregisterbind.action");
		urls.put(ACTION_RECHARGE, "/loan/toloanrecharge.action");
		urls.put(ACTION_TRANSFER, "/loan/loan.action");
		urls.put(ACTION_CHECK, "/loan/toloantransferaudit.action");
		urls.put(ACTION_CARDBINDING, "/loan/toloanfastpay.action");
		urls.put(ACTION_CASH, "/loan/toloanwithdraws.action");
		urls.put(ACTION_AUTHORIZE, "/loan/toloanauthorize.action");
		urls.put(ACTION_ORDERQUERY, "/loan/loanorderquery.action");
		urls.put(ACTION_BALANCEQUERY, "/loan/balancequery.action");
	}
	private static String url="http://218.4.234.150:88/main";
	private static String platformMoneymoremore="p401";
	private static String privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIQffWLA9OLXsb6tnVGpvxFRNLy/umRaUfnKS4DI6To44fDHLOXs/HMSfK4tExe4uBIrRM5LaQxUwyjnP2xeZp3+mQ3GYsWWAkXm/L6FuIUk6Ndjzb4UTBoLskznRinIp0MJyndia6Mgubyn8Kse7YbxxfsQTWo5f5CfPqHlSqU/AgMBAAECgYBAJjnu1NkRusBmYE1d9Rj8A32jl0Ocre3XZk06flIfHrc0/L/j8yivhm5a8y+t+NYGnFOQBjU+83i+R2kX6M3RfcLHu0tXVfxmDSERzJJP0OmNfogXXJnLyVUGpVifqFfcvgVWMpG5Hy4KZZD+i73H+cYTVTOsuqRvUI88EInqoQJBALyMBcsDGCeCR3B4tteQWB9fp+e6a2pLfLR+v6mNuFDVaybn6pMtp4haucr8KzFYh2rCH6AKu62wO+z6vPTGSHsCQQCzY+hthvtEUBN/Pm5bUnVnKX3w+fLzwIHEvTCrXfls0VHWUVC6dC2Y5Iy60N/aj3cdmxEfTQ+NooTlQGnpzrUNAkAmdBBCZTEp7aIQSC5SLHgsfd/KnPSHSzn1vdvtAqBSrBQcbTQkLC1827QEuAU/HSURGuJES6wXMlgmbsTWzxG9AkBpPncRMvzdIiGeKFF0UFdCk8wogWuw58L6WohgMXzxA4kAtKopCZnqtkN+IqcCQeL/Qod0FrDGRo+zM+wvWK9NAkEAkINUcB2aNnPsW3VuTTtNdehFjmPGs1CWMRWvV7Bsp8G0cZcyodv1bkd+PyneWv5EpG+n0UD/tv2JDk8u7qRX4w==";
	private static String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEH31iwPTi17G+rZ1Rqb8RUTS8v7pkWlH5ykuAyOk6OOHwxyzl7PxzEnyuLRMXuLgSK0TOS2kMVMMo5z9sXmad/pkNxmLFlgJF5vy+hbiFJOjXY82+FEwaC7JM50YpyKdDCcp3YmujILm8p/CrHu2G8cX7EE1qOX+Qnz6h5UqlPwIDAQAB";
	private static String serverHost="218.4.234.150";
	private static String serverPort="88";
	
	
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	
	protected static ICashStreamDao cashStreamDao = context.getBean(ICashStreamDao.class);
	protected static IHttpClientService httpClientService = context.getBean(IHttpClientService.class);
	protected static IAccountService accountService = context.getBean(IAccountService.class);
	protected static ISubmitService submitService = context.getBean(ISubmitService.class);
	
	public static void main(String args[]) throws Exception{
		withdrawSingleCashStream("LN19029242014122113491557888");
	}
	
	public static String getBaseUrl(String action) {
		return url+urls.get(action);
	}
	
	public static void checkSingleCashStream(){
Integer cashStreamId = 2078;
		
		CashStream cashStream=cashStreamDao.find(cashStreamId);
		if(cashStream==null)
			return;
		String baseUrl=getBaseUrl(ACTION_ORDERQUERY);
		Map<String,String> params=new HashMap<String,String>();
		params.put("PlatformMoneymoremore", platformMoneymoremore);
		if(cashStream.getAction()==CashStream.ACTION_CASH)
			params.put("Action", "2");
		else if(cashStream.getAction()==CashStream.ACTION_RECHARGE)
			params.put("Action", "1");
		params.put("OrderNo", String.valueOf(cashStream.getId()));
		StringBuilder sBuilder=new StringBuilder();
		sBuilder.append(StringUtil.strFormat(params.get("PlatformMoneymoremore")));
		sBuilder.append(StringUtil.strFormat(params.get("Action")));
		sBuilder.append(StringUtil.strFormat(params.get("OrderNo")));
		RsaHelper rsa = RsaHelper.getInstance();
		params.put("SignInfo", rsa.signData(sBuilder.toString(), privateKey));
		String body=httpClientService.post(baseUrl, params);
		Gson gson = new Gson();
		
		List<Map<String, String>> res = (List<Map<String,String>>)gson.fromJson(body, List.class);
		System.out.println(res.size());
	}
	
	public static void withdrawSingleCashStream(String loanNo){
		String baseUrl=getBaseUrl(ACTION_CHECK);
		Map<String,String> params=new HashMap<String, String>();
		params.put("PlatformMoneymoremore", platformMoneymoremore);
		params.put("AuditType", String.valueOf(2));
		params.put("ReturnURL", "http://" + serverHost + ":" + serverPort + "/account/checkBuy/response/bg");
		params.put("NotifyURL", params.get("ReturnURL"));
		
		params.put("LoanNoList", loanNo);
		sendCheck(params,baseUrl);
		
	}
	
	private static void sendCheck(Map<String,String> params,String baseUrl)
	{
		//LoanNoList + PlatformMoneymoremore + AuditType + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ReturnURL + NotifyURL
		StringBuilder sBuilder=new StringBuilder();
		sBuilder.append(StringUtil.strFormat(params.get("LoanNoList")));
		sBuilder.append(StringUtil.strFormat(params.get("PlatformMoneymoremore")));
		sBuilder.append(StringUtil.strFormat(params.get("AuditType")));
		sBuilder.append(StringUtil.strFormat(params.get("RandomTimeStamp")));
		sBuilder.append(StringUtil.strFormat(params.get("Remark1")));
		sBuilder.append(StringUtil.strFormat(params.get("Remark2")));
		sBuilder.append(StringUtil.strFormat(params.get("Remark3")));
		sBuilder.append(StringUtil.strFormat(params.get("ReturnURL")));
		sBuilder.append(StringUtil.strFormat(params.get("NotifyURL")));
		RsaHelper rsa = RsaHelper.getInstance();
		String signInfo=rsa.signData(sBuilder.toString(), privateKey);
		params.put("SignInfo", signInfo);
		String body=httpClientService.post(baseUrl, params);
		Gson gson = new Gson();
		Map<String,String> returnParams=gson.fromJson(body, Map.class);
		System.out.println(returnParams);
//		try {
//			checkBuyProcessor(returnParams);
//		} catch (SignatureException e) {
//			e.printStackTrace();
//		} catch (ResultCodeException e) {
//			e.printStackTrace();
//		}
	}
}
