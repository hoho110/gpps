package gpps.service.impl;

import static gpps.tools.StringUtil.checkNullAndTrim;
import gpps.constant.Pagination;
import gpps.dao.IBorrowerAccountDao;
import gpps.dao.IBorrowerDao;
import gpps.dao.IFinancingRequestDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.IProductDao;
import gpps.model.Borrower;
import gpps.model.BorrowerAccount;
import gpps.model.FinancingRequest;
import gpps.model.GovermentOrder;
import gpps.model.Product;
import gpps.model.ref.Accessory;
import gpps.model.ref.Accessory.MimeCol;
import gpps.model.ref.Accessory.MimeItem;
import gpps.service.IBorrowerService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.IllegalOperationException;
import gpps.service.exception.LoginException;
import gpps.service.exception.ValidateCodeException;
import gpps.tools.ObjectUtil;
import gpps.tools.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyservice.xml.EasyObjectXMLTransformerImpl;
import com.easyservice.xml.IEasyObjectXMLTransformer;
import com.easyservice.xml.XMLParseException;

@Service
public class BorrowerServiceImpl extends AbstractLoginServiceImpl implements IBorrowerService {
	@Autowired
	IBorrowerDao borrowerDao;
	@Autowired
	IBorrowerAccountDao borrowerAccountDao;
	@Autowired
	IFinancingRequestDao financingRequestDao;
	@Autowired
	IGovermentOrderDao govermentOrderDao;
	@Autowired
	IProductDao productDao;
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
		borrowerDao.changePassword(borrower.getId(), getProcessedPassword(checkNullAndTrim("password", password) + PASSWORDSEED));
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
		if(StringUtil.isDigit(borrower.getLoginId()))
			throw new IllegalArgumentException("登录名不能全部为数字");
//		borrower.setEmail(checkNullAndTrim("email", borrower.getEmail()));
//		borrower.setIdentityCard(checkNullAndTrim("identityCard", borrower.getIdentityCard()));
		borrower.setLicense(checkNullAndTrim("license", borrower.getLicense()));
//		borrower.setName(checkNullAndTrim("name", borrower.getName()));
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
		getCurrentSession().setAttribute(SESSION_ATTRIBUTENAME_USER, borrower);
		return borrower;
	}

	// @Override
	// public Borrower update(Borrower borrower) {
	// borrowerDao.update(borrower);
	// return borrower;
	// }
	static int[][] validConverts = { 
		{ Borrower.PRIVILEGE_VIEW, Borrower.PRIVILEGE_APPLY },
		{ Borrower.PRIVILEGE_REFUSE, Borrower.PRIVILEGE_APPLY },
		{ Borrower.PRIVILEGE_APPLY, Borrower.PRIVILEGE_FINANCING }, 
		{ Borrower.PRIVILEGE_APPLY, Borrower.PRIVILEGE_REFUSE } };

	private void changePrivilege(int id, int privilege) throws IllegalConvertException {
		Borrower borrower = borrowerDao.find(id);
		if (borrower == null)
			return;
		for(int[] validStateConvert:validConverts)
		{
			if(borrower.getPrivilege()==validStateConvert[0]&&privilege==validStateConvert[1])
			{
				borrowerDao.changePrivilege(id, privilege,System.currentTimeMillis());
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
	public List<Borrower> findFinancingBorrower(int privilege) {
		return borrowerDao.findByState(privilege);
	}
	
	@Override
	public List<Borrower> findRequestBorrower() {
		return borrowerDao.findByState(Borrower.PRIVILEGE_APPLY);
	}

	@Override
	public boolean isPhoneNumberExist(String phoneNumber) {
		return borrowerDao.findByTel(phoneNumber) == null ? false : true;
	}

	@Override
	public void applyForFunding() throws IllegalConvertException {
		Borrower borrower=getCurrentUser();
		changePrivilege(borrower.getId(),Borrower.PRIVILEGE_APPLY);
		borrower.setPrivilege(Borrower.PRIVILEGE_APPLY);
	}

	@Override
	public void passFundingApplying(Integer borrowerId) throws IllegalConvertException {
		changePrivilege(borrowerId,Borrower.PRIVILEGE_FINANCING);
	}

	@Override
	public void refuseFundingApplying(Integer borrowerId) throws IllegalConvertException {
		changePrivilege(borrowerId,Borrower.PRIVILEGE_REFUSE);
	}
	@Override
	public Borrower getCurrentUser() {
		HttpSession session=getCurrentSession();
		if(session==null)
			return null;
		Object user=session.getAttribute(SESSION_ATTRIBUTENAME_USER);
		if(user instanceof Borrower)
			return (Borrower)session.getAttribute(SESSION_ATTRIBUTENAME_USER);
		return null;
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
	public void delAccessory(Integer borrowerId, int category, String itemId)
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
			if(items.get(i).getId().equals(itemId))
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

	@Override
	@Transactional
	public void applyFinancing(FinancingRequest financingRequest) {
		Borrower borrower=getCurrentUser();
		ObjectUtil.checkNullObject(Borrower.class, borrower);
		financingRequest.setBorrowerID(borrower.getId());
		financingRequest.setState(FinancingRequest.STATE_INIT);
		financingRequestDao.create(financingRequest);
		if(borrower.getPrivilege()!=Borrower.PRIVILEGE_FINANCING)
		{
			borrowerDao.changePrivilege(borrower.getId(), Borrower.PRIVILEGE_APPLY,System.currentTimeMillis());
			borrower.setPrivilege(Borrower.PRIVILEGE_APPLY);
		}
	}

	@Override
	public FinancingRequest findFinancingRequest(Integer id) {
		FinancingRequest request= financingRequestDao.find(id);
		if(request!=null)
		{
			request.setGovermentOrder(govermentOrderDao.findByFinancingRequest(request.getId()));
			request.setBorrower(borrowerDao.find(request.getBorrowerID()));
		}
		return request;
	}

	@Override
	public List<FinancingRequest> findMyFinancingRequests(int state) {
		Borrower borrower=getCurrentUser();
		ObjectUtil.checkNullObject(Borrower.class, borrower);
		List<FinancingRequest> financingRequests= financingRequestDao.findByBorrowerAndState(borrower.getId(), state);
		for(FinancingRequest request:financingRequests)
		{
			request.setGovermentOrder(govermentOrderDao.findByFinancingRequest(request.getId()));
			request.setBorrower(borrowerDao.find(request.getBorrowerID()));
		}
		return financingRequests;
	}

	@Override
	public Map<String, Object> findAllFinancingRequests(int state, int offset, int recnum) {
		int count=financingRequestDao.countByState(state);
		if(count==0)
			return Pagination.buildResult(new ArrayList<FinancingRequest>(0), count, offset, recnum);
		List<FinancingRequest> financingRequests=financingRequestDao.findByState(state, offset, recnum);
		for(FinancingRequest request:financingRequests)
		{
			request.setGovermentOrder(govermentOrderDao.findByFinancingRequest(request.getId()));
			request.setBorrower(borrowerDao.find(request.getBorrowerID()));
		}
		return Pagination.buildResult(financingRequests, count, offset, recnum);
	}

	@Override
	public void passFinancingRequest(Integer financingRequestId) throws IllegalOperationException{
		FinancingRequest financingRequest=financingRequestDao.find(financingRequestId);
		GovermentOrder order=govermentOrderDao.findByFinancingRequest(financingRequest.getId());
		if(order==null)
			throw new IllegalOperationException("尚未为该融资申请建立订单");
		if(order.getState()!=GovermentOrder.STATE_UNPUBLISH)
			throw new IllegalOperationException("订单状态不为未发布");
		List<Product> products=productDao.findByGovermentOrder(order.getId());
		if(products==null||products.size()==0)
			throw new IllegalOperationException("尚未为该融资申请建立产品");
		govermentOrderDao.changeState(order.getId(), GovermentOrder.STATE_PREPUBLISH, System.currentTimeMillis());
		for(Product product:products)
		{
			productDao.changeState(product.getId(), Product.STATE_FINANCING, System.currentTimeMillis());
		}
		ObjectUtil.checkNullObject(FinancingRequest.class, financingRequest);
		financingRequestDao.changeState(financingRequestId, FinancingRequest.STATE_PROCESSED, System.currentTimeMillis());
//		borrowerDao.changePrivilege(financingRequest.getBorrowerID(), Borrower.PRIVILEGE_FINANCING);
	}

	@Override
	public boolean isIdentityCardExist(String identityCard) {
		return borrowerDao.findByIdentityCard(identityCard)==null?false:true;
	}

	@Override
	public void registerThirdPartyAccount(Integer id,String thirdPartyAccount) {
		thirdPartyAccount=checkNullAndTrim("thirdPartyAccount", thirdPartyAccount);
		borrowerDao.registerThirdPartyAccount(id, thirdPartyAccount);
		Borrower borrower=getCurrentUser();
		if(borrower!=null)
			borrower.setThirdPartyAccount(thirdPartyAccount);
	}

	@Override
	public boolean isThirdPartyAuthentication() {
		Borrower borrower=getCurrentUser();
		return StringUtil.isEmpty(borrower.getThirdPartyAccount())?false:true;
	}

	@Override
	public void refuseFinancingRequest(Integer financingRequestId) {
		FinancingRequest financingRequest=financingRequestDao.find(financingRequestId);
		ObjectUtil.checkNullObject(FinancingRequest.class, financingRequest);
		financingRequestDao.changeState(financingRequestId, FinancingRequest.STATE_REFUSE, (new Date()).getTime());
	}

	@Override
	public Map<String, Object> findByPrivilegeWithPaging(int privilege, int offset,
			int recnum) {
		int count =borrowerDao.countByPrivilege(privilege);
		if(count==0)
			return Pagination.buildResult(null, count, offset, recnum);
		return Pagination.buildResult(borrowerDao.findByPrivilegeWithPaging(privilege, offset, recnum), count, offset, recnum);
	}

	@Override
	public void update(Integer id, String corporationName, String corporationAddr, String brange) {
		borrowerDao.update(id, corporationName, corporationAddr, brange);
	}
	
}
