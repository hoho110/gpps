package gpps.servlet;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gpps.model.Borrower;
import gpps.model.CashStream;
import gpps.model.Lender;
import gpps.service.IAccountService;
import gpps.service.ILenderService;
import gpps.service.ILoginService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.InsufficientBalanceException;
import gpps.tools.StringUtil;

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
	Logger log=Logger.getLogger(AccountServlet.class);
	public static final String AMOUNT="amount";
	public static final String CASHSTREAMID="cashStreamId";
	@RequestMapping(value={"/account/thirdPartyRegist/request"})
	public void thirdPartyRegist(HttpServletRequest req, HttpServletResponse resp)
	{
		//TODO 重定向到第三方注册
		log.debug("第三方注册完毕，跳转回本地");
		try {
			resp.sendRedirect("/account/thirdPartyRegist/response");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value={"/account/thirdPartyRegist/response"})
	public void completeThirdPartyRegist(HttpServletRequest req, HttpServletResponse resp)
	{
		//TODO 第三方注册回调
		String thirdPartyAccount="thirdPartyAccount";
		lenderService.registerThirdPartyAccount(thirdPartyAccount);
		//TODO 重定向到指定页面
	}
	@RequestMapping(value={"/account/recharge/request"})
	public void recharge(HttpServletRequest req, HttpServletResponse resp)
	{
		String amount=req.getParameter(AMOUNT);
		HttpSession session=req.getSession();
		Object user=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		if(user==null)
		{
			try {
				resp.sendError(403,"未找到用户信息，请重新登录");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		Integer cashStreamId=null;
		if(user instanceof Lender)
		{
			Lender lender=(Lender)user;
			cashStreamId=accountService.rechargeLenderAccount(lender.getAccountId(),BigDecimal.valueOf(Double.valueOf(amount)), "充值");
		}
		else if(user instanceof Borrower)
		{
			Borrower borrower=(Borrower)user;
			cashStreamId=accountService.rechargeLenderAccount(borrower.getAccountId(),BigDecimal.valueOf(Double.valueOf(amount)), "充值");
		}
		log.debug("充值：amount="+amount+",cashStreamId="+cashStreamId);
		log.debug("跳转到第三方进行充值");
		log.debug("第三方充值完毕，跳转回本地");
		try {
			resp.sendRedirect("/account/recharge/response?cashStreamId="+cashStreamId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value={"/account/recharge/response"})
	public void completeRecharge(HttpServletRequest req, HttpServletResponse resp)
	{
		Integer cashStreamId=Integer.parseInt(StringUtil.checkNullAndTrim("cashStreamId", req.getParameter(CASHSTREAMID)));
		try {
			log.debug("充值成功");
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		} catch (IllegalConvertException e) {
			log.error(e.getMessage(),e);
		}
		//TODO 重定向到指定页面
	}
	@RequestMapping(value={"/account/cash/request"})
	public void cash(HttpServletRequest req, HttpServletResponse resp)
	{
		String amount=req.getParameter(AMOUNT);
		HttpSession session=req.getSession();
		Object user=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		if(user==null)
		{
			try {
				resp.sendError(403,"未找到用户信息，请重新登录");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		Integer cashStreamId=null;
		try {
			if(user instanceof Lender)
			{
				Lender lender=(Lender)user;
					cashStreamId=accountService.cashLenderAccount(lender.getAccountId(),BigDecimal.valueOf(Double.valueOf(amount)), "提现");
			}
			else if(user instanceof Borrower)
			{
				Borrower borrower=(Borrower)user;
				cashStreamId=accountService.cashBorrowerAccount(borrower.getAccountId(),BigDecimal.valueOf(Double.valueOf(amount)), "提现");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InsufficientBalanceException e) {
			try {
				resp.sendError(400,"余额不足");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.debug(e.getMessage(),e);
			return;
		}
		log.debug("取现：amount="+amount+",cashStreamId="+cashStreamId);
		log.debug("跳转到第三方进行取现");
		log.debug("第三方取现完毕，跳转回本地");
		try {
			resp.sendRedirect("/account/cash/response?cashStreamId="+cashStreamId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value={"/account/cash/response"})
	public void completeCash(HttpServletRequest req, HttpServletResponse resp)
	{
		Integer cashStreamId=Integer.parseInt(StringUtil.checkNullAndTrim("cashStreamId", req.getParameter(CASHSTREAMID)));
		try {
			log.debug("取现成功");
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		} catch (IllegalConvertException e) {
			log.error(e.getMessage(),e);
		}
		//TODO 重定向到指定页面
	}
}
