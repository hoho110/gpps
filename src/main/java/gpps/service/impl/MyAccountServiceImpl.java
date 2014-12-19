package gpps.service.impl;

import gpps.dao.IBorrowerAccountDao;
import gpps.dao.IBorrowerDao;
import gpps.dao.ICashStreamDao;
import gpps.dao.IFinancingRequestDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.IHelpDao;
import gpps.dao.ILenderAccountDao;
import gpps.dao.ILenderDao;
import gpps.dao.ILetterDao;
import gpps.dao.IPayBackDao;
import gpps.model.Admin;
import gpps.model.Borrower;
import gpps.model.BorrowerAccount;
import gpps.model.CardBinding;
import gpps.model.CashStream;
import gpps.model.FinancingRequest;
import gpps.model.GovermentOrder;
import gpps.model.Help;
import gpps.model.Lender;
import gpps.model.Letter;
import gpps.model.PayBack;
import gpps.service.IAccountService;
import gpps.service.IBorrowerService;
import gpps.service.IGovermentOrderService;
import gpps.service.IHelpService;
import gpps.service.ILenderService;
import gpps.service.ILetterService;
import gpps.service.ILoginService;
import gpps.service.IMyAccountService;
import gpps.service.INoticeService;
import gpps.service.IPayBackService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class MyAccountServiceImpl implements IMyAccountService {
	@Autowired
	ILenderService lenderService;
	@Autowired
	ILenderDao lenderDao;
	@Autowired
	ILenderAccountDao lenderAccountDao;
	@Autowired
	IBorrowerService borrowerService;
	@Autowired
	IBorrowerDao borrowerDao;
	@Autowired
	IBorrowerAccountDao borrowerAccountDao;
	@Autowired
	ILetterDao letterDao;
	@Autowired
	IHelpService helpService;
	@Autowired
	IHelpDao helpDao;
	@Autowired
	IGovermentOrderService orderService;
	@Autowired
	IPayBackService paybackService;
	@Autowired
	IPayBackDao paybackDao;
	@Autowired
	IAccountService accountService;
	@Autowired
	IFinancingRequestDao requestDao;
	@Autowired
	ICashStreamDao cashStreamDao;
	
	
	@Override
	public Map<String, Object> getCurrentUser(){
		Map<String, Object> res = new HashMap<String, Object>();
		HttpSession session=lenderService.getCurrentSession();
		if(session==null)
			return null;
		Object user=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		
		if(user instanceof Lender){
			res.put("usertype", "lender");
			res.put("letter", letterDao.countByReceiver(Letter.MARKREAD_NO, Letter.RECEIVERTYPE_LENDER, ((Lender)user).getId()));
			
		}else if(user instanceof Borrower){
			res.put("usertype", "borrower");
			res.put("letter", letterDao.countByReceiver(Letter.MARKREAD_NO, Letter.RECEIVERTYPE_BORROWER, ((Borrower)user).getId()));
		}else if(user instanceof Admin){
			res.put("usertype", "admin");
		}else{
			res.put("usertype", null);
		}
		res.put("value", user);
		
		return res;
	}
	
	@Override
	public Map<String, Object> getBAccountMessage() {
		Map<String, Object> message = new HashMap<String, Object>();
		Borrower borrower = borrowerService.getCurrentUser();
		BorrowerAccount account = borrowerAccountDao.find(borrower.getAccountId());
		message.put("name", borrower.getLoginId());
		message.put("companyname", borrower.getCompanyName());
		message.put("license", borrower.getLicense());
		message.put("total", account.getTotal());
		message.put("freeze", account.getFreeze());
		message.put("usable", account.getUsable());
		
		CardBinding cb = borrower.getCardBinding();
		if(cb!=null)
		{
			message.put("bankname", borrower.getCardBinding().getBranchBankName());
			message.put("bankcode", borrower.getCardBinding().getCardNo());
		}else{
			message.put("bankname", null);
			message.put("bankcode", null);
		}
		message.put("qdd", borrower.getAccountNumber());
		message.put("authorize", borrower.getAuthorizeTypeOpen());
		message.put("privilege", borrower.getPrivilege());
		
		message.put("score", borrower.getCreditValue());
		message.put("level", borrower.getLevel());
		
		message.put("letters", letterDao.countByReceiver(Letter.MARKREAD_NO, Letter.RECEIVERTYPE_BORROWER, borrower.getId()));
		
		message.put("helps",  helpDao.countPrivateHelps(-1, Help.QUESTIONERTYPE_BORROWER, borrower.getId()));
		
		
		
		
		message.put("request_init", requestDao.findByBorrowerAndState(borrower.getId(), FinancingRequest.STATE_INIT).size());
		
		message.put("request_processed", requestDao.findByBorrowerAndState(borrower.getId(), FinancingRequest.STATE_PROCESSED).size());
		
		message.put("request_refused", requestDao.findByBorrowerAndState(borrower.getId(), FinancingRequest.STATE_REFUSE).size());
		
		message.put("request_all", requestDao.findByBorrowerAndState(borrower.getId(), -1).size());
		
		
		
		List<GovermentOrder> orders_prepublish = orderService.findBorrowerOrderByStates(GovermentOrder.STATE_PREPUBLISH);
		message.put("orders_prepublish", orders_prepublish.size());
		
		List<GovermentOrder> orders_financing = orderService.findBorrowerOrderByStates(GovermentOrder.STATE_FINANCING);
		message.put("orders_financing", orders_financing.size());
		
		List<GovermentOrder> orders_repaying = orderService.findBorrowerOrderByStates(GovermentOrder.STATE_REPAYING);
		message.put("orders_repaying", orders_repaying.size());
		
		List<GovermentOrder> orders_waitingclose = orderService.findBorrowerOrderByStates(GovermentOrder.STATE_WAITINGCLOSE);
		message.put("orders_waitingclose", orders_waitingclose.size());
		
		List<GovermentOrder> orders_close = orderService.findBorrowerOrderByStates(GovermentOrder.STATE_CLOSE);
		message.put("orders_close", orders_close.size());
		
		
		message.put("pbs_waitforrepay", paybackService.findBorrowerWaitForRepayed().size());
		message.put("pbs_finishrepay", paybackDao.countByBorrowerAndState2(borrower.getAccountId(), PayBack.STATE_FINISHREPAY, -1, -1));
		message.put("pbs_waitforcheck", paybackDao.countByBorrowerAndState2(borrower.getAccountId(), PayBack.STATE_WAITFORCHECK, -1, -1));
		message.put("pbs_canberepayed", paybackService.findBorrowerCanBeRepayedPayBacks().size());
		message.put("pbs_canberepayedinadvance", paybackService.findBorrowerCanBeRepayedInAdvancePayBacks().size());
		
		
		
		message.put("cash_recharge", cashStreamDao.countByActionAndState(null, borrower.getAccountId(), CashStream.ACTION_RECHARGE, CashStream.STATE_SUCCESS));
		message.put("cash_withdraw", cashStreamDao.countByActionAndState(null, borrower.getAccountId(), CashStream.ACTION_CASH, CashStream.STATE_SUCCESS));
		message.put("cash_financing", cashStreamDao.countByActionAndState(null, borrower.getAccountId(), CashStream.ACTION_PAY, CashStream.STATE_SUCCESS));
		message.put("cash_payback", cashStreamDao.countByActionAndState(null, borrower.getAccountId(), CashStream.ACTION_REPAY, CashStream.STATE_SUCCESS));

		return message;
	}

	@Override
	public Map<String, Object> getLAccountMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
