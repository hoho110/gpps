package gpps.service.impl;

import static gpps.tools.StringUtil.checkNullAndTrim;
import gpps.dao.IBorrowerAccountDao;
import gpps.dao.IBorrowerDao;
import gpps.model.Borrower;
import gpps.model.BorrowerAccount;
import gpps.model.ref.Accessory;
import gpps.model.ref.Accessory.MimeCol;
import gpps.model.ref.Accessory.MimeItem;
import gpps.service.IBorrowerService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.LoginException;
import gpps.service.exception.ValidateCodeException;
import gpps.tools.StringUtil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyservice.xml.EasyObjectXMLTransformerImpl;
import com.easyservice.xml.IEasyObjectXMLTransformer;
import com.easyservice.xml.XMLParseException;

@Service
public class BorrowerServiceImpl extends AbstractLoginServiceImpl implements IBorrowerService {
	@Autowired
	IBorrowerDao borrowerDao;
	@Autowired
	IBorrowerAccountDao borrowerAccountDao;
	private static final IEasyObjectXMLTransformer xmlTransformer=new EasyObjectXMLTransformerImpl(); 
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
		borrower.setCreditValue(0);
		if(isLoginIdExist(borrower.getLoginId()))
			throw new LoginException("LoginId is existed");
		if(isPhoneNumberExist(borrower.getTel()))
			throw new LoginException("Tel is existed");
		BorrowerAccount account=new BorrowerAccount();
		borrowerAccountDao.create(account);
		borrower.setAccountId(account.getId());
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

	public void addAccessory(Integer borrowerId,int category,MimeItem item) throws XMLParseException
	{
		String text=borrowerDao.findAccessory(borrowerId);
		Accessory accessory=null;
		if(StringUtil.isEmpty(text))
			accessory=new Accessory();
		else {
			accessory=xmlTransformer.parse(text, Accessory.class);
		}
		if(accessory.getCols()==null)
			accessory.setCols(new ArrayList<Accessory.MimeCol>());
		MimeCol col=accessory.findMimeCol(category);
		if(col==null)
		{
			col=new MimeCol();
			col.setCategory(category);
			accessory.getCols().add(col);
		}
		if(col.getItems()==null)
			col.setItems(new ArrayList<Accessory.MimeItem>());
		col.getItems().add(item);
		text=xmlTransformer.export(accessory);
		borrowerDao.updateAccessory(borrowerId, text);
	}
	@Override
	public void delAccessory(Integer borrowerId, int category, String path)
			throws XMLParseException {
		String text=borrowerDao.findAccessory(borrowerId);
		if(StringUtil.isEmpty(text))
			return;
		Accessory accessory=xmlTransformer.parse(text, Accessory.class);
		if(accessory.getCols()==null)
			return;
		MimeCol col=accessory.findMimeCol(category);
		if(col==null)
			return;
		List<MimeItem> items=col.getItems();
		if(items==null||items.size()==0)
			return;
		for(int i=0;i<items.size();i++)
		{
			if(items.get(i).getPath().equals(path))
			{
				items.remove(i);
				break;
			}
		}
		text=xmlTransformer.export(accessory);
		borrowerDao.updateAccessory(borrowerId, text);
	}
	@Override
	public List<MimeItem> findMimeItems(Integer borrowerId, int category)
			throws XMLParseException {
		String text=borrowerDao.findAccessory(borrowerId);
		if(StringUtil.isEmpty(text))
			return new ArrayList<Accessory.MimeItem>(0);
		Accessory accessory=xmlTransformer.parse(text, Accessory.class);
		MimeCol col=accessory.findMimeCol(category);
		if(col==null)
			return new ArrayList<Accessory.MimeItem>(0);
		return col.getItems();
	}
}
