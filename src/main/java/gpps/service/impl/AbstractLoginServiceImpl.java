package gpps.service.impl;

import gpps.service.ILoginService;
import gpps.tools.GraphValidateCode;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpSession;

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

}
