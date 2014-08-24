/**
 * 
 */
package gpps.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gpps.dao.IBorrowerAccountDao;
import gpps.dao.ICashStreamDao;
import gpps.dao.ILenderAccountDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductDao;
import gpps.dao.ISubmitDao;
import gpps.model.BorrowerAccount;
import gpps.model.CashStream;
import gpps.model.LenderAccount;
import gpps.model.Payback;
import gpps.model.Product;
import gpps.model.Submit;
import gpps.service.IAccountService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.InsufficientBalanceException;
import static gpps.tools.StringUtil.*;
import static gpps.tools.ObjectUtil.*;

/**
 * @author wangm
 *
 */
@Service
public class AccountServiceImpl implements IAccountService {
	@Autowired
	ICashStreamDao cashStreamDao;
	@Autowired
	ILenderAccountDao lenderAccountDao;
	@Autowired
	IBorrowerAccountDao borrowerAccountDao;
	@Autowired
	ISubmitDao submitDao;
	@Autowired
	IPayBackDao payBackDao;
	@Autowired
	IProductDao productDao;
	@Override
	public Integer rechargeLenderAccount(Integer lenderAccountId, BigDecimal amount, String description) {
		checkNullObject(LenderAccount.class, lenderAccountDao.find(lenderAccountId));
		CashStream cashStream = new CashStream();
		cashStream.setLenderAccountId(lenderAccountId);
		cashStream.setChiefamount(amount);
		cashStream.setDescription(description);
		cashStream.setAction(CashStream.ACTION_RECHARGE);
		cashStreamDao.create(cashStream);
		return cashStream.getId();
	}

	@Override
	public Integer rechargeBorrowerAccount(Integer borrowerAccountId, BigDecimal amount, String description) {
		checkNullObject(BorrowerAccount.class, borrowerAccountDao.find(borrowerAccountId));
		CashStream cashStream = new CashStream();
		cashStream.setBorrowerAccountId(borrowerAccountId);
		cashStream.setChiefamount(amount);
		cashStream.setDescription(description);
		cashStream.setAction(CashStream.ACTION_RECHARGE);
		cashStreamDao.create(cashStream);
		return cashStream.getId();
	}

	@Override
	public Integer freezeLenderAccount(Integer lenderAccountId, BigDecimal amount, Integer submitId, String description) throws InsufficientBalanceException {
		LenderAccount lenderAccount=checkNullObject(LenderAccount.class, lenderAccountDao.find(lenderAccountId));
		checkNullObject(Submit.class, submitDao.find(submitId));
		if(amount.compareTo(lenderAccount.getUsable())>0)
			throw new InsufficientBalanceException();
		CashStream cashStream=new CashStream();
		cashStream.setLenderAccountId(lenderAccountId);
		cashStream.setChiefamount(amount);
		cashStream.setSubmitId(submitId);
		cashStream.setDescription(description);
		cashStream.setAction(CashStream.ACTION_FREEZE);
		cashStreamDao.create(cashStream);
		return cashStream.getId();
	}

	@Override
	public Integer freezeBorrowerAccount(Integer borrowerAccountId, BigDecimal amount, Integer paybackId, String description) throws InsufficientBalanceException {
		BorrowerAccount borrowerAccount=checkNullObject(BorrowerAccount.class, borrowerAccountDao.find(borrowerAccountId));
		if(amount.compareTo(borrowerAccount.getUsable())>0)
			throw new InsufficientBalanceException();
		CashStream cashStream=new CashStream();
		cashStream.setBorrowerAccountId(borrowerAccountId);
		cashStream.setChiefamount(amount);
		cashStream.setDescription(description);
		cashStream.setAction(CashStream.ACTION_FREEZE);
		cashStreamDao.create(cashStream);
		return cashStream.getId();
	}
	@Override
	public Integer unfreezeLenderAccount(Integer lenderAccountId, BigDecimal amount, Integer submitId, String description) {
		checkNullObject(LenderAccount.class, lenderAccountDao.find(lenderAccountId));
		CashStream cashStream=new CashStream();
		cashStream.setLenderAccountId(lenderAccountId);
		cashStream.setChiefamount(amount);
		cashStream.setSubmitId(submitId);
		cashStream.setDescription(description);
		cashStream.setAction(CashStream.ACTION_UNFREEZE);
		cashStreamDao.create(cashStream);
		return cashStream.getId();
	}
	@Override
	public Integer pay(Integer lenderAccountId, Integer borrowerAccountId, BigDecimal chiefamount, BigDecimal interest, Integer submitId, String description) {
		checkNullObject(LenderAccount.class, lenderAccountDao.find(lenderAccountId));
		checkNullObject(BorrowerAccount.class, borrowerAccountDao.find(borrowerAccountId));
		checkNullObject(Submit.class, submitDao.find(submitId));
		CashStream cashStream=new CashStream();
		cashStream.setLenderAccountId(lenderAccountId);
		cashStream.setBorrowerAccountId(borrowerAccountId);
		cashStream.setChiefamount(chiefamount);
		cashStream.setInterest(interest);
		cashStream.setSubmitId(submitId);
		cashStream.setDescription(description);
		cashStream.setAction(CashStream.ACTION_PAY);
		cashStreamDao.create(cashStream);
		return cashStream.getId();
	}

