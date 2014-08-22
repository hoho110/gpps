package gpps.service.impl;

import static gpps.tools.ObjectUtil.checkNullObject;
import static gpps.tools.StringUtil.checkNullAndTrim;
import gpps.service.ILoginService;
import gpps.service.exception.ValidateCodeException;
import gpps.tools.GraphValidateCode;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public abstract class AbstractLoginServiceImpl implements ILoginService {
	@Override
	public void loginOut() {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		session.removeAttribute(SESSION_ATTRIBUTENAME_USER);
	}

	@Override
	public void sendMessageValidateCode() {
		//TODO 发送短信
	}

	@Override
	public void writeGraphValidateCode(OutputStream os)throws IOException {
		GraphValidateCode validateCode=new GraphValidateCode(160, 40, 5, 150);
		getCurrentSession().setAttribute(SESSION_ATTRIBUTENAME_GRAPHVALIDATECODE, validateCode.getCode());
		validateCode.write(os);
	}
	public HttpSession getCurrentSession()
	{
		 HttpSession session=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		 return session;
	}
	protected void checkMessageValidateCode(String messageValidateCode) throws ValidateCodeException
	{
		HttpSession session =getCurrentSession();
		messageValidateCode=checkNullAndTrim("messageValidateCode", messageValidateCode);
		String originalMessageValidateCode=String.valueOf(checkNullObject("originalMessageValidateCode", session.getAttribute(SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODE)));
		session.removeAttribute(SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODE);
		long messageValidateCodeSendTime=(Long)session.getAttribute(SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODESENDTIME);
		session.removeAttribute(SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODESENDTIME);
		if(!originalMessageValidateCode.equals(messageValidateCode))
			throw new ValidateCodeException("GraphValidateCodes do not match");
		if(messageValidateCodeSendTime+MESSAGEVALIDATECODEEXPIRETIME<System.currentTimeMillis())
			throw new ValidateCodeException("Overdue");
	}
	protected void checkGraphValidateCode(String graphValidateCode) throws ValidateCodeException
	{
		HttpSession session =getCurrentSession();
		graphValidateCode=checkNullAndTrim("graphValidateCode", graphValidateCode);
		String originalGraphValidateCode=String.valueOf(checkNullObject("originalGraphValidateCode", session.getAttribute(SESSION_ATTRIBUTENAME_GRAPHVALIDATECODE)));
		session.removeAttribute(SESSION_ATTRIBUTENAME_GRAPHVALIDATECODE);//用过一次即删除
		if(!originalGraphValidateCode.equals(graphValidateCode))
			throw new ValidateCodeException("GraphValidateCodes do not match");
	}
	protected String getProcessedPassword(String password)
	{
		return DigestUtils.md5Hex(checkNullAndTrim("password", password)+PASSWORDSEED);
	}
}
