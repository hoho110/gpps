package gpps.service.impl;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import gpps.dao.ILenderAccountDao;
import gpps.dao.ILenderDao;
import gpps.model.Lender;
import gpps.model.LenderAccount;
import gpps.service.ILenderService;
import gpps.service.exception.LoginException;
import gpps.service.exception.ValidateCodeException;
import gpps.tools.StringUtil;
import static gpps.tools.StringUtil.*;
import static gpps.tools.ObjectUtil.*;
@Service
public class LenderServiceImpl extends AbstractLoginServiceImpl implements ILenderService{
	@Autowired
	ILenderDao lenderDao;
	@Autowired
	ILenderAccountDao lenderAccountDao;
	@Override
	public void login(String loginId, String password, String graphValidateCode)
			throws LoginException, ValidateCodeException {
		checkGraphValidateCode(graphValidateCode);
		HttpSession session =getCurrentSession();
		loginId=checkNullAndTrim("loginId", loginId);
		password=getProcessedPassword(checkNullAndTrim("password", password)+PASSWORDSEED);
		Lender lender=lenderDao.findByLoginIdAndPassword(loginId, password);
		if(lender==null)
			throw new LoginException("Login fail!!");
		session.setAttribute(SESSION_ATTRIBUTENAME_USER, lender);
	}

	@Override
	public void changePassword(String loginId, String password,
			String messageValidateCode) throws LoginException,ValidateCodeException {
		checkMessageValidateCode(messageValidateCode);
		Lender lender=lenderDao.findByLoginId(loginId);
		if(lender==null)
			throw new LoginException("LoginId is not existed");
		lenderDao.changePassword(lender.getId(), password);
	}

	@Override
	public boolean isLoginIdExist(String loginId) {
		Lender lender=lenderDao.findByLoginId(loginId);
		return lender==null?false:true;
	}

	@Override
	public String getProcessedTel(String loginId) {
		Lender lender=lenderDao.findByLoginId(loginId);
		if(lender==null)
			return null;
		String tel=lender.getTel();
		char[] processedTel=tel.toCharArray();
		for(int i=4;i<8;i++)
		{
			processedTel[i]='*';
		}
		return String.valueOf(processedTel);
	}

	@Override
	public Lender register(Lender lender, String messageValidateCode)
			throws ValidateCodeException, IllegalArgumentException, LoginException {
		checkMessageValidateCode(messageValidateCode);
		lender.setLoginId(checkNullAndTrim("loginId", lender.getLoginId()));
		lender.setPassword(getProcessedPassword(checkNullAndTrim("password", lender.getPassword())+PASSWORDSEED));
		lender.setCreatetime(System.currentTimeMillis());
		lender.setPrivilege(Lender.PRIVILEGE_UNOFFICIAL);
		lender.setLevel(Lender.LEVEL_COMMON);
		lender.setTel(checkNullAndTrim("tel", lender.getTel()));
		lender.setGrade(0);
		if(isLoginIdExist(lender.getLoginId()))
			throw new LoginException("LoginId is existed");
		if(isPhoneNumberExist(lender.getTel()))
			throw new LoginException("Tel is existed");
		LenderAccount account=new LenderAccount();
		lenderAccountDao.create(account);
		lender.setAccountId(account.getId());
		lenderDao.create(lender);
		lender.setPassword(null);
		getCurrentSession().setAttribute(SESSION_ATTRIBUTENAME_USER, lender);
		return lender;
	}
	
//	@Override
//	public Lender update(Lender lender) {
//		lenderDao.update(lender);
//		return lender;
//	}

	@Override
	public void changeLevel(int id, int level)
			throws IllegalArgumentException {
		lenderDao.changeLevel(id, level);
	}

	@Override
	public Lender find(int id) {
		return lenderDao.find(id);
	}

	@Override
	public int[] findAllLevel() {
		int[] privileges={Lender.LEVEL_COMMON,Lender.LEVEL_VIP1};
		return privileges;
	}

	@Override
	public Lender getCurrentUser() {
		HttpSession session=getCurrentSession();
		if(session==null)
			return null;
		return (Lender)session.getAttribute(SESSION_ATTRIBUTENAME_USER);
	}

	@Override
	public boolean isPhoneNumberExist(String phoneNumber) {
		Lender lender=lenderDao.findByTel(phoneNumber);
		return lender==null?false:true;
	}

	@Override
	public void registerSecondStep(String name, String identityCard, int sex, String address) throws IllegalArgumentException {
		name=checkNullAndTrim("name", name);
		identityCard=checkNullAndTrim("identityCard", identityCard);
		lenderDao.registerSecondStep(getCurrentUser().getId(), name, identityCard, sex, address);
	}

	@Override
	public void registerThirdPartyAccount(String thirdPartyAccount) {
		Lender lender=getCurrentUser();
		thirdPartyAccount=checkNullAndTrim("thirdPartyAccount", thirdPartyAccount);
		lenderDao.registerThirdPartyAccount(lender.getId(), thirdPartyAccount);
	}

	@Override
	public boolean isIdentityAuthentication() {
		Lender lender=getCurrentUser();
		return StringUtil.isEmpty(lender.getName())||StringUtil.isEmpty(lender.getIdentityCard())?false:true;
	}

	@Override
	public boolean isThirdPartyAuthentication() {
		Lender lender=getCurrentUser();
		return StringUtil.isEmpty(lender.getThirdPartyAccount())?false:true;
	}
}
