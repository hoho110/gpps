package gpps.service.impl;

import static gpps.tools.StringUtil.checkNullAndTrim;
import gpps.dao.IBorrowerDao;
import gpps.model.Borrower;
import gpps.model.Lender;
import gpps.service.IBorrowerService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.LoginException;
import gpps.service.exception.ValidateCodeException;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowerServiceImpl extends AbstractLoginServiceImpl implements IBorrowerService {
	@Autowired
	IBorrowerDao borrowerDao;

	@Override
	public void login(String loginId, String password, String graphValidateCode) throws LoginException, ValidateCodeException {
		checkGraphValidateCode(graphValidateCode);
		HttpSession session = getCurrentSession();
		loginId = checkNullAndTrim("loginId", loginId);
		password = getProcessedPassword(checkNullAndTrim("password", password) + PASSWORDSEED);
		Borrower borrower = borrowerDao.findByLoginIdAndPassword(loginId, password);
		if (borrower == null)
			throw new LoginException("Login fail!!");
		session.setAttribute(SESSION_ATTRIBUTENAME_USER, borrower);
	}

	@Override
	public void changePassword(String loginId, String password, String messageValidateCode) throws ValidateCodeException, LoginException {
		checkMessageValidateCode(messageValidateCode);
		Borrower borrower = borrowerDao.findByLoginId(loginId);
		if (borrower == null)
			throw new LoginException("LoginId is not existed");
		borrowerDao.changePassword(borrower.getId(), password);
	}

	@Override
	public boolean isLoginIdExist(String loginId) {
		Borrower borrower = borrowerDao.findByLoginId(loginId);
		return borrower == null ? false : true;
	}

	@Override
	public String getProcessedTel(String loginId) {
		Borrower borrower = borrowerDao.findByLoginId(loginId);
		if (borrower == null)
			return null;
		String tel = borrower.getTel();
		char[] processedTel = tel.toCharArray();
		for (int i = 4; i < 8; i++) {
			processedTel[i] = '*';
		}
		return String.valueOf(processedTel);
	}

	@Override
	public Borrower register(Borrower borrower, String messageValidateCode) throws ValidateCodeException, IllegalArgumentException, LoginException {
		checkMessageValidateCode(messageValidateCode);
		borrower.setLoginId(checkNullAndTrim("loginId", borrower.getLoginId()));
		borrower.setEmail(checkNullAndTrim("email", borrower.getEmail()));
		borrower.setIdentityCard(checkNullAndTrim("identityCard", borrower.getIdentityCard()));
		borrower.setName(checkNullAndTrim("name", borrower.getName()));
		borrower.setPassword(getProcessedPassword(checkNullAndTrim("password", borrower.getPassword()) + PASSWORDSEED));
		borrower.setCreatetime(System.currentTimeMillis());
		borrower.setPrivilege(borrower.PRIVILEGE_VIEW);
		borrower.setTel(checkNullAndTrim("tel", borrower.getTel()));
		if (borrowerDao.findByLoginId(borrower.getLoginId()) != null)
			throw new LoginException("LoginId is existed");
		borrowerDao.create(borrower);
		borrower.setPassword(null);
		return borrower;
	}

	// @Override
	// public Borrower update(Borrower borrower) {
	// borrowerDao.update(borrower);
	// return borrower;
	// }
	static int[][] validConverts = { 
		{ Borrower.PRIVILEGE_VIEW, Borrower.PRIVILEGE_APPLY }, 
		{ Borrower.PRIVILEGE_APPLY, Borrower.PRIVILEGE_FINANCING }, 
		{ Borrower.PRIVILEGE_APPLY, Borrower.PRIVILEGE_VIEW } };

	private void changePrivilege(int id, int privilege) throws IllegalConvertException {
		Borrower borrower = borrowerDao.find(id);
		if (borrower == null)
			return;
		for(int[] validStateConvert:validConverts)
		{
			if(borrower.getPrivilege()==validStateConvert[0]&&privilege==validStateConvert[1])
			{
				borrowerDao.changePrivilege(id, privilege);
				return;
			}
		}
		throw new IllegalConvertException();
	}

	@Override
	public Borrower find(int id) {
		return borrowerDao.find(id);
	}

	@Override
	public List<Borrower> findFinancingBorrower() {
		return borrowerDao.findByState(Borrower.PRIVILEGE_FINANCING);
	}

	@Override
	public void addAccessory(Integer borrowerId, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPhoneNumberExist(String phoneNumber) {
		return borrowerDao.findByTel(phoneNumber) == null ? false : true;
	}

	@Override
	public void applyForFunding() throws IllegalConvertException {
		Borrower borrower=getCurrentUser();
		changePrivilege(borrower.getId(),Borrower.PRIVILEGE_APPLY);
	}

	@Override
	public void passFundingApplying(Integer borrowerId) throws IllegalConvertException {
		changePrivilege(borrowerId,Borrower.PRIVILEGE_FINANCING);

	}

	@Override
	public void refuseFundingApplying(Integer borrowerId) throws IllegalConvertException {
		changePrivilege(borrowerId,Borrower.PRIVILEGE_VIEW);
	}
	@Override
	public Borrower getCurrentUser() {
		HttpSession session=getCurrentSession();
		if(session==null)
			return null;
		return (Borrower)session.getAttribute(SESSION_ATTRIBUTENAME_USER);
	}

}