	@Override
	public Integer repay(Integer lenderAccountId, Integer borrowerAccountId, BigDecimal chiefamount, BigDecimal interest, Integer submitId, Integer paybackId, String description) {
		checkNullObject(LenderAccount.class, lenderAccountDao.find(lenderAccountId));
		checkNullObject(BorrowerAccount.class, borrowerAccountDao.find(borrowerAccountId));
		checkNullObject(Submit.class, submitDao.find(submitId));
		checkNullObject(Payback.class, paybackId);
		CashStream cashStream=new CashStream();
		cashStream.setLenderAccountId(lenderAccountId);
		cashStream.setBorrowerAccountId(borrowerAccountId);
		cashStream.setChiefamount(chiefamount);
		cashStream.setInterest(interest);
		cashStream.setSubmitId(submitId);
		cashStream.setPaybackId(paybackId);
		cashStream.setDescription(description);
		cashStream.setAction(CashStream.ACTION_REPAY);
		cashStreamDao.create(cashStream);
		return cashStream.getId();
	}

	@Override
	public Integer cashLenderAccount(Integer lenderAccountId, BigDecimal amount, String description) throws InsufficientBalanceException {
		checkNullObject(LenderAccount.class, lenderAccountDao.find(lenderAccountId));
		CashStream cashStream=new CashStream();
		cashStream.setLenderAccountId(lenderAccountId);
		cashStream.setChiefamount(amount);
		cashStream.setDescription(description);
		cashStream.setAction(CashStream.ACTION_CASH);
		cashStreamDao.create(cashStream);
		return cashStream.getId();
	}

	@Override
	public Integer cashBorrowerAccount(Integer borrowerAccountId, BigDecimal amount, String description) throws InsufficientBalanceException {
		checkNullObject(BorrowerAccount.class, borrowerAccountDao.find(borrowerAccountId));
		CashStream cashStream=new CashStream();
		cashStream.setBorrowerAccountId(borrowerAccountId);
		cashStream.setChiefamount(amount);
		cashStream.setDescription(description);
		cashStream.setAction(CashStream.ACTION_CASH);
		cashStreamDao.create(cashStream);
		return cashStream.getId();
	}

	static int[][] validConverts = { 
		{ CashStream.STATE_INIT, CashStream.STATE_FAIL }, 
		{ CashStream.STATE_INIT, CashStream.STATE_SUCCESS }, 
		{ CashStream.STATE_FAIL, CashStream.STATE_SUCCESS } };

	@Override
	@Transactional
	public void changeCashStreamState(Integer cashStreamId, int state) throws IllegalConvertException {
		CashStream cashStream = cashStreamDao.find(cashStreamId);
		checkNullObject(CashStream.class, cashStream);
		for (int[] validStateConvert : validConverts) {
			if (cashStream.getState() == validStateConvert[0] && state == validStateConvert[1]) {
				cashStreamDao.changeCashStreamState(cashStreamId, state);
				if(state!=CashStream.STATE_SUCCESS)
					return;
				switch (cashStream.getAction()) {
				case CashStream.ACTION_RECHARGE:
					if(cashStream.getLenderAccountId()!=null&&cashStream.getBorrowerAccountId()==null)
						lenderAccountDao.recharge(cashStream.getLenderAccountId(), cashStream.getChiefamount());
					else if(cashStream.getBorrowerAccountId()!=null&&cashStream.getLenderAccountId()==null)
						borrowerAccountDao.recharge(cashStream.getBorrowerAccountId(), cashStream.getChiefamount());
					else
						throw new RuntimeException();
					break;
				case CashStream.ACTION_FREEZE:
					if(cashStream.getLenderAccountId()!=null&&cashStream.getBorrowerAccountId()==null)
						lenderAccountDao.freeze(cashStream.getLenderAccountId(), cashStream.getChiefamount());
					else if(cashStream.getBorrowerAccountId()!=null&&cashStream.getLenderAccountId()==null)
						borrowerAccountDao.freeze(cashStream.getBorrowerAccountId(), cashStream.getChiefamount());
					else
						throw new RuntimeException();
					break;
				case CashStream.ACTION_UNFREEZE:
					if(cashStream.getLenderAccountId()!=null&&cashStream.getBorrowerAccountId()==null)
						lenderAccountDao.unfreeze(cashStream.getLenderAccountId(), cashStream.getChiefamount());
					else 
						throw new RuntimeException();
					break;
				case CashStream.ACTION_PAY:
					lenderAccountDao.pay(cashStream.getLenderAccountId(), cashStream.getChiefamount(), cashStream.getInterest());//该利息为期望利息
					borrowerAccountDao.pay(cashStream.getBorrowerAccountId(), cashStream.getChiefamount());
					break;
				case CashStream.ACTION_REPAY:
					//TODO 待确认期待的利益是否一定与实际利息一致
					lenderAccountDao.repay(cashStream.getLenderAccountId(), cashStream.getChiefamount(), cashStream.getInterest(), cashStream.getInterest());
					borrowerAccountDao.repay(cashStream.getBorrowerAccountId(), cashStream.getChiefamount().add(cashStream.getInterest()));
					break;
				case CashStream.ACTION_CASH:
					if(cashStream.getLenderAccountId()!=null&&cashStream.getBorrowerAccountId()==null)
						lenderAccountDao.cash(cashStream.getId(), cashStream.getChiefamount());
					else if(cashStream.getBorrowerAccountId()!=null&&cashStream.getLenderAccountId()==null)
						borrowerAccountDao.cash(cashStream.getId(), cashStream.getChiefamount());
					else
						throw new RuntimeException();
					break;
				default:
					throw new UnsupportedOperationException();
				}
				return;
			}
		}
		throw new IllegalConvertException();
	}

	@Override
	public void checkThroughThirdPlatform(Integer cashStreamId) {
		//TODO 
	}

	@Override
	public List<CashStream> findAllDirtyCashStream() {
		return cashStreamDao.findByState(CashStream.STATE_INIT);
	}

}
