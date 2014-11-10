package gpps.service.impl;

import gpps.dao.ICardBindingDao;
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
import gpps.service.thirdpay.CardBinding;
import gpps.service.thirdpay.Cash;
import gpps.service.thirdpay.IHttpClientService;
import gpps.service.thirdpay.IThirdPaySupportService;
import gpps.service.thirdpay.Recharge;
import gpps.service.thirdpay.RegistAccount;
import gpps.service.thirdpay.Transfer;
import gpps.service.thirdpay.Transfer.LoanJson;
import gpps.tools.Common;
import gpps.tools.ObjectUtil;
import gpps.tools.RsaHelper;
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
public class ThirdPaySupportServiceImpl implements IThirdPaySupportService{
	public static final String ACTION_REGISTACCOUNT="0";
	public static final String ACTION_RECHARGE="1";
	public static final String ACTION_TRANSFER="2";
	public static final String ACTION_CHECK="3";
	public static final String ACTION_CARDBINDING="4";
	public static final String ACTION_CASH="5";
	private static Map<String, String> urls=new HashMap<String, String>();
	static {
		urls.put(ACTION_REGISTACCOUNT, "/loan/toloanregisterbind.action");
		urls.put(ACTION_RECHARGE, "/loan/toloanrecharge.action");
		urls.put(ACTION_TRANSFER, "/loan/loan.action");
		urls.put(ACTION_CHECK, "/loan/toloantransferaudit.action");
		urls.put(ACTION_CARDBINDING, "/loan/toloanfastpay.action");
		urls.put(ACTION_CASH, "/loan/toloanwithdraws.action");
	}
	private String url="";
	private String platformMoneymoremore="p401";
	private String privateKey;
	private String publicKey;
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
	@Autowired
	IHttpClientService httpClientService;
	@Autowired
	ICardBindingDao cardBindingDao;
	
	private Logger log=Logger.getLogger(ThirdPaySupportServiceImpl.class);
	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

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
			throw new RuntimeException("不支持该用户充值");
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

	@Override
	public void check(List<String> loanNos,int auditType) {
		if(loanNos==null||loanNos.size()==0)
			return;
		HttpServletRequest req=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		StringBuilder loanNoSBuilder=new StringBuilder();
		Map<String,String> params=new HashMap<String, String>();
		params.put("PlatformMoneymoremore", platformMoneymoremore);
		params.put("AuditType", String.valueOf(auditType));
		params.put("ReturnURL", req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/account/checkBuy/response/bg");
		params.put("NotifyURL", params.get("ReturnURL"));
		for(int i=0;i<loanNos.size();i++)
		{
			if(loanNoSBuilder.length()!=0)
				loanNoSBuilder.append(",");
			loanNoSBuilder.append(loanNos.get(i));
			if(i>0&&i%200==0)
			{
				params.put("LoanNoList", loanNoSBuilder.toString());
				
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
				String body=httpClientService.post(url, params);
				log.info(body);
				
				loanNoSBuilder=new StringBuilder();
			}
		}
		if(loanNoSBuilder.length()>0)
		{
			params.put("LoanNoList", loanNoSBuilder.toString());
			
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
			String body=httpClientService.post(url, params);
			log.info(body);
		}
	}

	@Override
	public CardBinding getCardBinding() {
		CardBinding cardBinding=new CardBinding();
		cardBinding.setBaseUrl(getBaseUrl(ACTION_CARDBINDING));
		HttpServletRequest req=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session=req.getSession();
		Object currentUser=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		cardBinding.setPlatformMoneymoremore(platformMoneymoremore);
		cardBinding.setAction("2");
//		RsaHelper rsa = RsaHelper.getInstance();
//		cardBinding.setCardNo(cardNo);
		cardBinding.setReturnURL(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() +"/account/cardBinding/response");
		cardBinding.setNotifyURL(cardBinding.getReturnURL()+"/bg");
		if(currentUser instanceof Lender)
		{
			Lender lender=(Lender)currentUser;
			cardBinding.setMoneymoremoreId(lender.getThirdPartyAccount());
		}else if(currentUser instanceof Borrower){
			Borrower borrower=(Borrower)currentUser;
			cardBinding.setMoneymoremoreId(borrower.getThirdPartyAccount());
		}
		else {
			throw new RuntimeException("不支持该用户开户");
		}
		cardBinding.setSignInfo(cardBinding.getSign(privateKey));
//		cardBinding.setCardNo(rsa.encryptData(cardNo, publicKey));
		return cardBinding;
	}

	@Override
	public Cash getCash(String amount) throws InsufficientBalanceException {
		Cash cash=new Cash();
		cash.setBaseUrl(getBaseUrl(ACTION_CASH));
		HttpServletRequest req=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session=req.getSession();
		Object currentUser=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		cash.setPlatformMoneymoremore(platformMoneymoremore);
		cash.setAmount(amount);
		Integer cashStreamId = null;
		String cardNo=null;
		gpps.model.CardBinding cardBinding=null;
		if(currentUser instanceof Lender)
		{
			Lender lender=(Lender)currentUser;
			cash.setWithdrawMoneymoremore(lender.getThirdPartyAccount());
			cashStreamId = accountService.cashLenderAccount(lender.getAccountId(), BigDecimal.valueOf(Double.valueOf(amount)), "提现");
			cardBinding=cardBindingDao.find(lender.getCardBindingId());
		}else if(currentUser instanceof Borrower){
			Borrower borrower=(Borrower)currentUser;
			cash.setWithdrawMoneymoremore(borrower.getThirdPartyAccount());
			cashStreamId = accountService.cashBorrowerAccount(borrower.getAccountId(), BigDecimal.valueOf(Double.valueOf(amount)), "提现");
			cardBinding=cardBindingDao.find(borrower.getCardBindingId());
		}
		else {
			throw new RuntimeException("不支持该用户体现");
		}
		if(cardBinding==null)
			throw new RuntimeException("未绑定银行卡");
		cardNo=cardBinding.getCardNo();
		cash.setCardNo(cardNo);
		cash.setCardType(String.valueOf(cardBinding.getCardType()));
		cash.setBankCode(cardBinding.getBankCode());
		cash.setBranchBankName(cardBinding.getBranchBankName());
		cash.setProvince(cardBinding.getProvince());
		cash.setCity(cardBinding.getCity());
		cash.setOrderNo(String.valueOf(cashStreamId));
		cash.setReturnURL(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() +"/account/cash/response");
		cash.setNotifyURL(cash.getReturnURL()+"/bg");
		cash.setSignInfo(cash.getSign(privateKey));
		RsaHelper rsa = RsaHelper.getInstance();
		cash.setCardNo(rsa.encryptData(cardNo, publicKey));
		return cash;
	}
}
