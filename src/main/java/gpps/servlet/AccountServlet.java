package gpps.servlet;

import gpps.dao.IBorrowerDao;
import gpps.dao.ICardBindingDao;
import gpps.dao.ICashStreamDao;
import gpps.dao.ILenderDao;
import gpps.dao.IProductSeriesDao;
import gpps.model.Borrower;
import gpps.model.CardBinding;
import gpps.model.CashStream;
import gpps.model.Lender;
import gpps.model.LenderAccount;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.Task;
import gpps.service.IAccountService;
import gpps.service.IBorrowerService;
import gpps.service.IGovermentOrderService;
import gpps.service.ILenderService;
import gpps.service.ILoginService;
import gpps.service.IPayBackService;
import gpps.service.IProductService;
import gpps.service.ISubmitService;
import gpps.service.ITaskService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.InsufficientBalanceException;
import gpps.service.thirdpay.IThirdPaySupportService;
import gpps.service.thirdpay.ResultCodeException;
import gpps.service.thirdpay.Transfer.LoanJson;
import gpps.tools.Common;
import gpps.tools.RsaHelper;
import gpps.tools.StringUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountServlet {
	@Autowired
	IAccountService accountService;
	@Autowired
	ILenderService lenderService;
	@Autowired
	ISubmitService submitService;
	@Autowired
	ICashStreamDao cashStreamDao;
	@Autowired
	IPayBackService payBackService;
	@Autowired
	ITaskService taskService;
	@Autowired
	IProductService productService;
	@Autowired
	IGovermentOrderService orderService;
	@Autowired
	IProductSeriesDao productSeriesDao;
	@Autowired
	IBorrowerService borrowerService;
	@Autowired
	IThirdPaySupportService thirdPaySupportService;
	@Autowired
	ICardBindingDao cardBindingDao;
	@Autowired
	ILenderDao lenderDao;
	@Autowired
	IBorrowerDao borrowerDao;
	Logger log = Logger.getLogger(AccountServlet.class);
	public static final String AMOUNT = "amount";
	public static final String CASHSTREAMID = "cashStreamId";
	public static final String SUBMITID = "submitId";
	public static final String PAYBACKID = "paybackId";

//	@RequestMapping(value = { "/account/thirdPartyRegist/request" })
//	public void thirdPartyRegist(HttpServletRequest req, HttpServletResponse resp) {
//		log.debug("第三方注册开始");
//		Object user = checkUserSession(req, resp);
//		if (user == null)
//			return;
//		writeThirdParty(resp, "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台注册认证</h3>说明：该步骤跳转到第三方平台进行用户账户注册.<p><a href='/account/thirdPartyRegist/response'>完成第三方认证</a></p></div>");
//		// log.debug("第三方注册完毕，跳转回本地");
//	}

	@RequestMapping(value = { "/account/thirdPartyRegist/response" })
	public void completeThirdPartyRegist(HttpServletRequest req, HttpServletResponse resp) {
		String message=null;
		try {
			completeThirdPartyRegistProcessor(req, resp);
		} catch (SignatureException e) {
			log.error(e.getMessage(),e);
			message=e.getMessage();
		} catch (ResultCodeException e) {
			//TODO 返回页面提示信息
			log.debug(e.getMessage(),e);
			message=e.getMessage();
		}
		//重定向到指定页面
		writeMsg(resp,message,"/myaccount.html?fid=mycenter");
	}
	
	@RequestMapping(value = { "/account/thirdPartyRegist/response/bg" })
	public void completeThirdPartyRegistBg(HttpServletRequest req, HttpServletResponse resp) {
		try {
			completeThirdPartyRegistProcessor(req, resp);
		} catch (SignatureException e) {
			log.error(e.getMessage(),e);
			return;
		} catch (ResultCodeException e) {
			log.debug(e.getMessage(),e);
			return;
		}
		writeSuccess(resp);
	}
	private void completeThirdPartyRegistProcessor(HttpServletRequest req, HttpServletResponse resp) throws SignatureException, ResultCodeException
	{
		log.debug("开户回调:"+req.getRequestURI());
		Map<String,String> params=getAllParams(req);
		String[] signStrs={"AccountType","AccountNumber","Mobile","Email","RealName","IdentificationNo","LoanPlatformAccount",
				"MoneymoremoreId","PlatformMoneymoremore","AuthFee","AuthState","RandomTimeStamp",
				"Remark1","Remark2","Remark3","ResultCode"};
		try {
			thirdPaySupportService.checkRollBack(params, signStrs);
		} catch (ResultCodeException e) {
			if(!e.getResultCode().equals("16"))
				throw e;
		}
		String thirdPartyAccount = params.get("MoneymoremoreId");
		String accountType=params.get("AccountType");
		String loanPlatformAccount=params.get("LoanPlatformAccount");
		String accountNumber=params.get("AccountNumber");
		Integer id=Integer.parseInt(loanPlatformAccount.substring(1, loanPlatformAccount.length()));
		String email=params.get("Email");
		String tel=params.get("Mobile");
		if (StringUtil.isEmpty(accountType)) {
			lenderService.registerThirdPartyAccount(id,thirdPartyAccount,accountNumber);
			Lender lender=lenderDao.find(id);
			if(StringUtil.isEmpty(email))
				email=lender.getEmail();
			if(StringUtil.isEmpty(tel))
				tel=lender.getTel();
			lenderDao.updateTelAndEmail(id, tel, email);
		} else if (accountType.equals("1")) {
			borrowerService.registerThirdPartyAccount(id,thirdPartyAccount,accountNumber);
			Borrower borrower=borrowerDao.find(id);
			if(StringUtil.isEmpty(email))
				email=borrower.getEmail();
			if(StringUtil.isEmpty(tel))
				tel=borrower.getTel();
			borrowerDao.updateTelAndEmail(id, tel, email);
		}
	}

//	@RequestMapping(value = { "/account/recharge/request" })
//	public void recharge(HttpServletRequest req, HttpServletResponse resp) {
//		String amount = req.getParameter(AMOUNT);
//		HttpSession session = req.getSession();
//		Object user = session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
//		if (user == null) {
//			try {
//				resp.sendError(403, "未找到用户信息，请重新登录");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return;
//		}
//		Integer cashStreamId = null;
//		if (user instanceof Lender) {
//			Lender lender = (Lender) user;
//			cashStreamId = accountService.rechargeLenderAccount(lender.getAccountId(), BigDecimal.valueOf(Double.valueOf(amount)), "充值");
//		} else if (user instanceof Borrower) {
//			Borrower borrower = (Borrower) user;
//			cashStreamId = accountService.rechargeBorrowerAccount(borrower.getAccountId(), BigDecimal.valueOf(Double.valueOf(amount)), "充值");
//		}
//		log.debug("充值：amount=" + amount + ",cashStreamId=" + cashStreamId);
//		log.debug("跳转到第三方进行充值");
//		writeThirdParty(resp, "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台充值</h3>说明：该步骤跳转到第三方平台,用户完成从银行卡到第三方平台账户的充值.<p><a href='/account/recharge/response?cashStreamId=" + cashStreamId + "'>充值成功</a><p><a href='javascript:window.close()'>充值失败</a></p></div>");
//	}

	@RequestMapping(value = { "/account/recharge/response" })
	public void completeRecharge(HttpServletRequest req, HttpServletResponse resp) {
		String message=null;
		try {
			completeRechargeProcessor(req, resp);
		} catch (SignatureException e) {
			log.error(e.getMessage(),e);
			message=e.getMessage();
		} catch (ResultCodeException e) {
			//TODO 返回页面提示信息
			log.debug(e.getMessage(),e);
			message=e.getMessage();
		}
		writeMsg(resp,message,"/myaccount.html?fid=cash&sid=cash-recharge");
	}
	@RequestMapping(value = { "/account/recharge/response/bg" })
	public void completeRechargeBg(HttpServletRequest req, HttpServletResponse resp) {
		try {
			completeRechargeProcessor(req, resp);
		} catch (SignatureException e) {
			log.error(e.getMessage(),e);
			return;
		} catch (ResultCodeException e) {
			log.debug(e.getMessage(),e);
		}
		writeSuccess(resp); 
	}
	private void completeRechargeProcessor(HttpServletRequest req, HttpServletResponse resp) throws SignatureException, ResultCodeException
	{
		log.debug("充值回调:"+req.getRequestURI());
		Map<String,String> params=getAllParams(req);
		String[] signStrs={"RechargeMoneymoremore","PlatformMoneymoremore","LoanNo","OrderNo","Amount","Fee","FeePlatform",
				"RechargeType","FeeType","CardNoList","RandomTimeStamp","Remark1","Remark2","Remark3","ResultCode"};
		thirdPaySupportService.checkRollBack(params, signStrs);
		Integer cashStreamId = Integer.parseInt(StringUtil.checkNullAndTrim("cashStreamId", StringUtil.strFormat(params.get("OrderNo"))));
		String loanNo=params.get("LoanNo");
		log.debug("充值成功");
		CashStream cashStream=cashStreamDao.find(cashStreamId);
		if(cashStream.getState()==CashStream.STATE_SUCCESS)
		{
			log.debug("重复的回复");
			return;
		}
//		if(cashStream.getChiefamount().compareTo(new BigDecimal(StringUtil.strFormat(params.get("Amount"))))!=0)
//		{
//			write(resp, "充值金额不符，请联系管理员解决.");
//			return;
//		}
		cashStreamDao.updateLoanNo(cashStreamId, loanNo,null);
		try {
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		} catch (IllegalConvertException e) {
			e.printStackTrace();
		}
	}
//	@RequestMapping(value = { "/account/cash/request" })
//	public void cash(HttpServletRequest req, HttpServletResponse resp) {
//		String amount = req.getParameter(AMOUNT);
//		HttpSession session = req.getSession();
//		Object user = session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
//		if (user == null) {
//			try {
//				resp.sendError(403, "未找到用户信息，请重新登录");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return;
//		}
//		Integer cashStreamId = null;
//		try {
//			if (user instanceof Lender) {
//				Lender lender = (Lender) user;
//				cashStreamId = accountService.cashLenderAccount(lender.getAccountId(), BigDecimal.valueOf(Double.valueOf(amount)), "提现");
//			} else if (user instanceof Borrower) {
//				Borrower borrower = (Borrower) user;
//				cashStreamId = accountService.cashBorrowerAccount(borrower.getAccountId(), BigDecimal.valueOf(Double.valueOf(amount)), "提现");
//			}
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (InsufficientBalanceException e) {
//			try {
//				resp.sendError(400, "余额不足");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			log.debug(e.getMessage(), e);
//			return;
//		}
//		log.debug("取现：amount=" + amount + ",cashStreamId=" + cashStreamId);
//		log.debug("跳转到第三方进行取现");
//		writeThirdParty(resp, "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台提现</h3>说明：该步骤跳转到第三方平台,用户输入第三方平台账户密码完成从账户到银行卡的提现.<p><a href='/account/cash/response?cashStreamId=" + cashStreamId + "'>提现成功</a><p><a href='javascript:window.close()'>提现失败</a></p></div>");
//	}

	@RequestMapping(value = { "/account/cash/response" })
	public void completeCash(HttpServletRequest req, HttpServletResponse resp) {
		String message=null;
		try {
			completeCashProcessor(req,resp);
		} catch (SignatureException e) {
			log.error(e.getMessage(),e);
			message=e.getMessage();
		} catch (ResultCodeException e) {
			log.debug(e.getMessage(),e);
			message=e.getMessage();
		}
		writeMsg(resp,message,"/myaccount.html?fid=cash&sid=cash-withdraw");
	}
	@RequestMapping(value = { "/account/cash/response/bg" })
	public void completeCashBg(HttpServletRequest req, HttpServletResponse resp) {
		try {
			completeCashProcessor(req,resp);
		} catch (SignatureException e) {
			log.error(e.getMessage(),e);
			return;
		} catch (ResultCodeException e) {
			log.debug(e.getMessage(),e);
		}
		writeSuccess(resp); 
	}
	private void completeCashProcessor(HttpServletRequest req,HttpServletResponse reps) throws SignatureException, ResultCodeException
	{
		log.debug("提现回调:"+req.getRequestURI());
		Map<String,String> params=getAllParams(req);
		String[] signStrs={"WithdrawMoneymoremore","PlatformMoneymoremore","LoanNo","OrderNo","Amount","FeeMax","FeeWithdraws",
				"FeePercent","Fee","FreeLimit","FeeRate","FeeSplitting","RandomTimeStamp","Remark1","Remark2","Remark3","ResultCode"};
		Integer cashStreamId = Integer.parseInt(StringUtil.checkNullAndTrim("cashStreamId", StringUtil.strFormat(params.get("OrderNo"))));
		try{
			thirdPaySupportService.checkRollBack(params, signStrs);
		}catch(ResultCodeException e)
		{
			String resultCode=e.getResultCode();
			if(resultCode.equals("89"))
			{
				try {
					accountService.returnCash(cashStreamId);
				} catch (IllegalConvertException e1) {
					e1.printStackTrace();
				}
			}
			else 
				throw e;
		}
		String loanNo=params.get("LoanNo");
		try {
			log.debug("取现成功");
			CashStream cashStream=cashStreamDao.find(cashStreamId);
			if(cashStream.getState()==CashStream.STATE_SUCCESS)
			{
				log.debug("重复的回复");
				return;
			}
//			if(cashStream.getChiefamount().negate().compareTo(new BigDecimal(StringUtil.strFormat(params.get("Amount"))))!=0)
//			{
//				write(resp, "取现金额不符，请联系管理员解决.");
//				return;
//			}
			cashStreamDao.updateLoanNo(cashStreamId, loanNo,new BigDecimal(params.get("FeeWithdraws")));
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		} catch (IllegalConvertException e) {
			log.error(e.getMessage(), e);
		}
	}
//	@RequestMapping(value = { "/account/buy/request" })
//	public void buy(HttpServletRequest req, HttpServletResponse resp) {
//		String pid = req.getParameter("pid");
//		HttpSession session = req.getSession();
//		Object user = session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
//		if (user == null) {
//			try {
//				resp.sendError(403, "未找到用户信息，请重新登录");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return;
//		}
//		Integer submitId = Integer.parseInt(StringUtil.checkNullAndTrim(SUBMITID, req.getParameter(SUBMITID)));
//		Submit submit = ObjectUtil.checkNullObject(Submit.class, submitService.find(submitId));
//		Integer cashStreamId = null;
//		try {
//			cashStreamId = accountService.freezeLenderAccount(((Lender) user).getAccountId(), submit.getAmount(), submitId, "购买");
//		} catch (InsufficientBalanceException e) {
//			try {
//				resp.sendError(400, "余额不足");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			log.debug(e.getMessage(), e);
//			return;
//		}
//		log.debug("购买：amount=" + submit.getAmount() + ",cashStreamId=" + cashStreamId);
//		log.debug("跳转到第三方进行购买");
//		// log.debug("第三方购买完毕，跳转回本地");
//		// try {
//		// resp.sendRedirect("/account/buy/response?cashStreamId="+cashStreamId);
//		// } catch (IOException e) {
//		// e.printStackTrace();
//		// }
//		String html = "";
//		if (pid != null) {
//			html = "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台付款</h3>" + "说明：该步骤跳转到第三方平台,用户输入第三方平台账户密码完成购买.<p><a href='/account/buy/response?cashStreamId=" + cashStreamId + "&success=true&pid=" + pid + "'>购买成功</a></p>" + "<p><a href='/account/buy/response?cashStreamId=" + cashStreamId + "&success=false&pid=" + pid + "'>购买失败</a></p></div>";
//		} else {
//			html = "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台付款</h3>" + "说明：该步骤跳转到第三方平台,用户输入第三方平台账户密码完成购买.<p><a href='/account/buy/response?cashStreamId=" + cashStreamId + "&success=true'>购买成功</a></p>" + "<p><a href='/account/buy/response?cashStreamId=" + cashStreamId + "&success=false'>购买失败</a></p></div>";
//		}
//
//		writeThirdParty(resp, html);
//	}

	@RequestMapping(value = { "/account/buy/response" })
	public void completeBuy(HttpServletRequest req, HttpServletResponse resp) {
		String message=null;
		try {
			completeBuyProcessor(req, resp);
		} catch (SignatureException e) {
			e.printStackTrace();
			message=e.getMessage();
		} catch (ResultCodeException e) {
			e.printStackTrace();
			message=e.getMessage();
		}
		String pid=(String) req.getAttribute("pid");
		if(!StringUtil.isEmpty(message)){
			writeMsg(resp,message,"/myaccount.html");
			return;
		}
		if (!StringUtil.isEmpty(pid))
			write(resp, "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台付款</h3>购买成功，<p><a href='/productdetail.html?pid=" + pid + "'>继续购买</a></p><a href='/myaccount.html'>返回我的帐户</a></div>");
		else {
			write(resp, "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台付款</h3>购买成功，<p><a href='/myaccount.html'>返回我的帐户</a></p></div>");
		}
//			log.debug("购买失败");
//			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_FAIL);
//			write(resp, "<head><script>window.location.href='/myaccount.html?fid=submit&sid=submit-toafford'</script></head>");
	}
	@RequestMapping(value = { "/account/buy/response/bg" })
	public void completeBuyBg(HttpServletRequest req, HttpServletResponse resp) {
		try {
			completeBuyProcessor(req, resp);
		} catch (SignatureException e) {
			e.printStackTrace();
			return;
		} catch (ResultCodeException e) {
			e.printStackTrace();
			return;
		}
		writeSuccess(resp);
	}
	private void completeBuyProcessor(HttpServletRequest req,HttpServletResponse resp) throws SignatureException, ResultCodeException
	{
		log.debug("购买回调:"+req.getRequestURI());
		Map<String,String> params=getAllParams(req);
		String[] signStrs={"LoanJsonList","PlatformMoneymoremore","Action","RandomTimeStamp","Remark1","Remark2","Remark3","ResultCode"};
		String loanJsonList = null;
		try {
			loanJsonList=URLDecoder.decode(params.get("LoanJsonList"),"UTF-8");
			params.put("LoanJsonList", loanJsonList);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		thirdPaySupportService.checkRollBack(params, signStrs);
		List<Object> loanJsons=Common.JSONDecodeList(loanJsonList, LoanJson.class);
		String pid = params.get("Remark1");
		req.setAttribute("pid", pid);
		LoanJson loanJson=(LoanJson)(loanJsons.get(0));
		Integer cashStreamId = Integer.parseInt(StringUtil.checkNullAndTrim(CASHSTREAMID, loanJson.getOrderNo()));
		String loanNo=loanJson.getLoanNo();
		CashStream cashStream = cashStreamDao.find(cashStreamId);
		if(cashStream.getState()==CashStream.STATE_SUCCESS)
		{
			log.debug("重复的回复");
			return;
		}
		try {
			submitService.confirmBuy(cashStream.getSubmitId());
			cashStreamDao.updateLoanNo(cashStreamId, loanNo,null);
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		} catch (IllegalConvertException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = { "/account/checkBuy/response/bg" })
	public void checkBuyBg(HttpServletRequest req,HttpServletResponse resp)
	{
		try {
			log.debug("购买确认回调:"+req.getRequestURI());
			Map<String,String> params=getAllParams(req);
			thirdPaySupportService.checkBuyProcessor(params);
		} catch (SignatureException e) {
			e.printStackTrace();
			return;
		} catch (ResultCodeException e) {
			e.printStackTrace();
			return;
		}
		writeSuccess(resp);
	}
//	@RequestMapping(value = { "/account/repay/request" })
//	public void repay(HttpServletRequest req, HttpServletResponse resp) {
//		// HttpSession session=req.getSession();
//		// Object
//		// user=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
//		// if(user==null)
//		// {
//		// try {
//		// resp.sendError(403,"未找到用户信息，请重新登录");
//		// } catch (IOException e) {
//		// e.printStackTrace();
//		// }
//		// return;
//		// }
//		// 测试暂时借款人账户ID不从Session取,而从payback中取
//		Integer payBackId = Integer.parseInt(StringUtil.checkNullAndTrim(PAYBACKID, req.getParameter(PAYBACKID)));
//		PayBack payBack = payBackService.find(payBackId);
//		Product currentProduct = productService.find(payBack.getProductId());
//		if (currentProduct.getState() != Product.STATE_REPAYING) {
//			write(resp, "该产品尚未进入还款阶段");
//			return;
//		}
//		// 验证还款顺序
//		List<Product> products = productService.findByGovermentOrder(currentProduct.getGovermentorderId());
//		for (Product product : products) {
//			if (product.getId() == (int) (currentProduct.getId())) {
//				List<PayBack> payBacks = payBackService.findAll(product.getId());
//				for (PayBack pb : payBacks) {
//					if (pb.getState() == PayBack.STATE_FINISHREPAY || pb.getState() == PayBack.STATE_REPAYING)
//						continue;
//					if (pb.getDeadline() < payBack.getDeadline()) {
//						write(resp, "请按时间顺序进行还款");
//						return;
//					}
//				}
//				continue;
//			}
//			product.setProductSeries(productSeriesDao.find(product.getProductseriesId()));
//			if (product.getProductSeries().getType() < currentProduct.getProductSeries().getType()) {
//				List<PayBack> payBacks = payBackService.findAll(product.getId());
//				for (PayBack pb : payBacks) {
//					if (pb.getState() == PayBack.STATE_FINISHREPAY || pb.getState() == PayBack.STATE_REPAYING)
//						continue;
//					if (pb.getDeadline() <= payBack.getDeadline()) {
//						write(resp, "请先还完稳健型/平衡型产品再进行此次还款");
//						return;
//					}
//				}
//			}
//		}
//
//		Integer cashStreamId = null;
//		try {
//			cashStreamId = accountService.freezeBorrowerAccount(payBack.getBorrowerAccountId(), payBack.getChiefAmount().add(payBack.getInterest()), payBack.getId(), "冻结");
//		} catch (InsufficientBalanceException e) {
//			try {
//				resp.sendError(400, "余额不足");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			log.debug(e.getMessage(), e);
//			return;
//		}
//		log.debug("还款：amount=" + payBack.getChiefAmount().add(payBack.getInterest()) + ",cashStreamId=" + cashStreamId);
//		log.debug("跳转到第三方进行还款");
//		writeThirdParty(resp, "说明：该步骤跳转到第三方平台,借款人输入第三方平台账户密码完成还款.<p><a href='/account/repay/response?cashStreamId=" + cashStreamId + "'>还款成功</a><p><a href='javascript:window.close()'>还款失败</a>");
//	}

	@RequestMapping(value = { "/account/repay/response/bg" })
	public void completeRepayBg(HttpServletRequest req, HttpServletResponse resp) {
		try {
			log.debug("还款回调:"+req.getRequestURI());
			Map<String,String> params=getAllParams(req);
			thirdPaySupportService.repayProcessor(params);
		} catch (SignatureException e) {
			e.printStackTrace();
			return;
		} catch (ResultCodeException e) {
			e.printStackTrace();
			return;
		}
		writeSuccess(resp);
	}
	@RequestMapping(value = { "/account/cardBinding/response" })
	public void completeCardBinding(HttpServletRequest req, HttpServletResponse resp) {
		String message=null;
		try {
			completeCardBindingProcessor(req, resp);
		} catch (SignatureException e) {
			e.printStackTrace();
			message=e.getMessage();
		} catch (ResultCodeException e) {
			e.printStackTrace();
			message=e.getMessage();
		}
		//重定向到指定页面
		writeMsg(resp,message,"/myaccount.html?fid=mycenter");
	}

	@RequestMapping(value = { "/account/cardBinding/response/bg" })
	public void completeCardBindingBg(HttpServletRequest req, HttpServletResponse resp) {
		try {
			completeCardBindingProcessor(req, resp);
		} catch (SignatureException e) {
			e.printStackTrace();
			return;
		} catch (ResultCodeException e) {
			e.printStackTrace();
			return;
		}
		writeSuccess(resp);
	}
	private void completeCardBindingProcessor(HttpServletRequest req,HttpServletResponse resp) throws SignatureException, ResultCodeException
	{
		log.debug("购买回调:"+req.getRequestURI());
		Map<String,String> params=getAllParams(req);
		String[] signStrs={"MoneymoremoreId","PlatformMoneymoremore","Action","CardType","BankCode","CardNo","BranchBankName",
				"Province","City","WithholdBeginDate","WithholdEndDate","SingleWithholdLimit","TotalWithholdLimit+ "
						+ "RandomTimeStamp","Remark1","Remark2","Remark3","ResultCode"};
		RsaHelper rsa = RsaHelper.getInstance();
		String cardNo=rsa.decryptData(params.get("CardNo"), thirdPaySupportService.getPrivateKey());
		params.put("CardNo", cardNo);
		thirdPaySupportService.checkRollBack(params, signStrs);
		String moneymoremoreId=params.get("MoneymoremoreId");
		Lender lender=lenderDao.findByThirdPartyAccount(moneymoremoreId);
		if(lender!=null)
		{
			if(lender.getCardBindingId()!=null)
			{
				CardBinding orignal=cardBindingDao.find(lender.getCardBindingId());
				if(orignal!=null&&orignal.getCardNo().equals(cardNo))
				{
					//已绑定
					return;
				}
			}
			CardBinding cardBinding=new CardBinding();
			cardBinding.setBankCode(params.get("BankCode"));
			cardBinding.setBranchBankName(params.get("BranchBankName"));
			cardBinding.setCardNo(cardNo);
			cardBinding.setCardType(Integer.parseInt(params.get("CardType")));
			cardBinding.setCity(params.get("City"));
			cardBinding.setProvince(params.get("Province"));
			cardBindingDao.create(cardBinding);
			lenderService.bindCard(lender.getId(), cardBinding.getId());
			//加一分钱
			Integer cashStreamId=accountService.rechargeLenderAccount(lender.getAccountId(), new BigDecimal(0.01), "快捷支付充值");
			try {
				accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
			} catch (IllegalConvertException e) {
				e.printStackTrace();
			}
		}
//		else 
//		{
//			Borrower borrower=borrowerDao.findByThirdPartyAccount(moneymoremoreId);
//			if(borrower!=null)
//			{
//				borrowerService.bindCard(borrower.getId(), cardBinding.getId());
//				//加一分钱
//				Integer cashStreamId=accountService.rechargeBorrowerAccount(borrower.getAccountId(), new BigDecimal(0.01), "快捷支付充值");
//				try {
//					accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
//				} catch (IllegalConvertException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
	@RequestMapping(value = { "/account/authorize/response" })
	public void completeAuthorize(HttpServletRequest req,HttpServletResponse resp)
	{
		String message=null;
		try {
			completeAuthorizeProcessor(req,resp);
		} catch (SignatureException e) {
			e.printStackTrace();
			message=e.getMessage();
		} catch (ResultCodeException e) {
			e.printStackTrace();
			message=e.getMessage();
		}
		writeMsg(resp,message,"/myaccount.html?fid=mycenter");
	}
	@RequestMapping(value = { "/account/authorize/response/bg" })
	public void completeAuthorizeBg(HttpServletRequest req,HttpServletResponse resp)
	{
		try {
			completeAuthorizeProcessor(req,resp);
		} catch (SignatureException e) {
			e.printStackTrace();
			return;
		} catch (ResultCodeException e) {
			e.printStackTrace();
			return;
		}
		writeSuccess(resp);
	}
	private void completeAuthorizeProcessor(HttpServletRequest req,HttpServletResponse resp) throws SignatureException, ResultCodeException
	{
		log.debug("授权回调:"+req.getRequestURI());
		Map<String,String> params=getAllParams(req);
		String[] signStrs={"MoneymoremoreId","PlatformMoneymoremore","AuthorizeTypeOpen","AuthorizeTypeClose","AuthorizeType","RandomTimeStamp","Remark1","Remark2","Remark3","ResultCode"};
		thirdPaySupportService.checkRollBack(params, signStrs);
		String authorizeTypeOpen=params.get("AuthorizeTypeOpen");
		String authorizeTypeClose=params.get("AuthorizeTypeClose");
		String moneymoremoreId=params.get("MoneymoremoreId");
		Borrower borrower=borrowerDao.findByThirdPartyAccount(moneymoremoreId);
		int originalAuthorizeTypeOpen=borrower.getAuthorizeTypeOpen();
		if(!StringUtil.isEmpty(authorizeTypeOpen))
		{
			String[] strs=authorizeTypeOpen.split(",");
			for(String str:strs)
			{
				if(str.equals("3"))
					originalAuthorizeTypeOpen=originalAuthorizeTypeOpen|Borrower.AUTHORIZETYPEOPEN_SECORD;
				else
					originalAuthorizeTypeOpen=originalAuthorizeTypeOpen|Integer.parseInt(str);
			}
		}
		if(!StringUtil.isEmpty(authorizeTypeClose))
		{
			String[] strs=authorizeTypeOpen.split(",");
			for(String str:strs)
			{
				if(str.equals("3"))
					originalAuthorizeTypeOpen=originalAuthorizeTypeOpen-(originalAuthorizeTypeOpen&Borrower.AUTHORIZETYPEOPEN_SECORD);
				else
					originalAuthorizeTypeOpen=originalAuthorizeTypeOpen-(originalAuthorizeTypeOpen&Integer.parseInt(str));
			}
		}
		borrowerDao.updateAuthorizeTypeOpen(borrower.getId(), originalAuthorizeTypeOpen);
		borrower=borrowerService.getCurrentUser();
		if(borrower!=null)
			borrower.setAuthorizeTypeOpen(originalAuthorizeTypeOpen);
	}
	private void writeThirdParty(HttpServletResponse resp, String message) {

		StringBuilder text = new StringBuilder();
		text.append("<div style='width:100%; text-align:center; color:#999'>");
		text.append("<h1>第三方支付平台</h1>");
		text.append("<p>当前页面为第三方平台（如汇付天下）模拟页面，模拟本系统集成第三方平台后用户在第三方平台可以做的操作</p>");
		text.append("<p>用户可操作如下，如用户在跳转到第三方后未进行任何操作直接关闭第三方平台页面，则该种情况等同于操作失败</p>");
		text.append("</div>");
		text.append(message);
		write(resp, text.toString());
	}

	private void writeLocal(HttpServletResponse resp, String message) {
		StringBuilder text = new StringBuilder();
		text.append("<div style='width:100%; text-align:center; color:#999'>");
		text.append("<h1>政采贷</h1>");
		text.append("<p>该页面为第三方操作成功后的返回页面，会提示\"第三方操作成功\"之类的说明文字，然后自动跳转到用户的相应页面，具体跳转页面如下所示</p>");
		text.append("<p>实际系统在提示操作成功后倒计时5s自动跳转到相应页面，此处为了流程演示，暂时由用户点击后才跳转到相应页面.</p>");
		text.append("</div>");
		text.append(message);
		write(resp, text.toString());
	}

	private void write(HttpServletResponse resp, String message) {
		resp.setContentType("text/html");
		resp.setCharacterEncoding("utf-8");
		PrintWriter writer = null;
		try {
			writer = resp.getWriter();
			writer.write(message.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}

	}
	private void writeMsg(HttpServletResponse resp, String message,String redirct) {
		resp.setContentType("text/html");
		resp.setCharacterEncoding("utf-8");
		StringBuilder sBuilder=new StringBuilder();
		sBuilder.append("<head><script>");
		if(!StringUtil.isEmpty(message))
		{
			sBuilder.append("alert('").append(message).append("');");
		}
		sBuilder.append("window.location.href='").append(redirct).append("'</script></head>");
		PrintWriter writer = null;
		try {
			writer = resp.getWriter();
			writer.write(sBuilder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}

	}

	private Object checkUserSession(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		Object user = session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		if (user == null) {
			try {
				resp.sendError(403, "未找到用户信息，请重新登录");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return user;
	}

	private Map<String, String> getAllParams(HttpServletRequest req) {
		Map<String, String> params = new HashMap<String, String>();
		try {
			req.setCharacterEncoding("UTF-8");
			Map m = req.getParameterMap();
			Iterator it = m.keySet().iterator();
			while (it.hasNext()) {
				String key = String.valueOf(it.next());
				String[] values = (String[]) (m.get(key));
				String value=values[0];
				log.info(key + "=" + value);
				params.put(key, value);
			}
		} catch (Throwable e) {
			log.error(e.getMessage(),e);
		}
		return params;
	}
	private void writeSuccess(HttpServletResponse resp)
	{
		resp.setCharacterEncoding("UTF-8");
		resp.setStatus(200);
		PrintWriter writer = null;
		try {
			writer = resp.getWriter();
			writer.write("SUCCESS");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}
}
