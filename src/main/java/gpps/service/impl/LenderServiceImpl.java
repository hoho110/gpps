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
import gpps.service.ILenderService;
import gpps.service.exception.LoginException;
import gpps.service.exception.ValidateCodeException;
import static gpps.tools.StringUtil.*;
@Service
public class LenderServiceImpl extends AbstractLoginServiceImpl implements ILenderService{
	@Autowired
	ILenderDao lenderDao;
	@Autowired
	ILenderAccountDao lenderAccountDao;
	@Override
	public void login(String loginId, String password, String graphValidateCode)
			throws LoginException, ValidateCodeException {
		HttpSession session =getCurrentSession();
		loginId=checkNullAndTrim("loginId", loginId);
		password=DigestUtils.md5Hex(checkNullAndTrim("password", password)+PASSWORDSEED);
		graphValidateCode=checkNullAndTrim("graphValidateCode", graphValidateCode);
		String originalGraphValidateCode=String.valueOf(session.getAttribute(SESSION_ATTRIBUTENAME_GRAPHVALIDATECODE));
		session.removeAttribute(SESSION_ATTRIBUTENAME_GRAPHVALIDATECODE);//用过一次即删除u
		if(!originalGraphValidateCode.equals(graphValidateCode))
			throw new ValidateCodeException();
		Lender lender=lenderDao.findByLoginIdAndPassword(loginId, password);
		if(lender==null)
			throw new LoginException("Login fail!!");
		session.setAttribute(SESSION_ATTRIBUTENAME_USER, lender);
	}

	@Override
	public void changePassword(String loginId, String password,
			String messageValidateCode) throws ValidateCodeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loginOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessageValidateCode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLoginIdExist(String loginId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getProcessedTel(String loginId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Lender register(Lender lender, String messageValidateCode)
			throws ValidateCodeException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Lender update(Lender lender) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changePrivilege(int id, int privilege)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Lender find(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] findAllPrivilege() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Lender getCurrentUser() {
		HttpSession session=getCurrentSession();
		if(session==null)
			return null;
		return (Lender)session.getAttribute(SESSION_ATTRIBUTENAME_USER);
	}

}
