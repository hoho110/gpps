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
import gpps.service.ILoginService;
import gpps.service.exception.IllegalConvertException;
import gpps.tools.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountServlet {
	@Autowired
	IAccountService accountService;
	Logger log=Logger.getLogger(AccountServlet.class);
	public static final String RECHARGEAMOUNT="rechargeAmount";
	public static final String CASHSTREAMID="cashStreamId";
	@RequestMapping(value={"/account/recharge/request"})
	public void recharge(HttpServletRequest req, HttpServletResponse resp)
	{
		String amount=req.getParameter(RECHARGEAMOUNT);
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
	}
}
