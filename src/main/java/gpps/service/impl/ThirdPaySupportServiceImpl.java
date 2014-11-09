package gpps.service.impl;

import gpps.model.Borrower;
import gpps.model.GovermentOrder;
import gpps.model.Lender;
import gpps.model.Product;
import gpps.model.Submit;
import gpps.service.IAccountService;
import gpps.service.IBorrowerService;
import gpps.service.IGovermentOrderService;
import gpps.service.ILenderService;
import gpps.service.ILoginService;
import gpps.service.IProductService;
import gpps.service.ISubmitService;
import gpps.service.exception.InsufficientBalanceException;
import gpps.service.thirdpay.IThirdPaySupportService;
import gpps.service.thirdpay.Recharge;
import gpps.service.thirdpay.RegistAccount;
import gpps.service.thirdpay.Transfer;
import gpps.service.thirdpay.Transfer.LoanJson;
import gpps.tools.Common;
import gpps.tools.ObjectUtil;
import gpps.tools.StringUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
public class ThirdPaySupportServiceImpl implements IThirdPaySupportService{
	public static final String ACTION_REGISTACCOUNT="0";
	public static final String ACTION_RECHARGE="1";
	public static final String ACTION_TRANSFER="2";
	private static Map<String, String> urls=new HashMap<String, String>();
	static {
		urls.put(ACTION_REGISTACCOUNT, "/loan/toloanregisterbind.action");
		urls.put(ACTION_RECHARGE, "/loan/toloanrecharge.action");
		urls.put(ACTION_TRANSFER, "/loan/loan.action");
	}
	private String url="";
	private String platformMoneymoremore="p401";
	private String privateKey;
	@Autowired
	IAccountService accountService;
	@Autowired
	ILenderService lenderService;
	@Autowired
	ISubmitService submitService;
	@Autowired
	IProductService productService;
	@Autowired
	IGovermentOrderService orderService;
	@Autowired
	IBorrowerService borrowerService;
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
			registAccount.setRealName(borrower.getCompanyName());//放置公司名称，公司名称必须以“有限公司”结尾
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
		recharge.setBaseUrl(getBaseUrl(ACTION_RECHARGE));
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

	@Override
	public Transfer getTransferToBuy(Integer submitId,String pid) throws InsufficientBalanceException {
		Transfer transfer=new Transfer();
		transfer.setBaseUrl(getBaseUrl(ACTION_TRANSFER));
		
		Lender lender=lenderService.getCurrentUser();
		if(lender==null)
			throw new RuntimeException("未找到用户信息，请重新登录");
		Submit submit = ObjectUtil.checkNullObject(Submit.class, submitService.find(submitId));
		GovermentOrder order=orderService.findGovermentOrderByProduct(submit.getProductId());
		Borrower borrower=borrowerService.find(order.getId());
		
		HttpServletRequest req=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		transfer.setAction("1");
		transfer.setNeedAudit(null);//空.需要审核
		transfer.setReturnURL(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/account/buy/response");
		transfer.setNotifyURL(transfer.getReturnURL()+"/bg");
		transfer.setPlatformMoneymoremore(platformMoneymoremore);
		transfer.setRemark1(pid);
		transfer.setTransferAction("1");//投标
		transfer.setTransferType("2");//直连
		
		List<LoanJson> loanJsons=new ArrayList<LoanJson>();
		
		Integer cashStreamId = accountService.freezeLenderAccount(lender.getAccountId(), submit.getAmount(), submitId, "购买");
		LoanJson loanJson=new LoanJson();
		loanJson.setLoanOutMoneymoremore(lender.getThirdPartyAccount());
		loanJson.setLoanInMoneymoremore(borrower.getThirdPartyAccount());
		loanJson.setOrderNo(cashStreamId.toString());
		loanJson.setBatchNo(String.valueOf(submit.getProductId()));//????
//		loanJson.setExchangeBatchNo(null);
//		loanJson.setAdvanceBatchNo(null);
		loanJson.setAmount(submit.getAmount().toString());
		loanJson.setFullAmount("");
		loanJson.setTransferName("投标");
		loanJson.setRemark("");
		loanJson.setSecondaryJsonList("");
		loanJsons.add(loanJson);
		try {
			transfer.setLoanJsonList(URLEncoder.encode(Common.JSONEncode(loanJsons),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		transfer.setSignInfo(transfer.getSign(privateKey));
		return transfer;
	}
}
