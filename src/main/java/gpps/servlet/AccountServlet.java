package gpps.servlet;

import gpps.dao.ICashStreamDao;
import gpps.dao.IProductSeriesDao;
import gpps.model.Borrower;
import gpps.model.CashStream;
import gpps.model.Lender;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.Submit;
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
import gpps.tools.ObjectUtil;
import gpps.tools.RsaHelper;
import gpps.tools.StringUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
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
	Logger log = Logger.getLogger(AccountServlet.class);
	public static final String AMOUNT = "amount";
	public static final String CASHSTREAMID = "cashStreamId";
	public static final String SUBMITID = "submitId";
	public static final String PAYBACKID = "paybackId";

	@RequestMapping(value = { "/account/thirdPartyRegist/request" })
	public void thirdPartyRegist(HttpServletRequest req, HttpServletResponse resp) {
		log.debug("第三方注册开始");
		Object user = checkUserSession(req, resp);
		if (user == null)
			return;
		writeThirdParty(resp, "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台注册认证</h3>说明：该步骤跳转到第三方平台进行用户账户注册.<p><a href='/account/thirdPartyRegist/response'>完成第三方认证</a></p></div>");
		// log.debug("第三方注册完毕，跳转回本地");
	}

	@RequestMapping(value = { "/account/thirdPartyRegist/response" })
	public void completeThirdPartyRegist(HttpServletRequest req, HttpServletResponse resp) {
		completeThirdPartyRegistBg(req, resp);
		//重定向到指定页面
		write(resp,"<head><script>window.location.href='/views/google/myaccount.html?fid=mycenter'</script></head>");
	}

	@RequestMapping(value = { "/account/thirdPartyRegist/response/bg" })
	public void completeThirdPartyRegistBg(HttpServletRequest req, HttpServletResponse resp) {
		log.info("后台回调:"+req.getRequestURI());
		Map<String,String> params=getAllParams(req);
		if(params==null)
		{
			write(resp, "解析第三方支持错误");
			return;
		}
		String resultCode=params.get("ResultCode");
		if(StringUtil.isEmpty(resultCode)||!resultCode.equals("88"))
		{
			write(resp, "第三方支付返回错误,结果代码："+resultCode+",错误信息："+params.get("Message"));
			return;
		}
		//AccountType + AccountNumber + Mobile + Email + RealName + IdentificationNo + LoanPlatformAccount 
		//+ MoneymoremoreId + PlatformMoneymoremore + AuthFee + AuthState + RandomTimeStamp + Remark1 + Remark2 + Remark3 
		//+ ResultCode
		StringBuilder sBuilder=new StringBuilder();
		sBuilder.append(StringUtil.strFormat(params.get("AccountType")));
		sBuilder.append(StringUtil.strFormat(params.get("AccountNumber")));
		sBuilder.append(StringUtil.strFormat(params.get("Mobile")));
		sBuilder.append(StringUtil.strFormat(params.get("Email")));
		sBuilder.append(StringUtil.strFormat(params.get("RealName")));
		sBuilder.append(StringUtil.strFormat(params.get("IdentificationNo")));
		sBuilder.append(StringUtil.strFormat(params.get("LoanPlatformAccount")));
		sBuilder.append(StringUtil.strFormat(params.get("MoneymoremoreId")));
		sBuilder.append(StringUtil.strFormat(params.get("PlatformMoneymoremore")));
		sBuilder.append(StringUtil.strFormat(params.get("AuthFee")));
		sBuilder.append(StringUtil.strFormat(params.get("AuthState")));
		sBuilder.append(StringUtil.strFormat(params.get("RandomTimeStamp")));
		sBuilder.append(StringUtil.strFormat(params.get("Remark1")));
		sBuilder.append(StringUtil.strFormat(params.get("Remark2")));
		sBuilder.append(StringUtil.strFormat(params.get("Remark3")));
		sBuilder.append(StringUtil.strFormat(params.get("ResultCode")));
		RsaHelper rsa = RsaHelper.getInstance();
		String sign=rsa.signData(sBuilder.toString(), thirdPaySupportService.getPrivateKey());
		if(!sign.equals(params.get("SignInfo")))
		{
			write(resp, "错误的签名");
			return;
		}
		String thirdPartyAccount = params.get("MoneymoremoreId");
		HttpSession session = req.getSession();
		Object user = session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		if (user == null) {
			try {
				resp.sendError(403, "未找到用户信息，请重新登录");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		if (user instanceof Lender) {
			lenderService.registerThirdPartyAccount(thirdPartyAccount);
		} else if (user instanceof Borrower) {
			borrowerService.registerThirdPartyAccount(thirdPartyAccount);
		}
	}

	@RequestMapping(value = { "/account/recharge/request" })
	public void recharge(HttpServletRequest req, HttpServletResponse resp) {
		String amount = req.getParameter(AMOUNT);
		HttpSession session = req.getSession();
		Object user = session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		if (user == null) {
			try {
				resp.sendError(403, "未找到用户信息，请重新登录");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		Integer cashStreamId = null;
		if (user instanceof Lender) {
			Lender lender = (Lender) user;
			cashStreamId = accountService.rechargeLenderAccount(lender.getAccountId(), BigDecimal.valueOf(Double.valueOf(amount)), "充值");
		} else if (user instanceof Borrower) {
			Borrower borrower = (Borrower) user;
			cashStreamId = accountService.rechargeBorrowerAccount(borrower.getAccountId(), BigDecimal.valueOf(Double.valueOf(amount)), "充值");
		}
		log.debug("充值：amount=" + amount + ",cashStreamId=" + cashStreamId);
		log.debug("跳转到第三方进行充值");
		writeThirdParty(resp, "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台充值</h3>说明：该步骤跳转到第三方平台,用户完成从银行卡到第三方平台账户的充值.<p><a href='/account/recharge/response?cashStreamId=" + cashStreamId + "'>充值成功</a><p><a href='javascript:window.close()'>充值失败</a></p></div>");
	}

	@RequestMapping(value = { "/account/recharge/response" })
	public void completeRecharge(HttpServletRequest req, HttpServletResponse resp) {
		Integer cashStreamId = Integer.parseInt(StringUtil.checkNullAndTrim("cashStreamId", req.getParameter(CASHSTREAMID)));
		try {
			log.debug("充值成功");
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		} catch (IllegalConvertException e) {
			log.error(e.getMessage(), e);
		}
		// TODO 重定向到指定页面
		write(resp, "<head><script>window.location.href='/views/google/myaccount.html?fid=cash&sid=cash-recharge'</script></head>");
		// writeLocal(resp,
		// "充值成功，5秒后自动跳转到我的帐户<a href='/views/google/myaccount.html?fid=cash&sid=cash-recharge'>我的帐户</a>");
	}

	@RequestMapping(value = { "/account/cash/request" })
	public void cash(HttpServletRequest req, HttpServletResponse resp) {
		String amount = req.getParameter(AMOUNT);
		HttpSession session = req.getSession();
		Object user = session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		if (user == null) {
			try {
				resp.sendError(403, "未找到用户信息，请重新登录");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		Integer cashStreamId = null;
		try {
			if (user instanceof Lender) {
				Lender lender = (Lender) user;
				cashStreamId = accountService.cashLenderAccount(lender.getAccountId(), BigDecimal.valueOf(Double.valueOf(amount)), "提现");
			} else if (user instanceof Borrower) {
				Borrower borrower = (Borrower) user;
				cashStreamId = accountService.cashBorrowerAccount(borrower.getAccountId(), BigDecimal.valueOf(Double.valueOf(amount)), "提现");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InsufficientBalanceException e) {
			try {
				resp.sendError(400, "余额不足");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.debug(e.getMessage(), e);
			return;
		}
		log.debug("取现：amount=" + amount + ",cashStreamId=" + cashStreamId);
		log.debug("跳转到第三方进行取现");
		writeThirdParty(resp, "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台提现</h3>说明：该步骤跳转到第三方平台,用户输入第三方平台账户密码完成从账户到银行卡的提现.<p><a href='/account/cash/response?cashStreamId=" + cashStreamId + "'>提现成功</a><p><a href='javascript:window.close()'>提现失败</a></p></div>");
	}

	@RequestMapping(value = { "/account/cash/response" })
	public void completeCash(HttpServletRequest req, HttpServletResponse resp) {
		Integer cashStreamId = Integer.parseInt(StringUtil.checkNullAndTrim("cashStreamId", req.getParameter(CASHSTREAMID)));
		try {
			log.debug("取现成功");
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		} catch (IllegalConvertException e) {
			log.error(e.getMessage(), e);
		}

		write(resp, "<head><script>window.location.href='/views/google/myaccount.html?fid=cash&sid=cash-withdraw'</script></head>");
		// TODO 重定向到指定页面
		// writeLocal(resp,
		// "取现成功，5秒后自动跳转到我的帐户<a href='/views/google/myaccount.html?fid=cash&sid=cash-withdraw'>我的帐户</a>");
	}

	@RequestMapping(value = { "/account/buy/request" })
	public void buy(HttpServletRequest req, HttpServletResponse resp) {
		String pid = req.getParameter("pid");
		HttpSession session = req.getSession();
		Object user = session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		if (user == null) {
			try {
				resp.sendError(403, "未找到用户信息，请重新登录");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		Integer submitId = Integer.parseInt(StringUtil.checkNullAndTrim(SUBMITID, req.getParameter(SUBMITID)));
		Submit submit = ObjectUtil.checkNullObject(Submit.class, submitService.find(submitId));
		Integer cashStreamId = null;
		try {
			cashStreamId = accountService.freezeLenderAccount(((Lender) user).getAccountId(), submit.getAmount(), submitId, "购买");
		} catch (InsufficientBalanceException e) {
			try {
				resp.sendError(400, "余额不足");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.debug(e.getMessage(), e);
			return;
		}
		log.debug("购买：amount=" + submit.getAmount() + ",cashStreamId=" + cashStreamId);
		log.debug("跳转到第三方进行购买");
		// log.debug("第三方购买完毕，跳转回本地");
		// try {
		// resp.sendRedirect("/account/buy/response?cashStreamId="+cashStreamId);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		String html = "";
		if (pid != null) {
			html = "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台付款</h3>" + "说明：该步骤跳转到第三方平台,用户输入第三方平台账户密码完成购买.<p><a href='/account/buy/response?cashStreamId=" + cashStreamId + "&success=true&pid=" + pid + "'>购买成功</a></p>" + "<p><a href='/account/buy/response?cashStreamId=" + cashStreamId + "&success=false&pid=" + pid + "'>购买失败</a></p></div>";
		} else {
			html = "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台付款</h3>" + "说明：该步骤跳转到第三方平台,用户输入第三方平台账户密码完成购买.<p><a href='/account/buy/response?cashStreamId=" + cashStreamId + "&success=true'>购买成功</a></p>" + "<p><a href='/account/buy/response?cashStreamId=" + cashStreamId + "&success=false'>购买失败</a></p></div>";
		}

		writeThirdParty(resp, html);
	}

	@RequestMapping(value = { "/account/buy/response" })
	public void completeBuy(HttpServletRequest req, HttpServletResponse resp) {
		String pid = req.getParameter("pid");
		Integer cashStreamId = Integer.parseInt(StringUtil.checkNullAndTrim(CASHSTREAMID, req.getParameter(CASHSTREAMID)));
		boolean success = Boolean.parseBoolean(StringUtil.checkNullAndTrim(CASHSTREAMID, req.getParameter("success")));
		if (success) {
			try {
				log.debug("购买成功");
				CashStream cashStream = cashStreamDao.find(cashStreamId);
				submitService.confirmBuy(cashStream.getSubmitId());
				accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
			} catch (IllegalConvertException e) {
				log.error(e.getMessage(), e);
			}
			// TODO 重定向到指定页面
			if (pid != null)
				writeLocal(resp, "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台付款</h3>购买成功，<p><a href='/views/google/productdetail.html?pid=" + pid + "'>继续购买</a></p><a href='/views/google/myaccount.html'>返回我的帐户</a></div>");
			else {
				writeLocal(resp, "<div style='width:100%; text-align:center; margin-top:20px; color:#0697da;'><h3>第三方平台付款</h3>购买成功，<p><a href='/views/google/myaccount.html'>返回我的帐户</a></p></div>");
			}
		} else {
			try {
				log.debug("购买失败");
				accountService.changeCashStreamState(cashStreamId, CashStream.STATE_FAIL);
			} catch (IllegalConvertException e) {
				e.printStackTrace();
			}
			write(resp, "<head><script>window.location.href='/views/google/myaccount.html?fid=submit&sid=submit-toafford'</script></head>");
		}

	}

	@RequestMapping(value = { "/account/repay/request" })
	public void repay(HttpServletRequest req, HttpServletResponse resp) {
		// HttpSession session=req.getSession();
		// Object
		// user=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		// if(user==null)
		// {
		// try {
		// resp.sendError(403,"未找到用户信息，请重新登录");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// return;
		// }
		// 测试暂时借款人账户ID不从Session取,而从payback中取
		Integer payBackId = Integer.parseInt(StringUtil.checkNullAndTrim(PAYBACKID, req.getParameter(PAYBACKID)));
		PayBack payBack = payBackService.find(payBackId);
		Product currentProduct = productService.find(payBack.getProductId());
		if (currentProduct.getState() != Product.STATE_REPAYING) {
			write(resp, "该产品尚未进入还款阶段");
			return;
		}
		// 验证还款顺序
		List<Product> products = productService.findByGovermentOrder(currentProduct.getGovermentorderId());
		for (Product product : products) {
			if (product.getId() == (int) (currentProduct.getId())) {
				List<PayBack> payBacks = payBackService.findAll(product.getId());
				for (PayBack pb : payBacks) {
					if (pb.getState() == PayBack.STATE_FINISHREPAY || pb.getState() == PayBack.STATE_REPAYING)
						continue;
					if (pb.getDeadline() < payBack.getDeadline()) {
						write(resp, "请按时间顺序进行还款");
						return;
					}
				}
				continue;
			}
			product.setProductSeries(productSeriesDao.find(product.getProductseriesId()));
			if (product.getProductSeries().getType() < currentProduct.getProductSeries().getType()) {
				List<PayBack> payBacks = payBackService.findAll(product.getId());
				for (PayBack pb : payBacks) {
					if (pb.getState() == PayBack.STATE_FINISHREPAY || pb.getState() == PayBack.STATE_REPAYING)
						continue;
					if (pb.getDeadline() <= payBack.getDeadline()) {
						write(resp, "请先还完稳健型/平衡型产品再进行此次还款");
						return;
					}
				}
			}
		}

		Integer cashStreamId = null;
		try {
			cashStreamId = accountService.freezeBorrowerAccount(payBack.getBorrowerAccountId(), payBack.getChiefAmount().add(payBack.getInterest()), payBack.getId(), "冻结");
		} catch (InsufficientBalanceException e) {
			try {
				resp.sendError(400, "余额不足");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.debug(e.getMessage(), e);
			return;
		}
		log.debug("还款：amount=" + payBack.getChiefAmount().add(payBack.getInterest()) + ",cashStreamId=" + cashStreamId);
		log.debug("跳转到第三方进行还款");
		writeThirdParty(resp, "说明：该步骤跳转到第三方平台,借款人输入第三方平台账户密码完成还款.<p><a href='/account/repay/response?cashStreamId=" + cashStreamId + "'>还款成功</a><p><a href='javascript:window.close()'>还款失败</a>");
	}

	@RequestMapping(value = { "/account/repay/response" })
	public void completeRepay(HttpServletRequest req, HttpServletResponse resp) {
		Integer cashStreamId = Integer.parseInt(StringUtil.checkNullAndTrim("cashStreamId", req.getParameter(CASHSTREAMID)));
		try {
			log.debug("购买成功");
			CashStream cashStream = cashStreamDao.find(cashStreamId);
			// 增加还款任务
			Task task = new Task();
			task.setCreateTime(System.currentTimeMillis());
			task.setPayBackId(cashStream.getPaybackId());
			PayBack payBack = payBackService.find(cashStream.getPaybackId());
			task.setProductId(payBack.getProductId());
			task.setState(Task.STATE_INIT);
			task.setType(Task.TYPE_REPAY);
			taskService.submit(task);
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
			accountService.unfreezeBorrowerAccount(cashStream.getBorrowerAccountId(), cashStream.getChiefamount(), cashStream.getPaybackId(), "解冻");
			payBackService.changeState(cashStream.getPaybackId(), PayBack.STATE_FINISHREPAY);
			if (payBack.getType() == PayBack.TYPE_LASTPAY) {
				// TODO 金额正确，设置产品状态为还款完毕
				productService.finishRepay(payBack.getProductId());
				Product product = productService.find(payBack.getProductId());
				List<Product> products = productService.findByGovermentOrder(product.getGovermentorderId());
				boolean isAllRepay = true;
				for (Product pro : products) {
					if (pro.getState() != Product.STATE_FINISHREPAY) {
						isAllRepay = false;
						break;
					}
				}
				if (isAllRepay)
					orderService.closeFinancing(product.getGovermentorderId());
			}
		} catch (IllegalConvertException e) {
			log.error(e.getMessage(), e);
		}
		// TODO 重定向到指定页面
		write(resp, "还款成功，返回管理页面<a href='/views/google/admin.html'>返回</a>");
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
		try {
			req.setCharacterEncoding("UTF-8");
			Map<String, String> params = new HashMap<String, String>();
			Map m = req.getParameterMap();
			Iterator it = m.keySet().iterator();
			while (it.hasNext()) {
				String key = String.valueOf(it.next());
				String[] values = (String[]) (m.get(key));
				String value=values[0];
				log.info(key + "=" + value);
				params.put(key, value);
			}
			return params;
		} catch (Throwable e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
}
