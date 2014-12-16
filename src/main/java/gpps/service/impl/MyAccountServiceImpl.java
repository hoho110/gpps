package gpps.service.impl;

import gpps.dao.IBorrowerAccountDao;
import gpps.dao.IBorrowerDao;
import gpps.dao.ICashStreamDao;
import gpps.dao.ILenderAccountDao;
import gpps.dao.ILenderDao;
import gpps.model.Borrower;
import gpps.model.BorrowerAccount;
import gpps.model.CardBinding;
import gpps.model.FinancingRequest;
import gpps.model.GovermentOrder;
import gpps.model.PayBack;
import gpps.service.IAccountService;
import gpps.service.IBorrowerService;
import gpps.service.IGovermentOrderService;
import gpps.service.IHelpService;
import gpps.service.ILetterService;
import gpps.service.IMyAccountService;
import gpps.service.INoticeService;
import gpps.service.IPayBackService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class MyAccountServiceImpl implements IMyAccountService {
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
	ILetterService letterService;
	@Autowired
	IHelpService helpService;
	@Autowired
	IGovermentOrderService orderService;
	@Autowired
	IPayBackService paybackService;
	@Autowired
	IAccountService accountService;
	
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
		
		Map<String, Object> res = letterService.findMyLetters(0, 0, 1);
		message.put("letters", res.get("total"));
		
		
		Map<String, Object> reshelp = helpService.findMyHelps(-1, 0, 1);
		message.put("helps",  reshelp.get("total"));
		
		
		List<FinancingRequest> requests_init = borrowerService.findMyFinancingRequests(FinancingRequest.STATE_INIT);
		message.put("request_init", requests_init.size());
		
		List<FinancingRequest> request_processed = borrowerService.findMyFinancingRequests(FinancingRequest.STATE_PROCESSED);
		message.put("request_processed", request_processed.size());
		
		List<FinancingRequest> request_refused = borrowerService.findMyFinancingRequests(FinancingRequest.STATE_REFUSE);
		message.put("request_refused", request_refused.size());
		
		message.put("request_all", requests_init.size()+request_processed.size()+request_refused.size());
		
		List<GovermentOrder> orders_prepublish = orderService.findBorrowerOrderByStates(GovermentOrder.STATE_PREPUBLISH);
		if(orders_prepublish==null){
			message.put("orders_prepublish", 0);
		}else{
			message.put("orders_prepublish", orders_prepublish.size());
		}
		
		List<GovermentOrder> orders_financing = orderService.findBorrowerOrderByStates(GovermentOrder.STATE_FINANCING);
		if(orders_financing==null){
			message.put("orders_financing", 0);
		}else{
			message.put("orders_financing", orders_financing.size());
		}
		
		List<GovermentOrder> orders_repaying = orderService.findBorrowerOrderByStates(GovermentOrder.STATE_REPAYING);
		if(orders_repaying==null){
			message.put("orders_repaying", 0);
		}else{
			message.put("orders_repaying", orders_repaying.size());
		}
		
		List<GovermentOrder> orders_waitingclose = orderService.findBorrowerOrderByStates(GovermentOrder.STATE_WAITINGCLOSE);
		if(orders_waitingclose==null){
			message.put("orders_waitingclose", 0);
		}else{
			message.put("orders_waitingclose", orders_waitingclose.size());
		}
		
		List<GovermentOrder> orders_close = orderService.findBorrowerOrderByStates(GovermentOrder.STATE_CLOSE);
		if(orders_waitingclose==null){
			message.put("orders_close", 0);
		}else{
			message.put("orders_close", orders_close.size());
		}
		
		message.put("pbs_waitforrepay", paybackService.findBorrowerPayBacks(PayBack.STATE_WAITFORREPAY, -1, -1, 0, 1).get("total"));
		message.put("pbs_finishrepay", paybackService.findBorrowerPayBacks(PayBack.STATE_FINISHREPAY, -1, -1, 0, 1).get("total"));
		message.put("pbs_waitforcheck", paybackService.findBorrowerPayBacks(PayBack.STATE_WAITFORCHECK, -1, -1, 0, 1).get("total"));
		message.put("pbs_canberepayed", paybackService.findBorrowerCanBeRepayedPayBacks().size());
		message.put("pbs_canberepayedinadvance", paybackService.findBorrowerCanBeRepayedInAdvancePayBacks().size());
		
		
		message.put("cash_recharge", accountService.findBorrowerCashStreamByActionAndState(0,2, 0, 1).get("total"));
		message.put("cash_withdraw", accountService.findBorrowerCashStreamByActionAndState(5,2, 0, 1).get("total"));
		message.put("cash_financing", accountService.findBorrowerCashStreamByActionAndState(3,2, 0, 1).get("total"));
		message.put("cash_payback", accountService.findBorrowerCashStreamByActionAndState(4,2, 0, 1).get("total"));

		return message;
	}

	@Override
	public Map<String, Object> getLAccountMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
