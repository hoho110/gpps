package gpps.service.impl;

import static gpps.tools.ObjectUtil.checkNullObject;
import static gpps.tools.StringUtil.checkNullAndTrim;
import gpps.service.ILoginService;
import gpps.service.exception.FrozenException;
import gpps.service.exception.ValidateCodeException;
import gpps.tools.GraphValidateCode;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

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
	public void sendMessageValidateCode() throws FrozenException{
		//TODO 发送短信
		HttpSession session=getCurrentSession();
		if(session.getAttribute(SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODESENDTIME)!=null)
		{
			if((Long)(session.getAttribute(SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODESENDTIME))+MESSAGEVALIDATECODEINTERVAL>System.currentTimeMillis())
				throw new FrozenException("一分钟内不可重复发送");
		}
			
		String validateCode=getRandomValidateCode(5);
		System.out.println("messageValidateCode="+validateCode);
		session.setAttribute(SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODE, validateCode);
		session.setAttribute(SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODESENDTIME, System.currentTimeMillis());
	}

	@Override
	public void writeGraphValidateCode(OutputStream os)throws IOException {
		GraphValidateCode validateCode=new GraphValidateCode(160, 40, 4, 40);
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
		if(!originalGraphValidateCode.toLowerCase().equals(graphValidateCode.toLowerCase()))
			throw new ValidateCodeException("GraphValidateCodes do not match");
	}
	protected String getProcessedPassword(String password)
	{
		return DigestUtils.md5Hex(checkNullAndTrim("password", password)+PASSWORDSEED);
	}
//	private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
//			'K', 'L', 'M', 'N',  'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
//			'X', 'Y', 'Z','a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
//			'k', 'l', 'm', 'n',  'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
//			'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private char[] codeSequence = {'1', '2', '3', '4', '5', '6', '7', '8', '9','0'};
	private String getRandomValidateCode(int length)
	{
		StringBuffer randomCode = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			randomCode.append(strRand);
		}
		return randomCode.toString();
	}
}
