package gpps.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;

import gpps.dao.ICashStreamDao;
import gpps.dao.IPayBackDao;
import gpps.dao.ISubmitDao;
import gpps.model.Borrower;
import gpps.model.CashStream;
import gpps.model.Lender;
import gpps.model.PayBack;
import gpps.model.Submit;
import gpps.model.Task;
import gpps.service.IAccountService;
import gpps.service.ILenderService;
import gpps.service.ILoginService;
import gpps.service.IPayBackService;
import gpps.service.ISubmitService;
import gpps.service.ITaskService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.InsufficientBalanceException;
import gpps.tools.ObjectUtil;
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
	@Autowired
	ISubmitService submitService;
	@Autowired
	ICashStreamDao cashStreamDao;
	@Autowired
	IPayBackService payBackService;
	@Autowired
	ITaskService taskService;
	Logger log=Logger.getLogger(AccountServlet.class);
	public static final String AMOUNT="amount";
	public static final String CASHSTREAMID="cashStreamId";
	public static final String SUBMITID="submitId";
	public static final String PAYBACKID="paybackId";
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
		write(resp, "第三方注册成功，转向我的账户页面");
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
		write(resp, "充值成功，转向我的账户页面");
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
		write(resp, "取现成功，转向我的账户页面");
	}
	@RequestMapping(value={"/account/buy/request"})
	public void buy(HttpServletRequest req, HttpServletResponse resp)
	{
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
		Integer submitId=Integer.parseInt(StringUtil.checkNullAndTrim(SUBMITID, req.getParameter(SUBMITID)));
		Submit submit=ObjectUtil.checkNullObject(Submit.class,submitService.find(submitId));
		Integer cashStreamId=null;
		try {
			cashStreamId = accountService.freezeLenderAccount(((Lender)user).getAccountId(), submit.getAmount(), submitId, "购买");
		} catch (InsufficientBalanceException e) {
			try {
				resp.sendError(400,"余额不足");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.debug(e.getMessage(),e);
			return;
		}
		log.debug("购买：amount="+submit.getAmount()+",cashStreamId="+cashStreamId);
		log.debug("跳转到第三方进行购买");
		log.debug("第三方购买完毕，跳转回本地");
		try {
			resp.sendRedirect("/account/buy/response?cashStreamId="+cashStreamId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value={"/account/buy/response"})
	public void completeBuy(HttpServletRequest req, HttpServletResponse resp)
	{
		Integer cashStreamId=Integer.parseInt(StringUtil.checkNullAndTrim(CASHSTREAMID, req.getParameter(CASHSTREAMID)));
		try {
			log.debug("购买成功");
			CashStream cashStream=cashStreamDao.find(cashStreamId);
			submitService.confirmBuy(cashStream.getSubmitId());
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		} catch (IllegalConvertException e) {
			log.error(e.getMessage(),e);
		}
		//TODO 重定向到指定页面
		write(resp, "购买成功，转向我的订单页面");
	}
	@RequestMapping(value={"/account/repay/request"})
	public void repay(HttpServletRequest req, HttpServletResponse resp)
	{
//		HttpSession session=req.getSession();
//		Object user=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
//		if(user==null)
//		{
//			try {
//				resp.sendError(403,"未找到用户信息，请重新登录");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return;
//		}
		//测试暂时借款人账户ID不从Session取,而从payback中取
		Integer payBackId=Integer.parseInt(StringUtil.checkNullAndTrim(PAYBACKID, req.getParameter(PAYBACKID)));
		PayBack payBack=payBackService.find(payBackId);
		
		
		Integer cashStreamId=null;
		try {
			cashStreamId = accountService.freezeBorrowerAccount(payBack.getBorrowerAccountId(), payBack.getChiefAmount().add(payBack.getInterest()), payBack.getId(), "还款");
		} catch (InsufficientBalanceException e) {
			try {
				resp.sendError(400,"余额不足");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.debug(e.getMessage(),e);
			return;
		}
		log.debug("还款：amount="+payBack.getChiefAmount().add(payBack.getInterest())+",cashStreamId="+cashStreamId);
		log.debug("跳转到第三方进行还款");
		log.debug("第三方还款完毕，跳转回本地");
		try {
			resp.sendRedirect("/account/repay/response?cashStreamId="+cashStreamId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value={"/account/repay/response"})
	public void completeRepay(HttpServletRequest req, HttpServletResponse resp)
	{
		Integer cashStreamId=Integer.parseInt(StringUtil.checkNullAndTrim("cashStreamId", req.getParameter(CASHSTREAMID)));
		try {
			log.debug("购买成功");
			CashStream cashStream=cashStreamDao.find(cashStreamId);
			//TODO 增加还款任务
			Task task=new Task();
			task.setCreateTime(System.currentTimeMillis());
			task.setPayBackId(cashStream.getPaybackId());
			PayBack payBack=payBackService.find(cashStream.getPaybackId());
			task.setProductId(payBack.getProductId());
			task.setState(Task.STATE_INIT);
			task.setType(Task.TYPE_REPAY);
			taskService.submit(task);
			accountService.changeCashStreamState(cashStreamId, CashStream.STATE_SUCCESS);
		} catch (IllegalConvertException e) {
			log.error(e.getMessage(),e);
		}
		//TODO 重定向到指定页面
		write(resp, "还款成功，转向我的账户页面");
	}
	private void write(HttpServletResponse resp,String message)
	{
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("utf-8");
		PrintWriter writer=null;
		try {
			writer=resp.getWriter();
			writer.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}finally
		{
			if(writer!=null)
			{
				writer.flush();
				writer.close();
			}
		}
		
	}
}
