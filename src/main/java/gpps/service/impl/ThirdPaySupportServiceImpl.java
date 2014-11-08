package gpps.service.impl;

import gpps.model.Borrower;
import gpps.model.Lender;
import gpps.service.IAccountService;
import gpps.service.ILoginService;
import gpps.service.thirdpay.IThirdPaySupportService;
import gpps.service.thirdpay.Recharge;
import gpps.service.thirdpay.RegistAccount;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
public class ThirdPaySupportServiceImpl implements IThirdPaySupportService{
	public static final String ACTION_REGISTACCOUNT="0";
	public static final String ACTION_RECHARGE="0";
	private static Map<String, String> urls=new HashMap<String, String>();
	static {
		urls.put(String.valueOf(ACTION_REGISTACCOUNT), "/loan/toloanregisterbind.action");
		urls.put(String.valueOf(ACTION_RECHARGE), "/loan/toloanrecharge.action");
	}
	private String url="";
	private String platformMoneymoremore="p401";
	private String privateKey;
	@Autowired
	IAccountService accountService;
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getBaseUrl(String action) {
		return url+urls.get(action);
	}
	
	public String getPlatformMoneymoremore() {
		return platformMoneymoremore;
	}

	public void setPlatformMoneymoremore(String platformMoneymoremore) {
		this.platformMoneymoremore = platformMoneymoremore;
	}
	@Override
	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	@Override
	public RegistAccount getRegistAccount() {
		RegistAccount registAccount=new RegistAccount();
		registAccount.setBaseUrl(getBaseUrl(ACTION_REGISTACCOUNT));
		HttpServletRequest req=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session=req.getSession();
		Object currentUser=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		if(currentUser instanceof Lender)
		{
			registAccount.setAccountType(null);
			Lender lender=(Lender)currentUser;
			registAccount.setEmail(lender.getEmail());
			registAccount.setIdentificationNo(lender.getIdentityCard());
			registAccount.setLoanPlatformAccount("L"+lender.getId());
			registAccount.setMobile(lender.getTel());
			registAccount.setRealName(lender.getName());
		}else if(currentUser instanceof Borrower){
			registAccount.setAccountType(1);
			Borrower borrower=(Borrower)currentUser;
			registAccount.setEmail(borrower.getEmail());
			registAccount.setIdentificationNo(borrower.getLicense());
			registAccount.setLoanPlatformAccount("B"+borrower.getId());
			registAccount.setMobile(borrower.getTel());
			registAccount.setRealName(borrower.getName());
		}
		else {
			throw new RuntimeException("不支持该用户开户");
		}
		registAccount.setReturnURL(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/account/thirdPartyRegist/response");
		registAccount.setNotifyURL(registAccount.getReturnURL()+"/bg");
		registAccount.setPlatformMoneymoremore(platformMoneymoremore);
		registAccount.setSignInfo(registAccount.getSign(privateKey));
		return registAccount;
	}

	@Override
	public Recharge getRecharge(String amount) {
		Recharge recharge=new Recharge();
		recharge.setBaseUrl(getBaseUrl(ACTION_REGISTACCOUNT));
		HttpServletRequest req=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session=req.getSession();
		Object currentUser=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		
		recharge.setAmount(amount);
		recharge.setReturnURL(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/account/recharge/response");
		recharge.setNotifyURL(recharge.getReturnURL()+"/bg");
		recharge.setPlatformMoneymoremore(platformMoneymoremore);
		recharge.setSignInfo(recharge.getSign(privateKey));
		Integer cashStreamId = null;
		if(currentUser instanceof Lender)
		{
			Lender lender=(Lender)currentUser;
			recharge.setRechargeMoneymoremore(lender.getThirdPartyAccount());
			cashStreamId = accountService.rechargeLenderAccount(lender.getAccountId(), BigDecimal.valueOf(Double.valueOf(amount)), "充值");
		}else if(currentUser instanceof Borrower){
			Borrower borrower=(Borrower)currentUser;
			recharge.setRechargeMoneymoremore(borrower.getThirdPartyAccount());
			cashStreamId = accountService.rechargeBorrowerAccount(borrower.getAccountId(), BigDecimal.valueOf(Double.valueOf(amount)), "充值");
		}
		else {
			throw new RuntimeException("不支持该用户开户");
		}
		recharge.setOrderNo(String.valueOf(cashStreamId));
		recharge.setSignInfo(recharge.getSign(privateKey));
		return recharge;
	}
}
