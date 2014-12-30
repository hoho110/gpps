package gpps.service.impl;

import gpps.service.exception.SMSException;
import gpps.service.message.Client;
import gpps.service.message.IMessageSupportService;
import gpps.tools.StringUtil;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.client.utils.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
@Service
public class MessageSupportServiceImpl implements IMessageSupportService {
	public static final String CHARSET="UTF-8";
	private String serialNo;// 软件序列号,请通过亿美销售人员获取
	private String key;// 序列号首次激活时自己设定
	private String password;// 密码,,请通过亿美销售人员获取
	private String baseUrl;
	private Client client=null;
	private static Logger logger=Logger.getLogger(MessageSupportServiceImpl.class.getName());
	private static Map<String, String> errorMsgs=new HashMap<String, String>();
	/**
	 * 		错误码304
			客户端发送三次失败
				错误码305
			服务器返回了错误的数据，原因可能是通讯过程中有数据丢失
				错误码307
			发送短信目标号码不符合规则，手机号码必须是以0、1开头
				错误码308
			非数字错误，修改密码时如果新密码不是数字那么会报308错误
				其他错误码
			错误码	类别	说明
			3	服务器	连接过多，指单个节点要求同时建立的连接数过多
			10	服务器	客户端注册失败
			11	服务器	企业信息注册失败
			12	服务器	查询余额失败
			13	服务器	充值失败
			14	服务器	手机转移失败
			15	服务器	手机扩展转移失败
			16	服务器	取消转移失败
			17	服务器	发送信息失败
			18	服务器	发送定时信息失败
			22	服务器	注销失败
			27	服务器	查询单条短信费用错误码

	 */
	static {
		errorMsgs.put("304", "客户端发送三次失败功");
		errorMsgs.put("305", "服务器返回了错误的数据，原因可能是通讯过程中有数据丢失");
		errorMsgs.put("307", "发送短信目标号码不符合规则，手机号码必须是以0、1开头");
		errorMsgs.put("308", "非数字错误，修改密码时如果新密码不是数字那么会报308错误");
		errorMsgs.put("3", "连接过多，指单个节点要求同时建立的连接数过多");
		errorMsgs.put("10", "客户端注册失败");
		errorMsgs.put("11", "企业信息注册失败");
		errorMsgs.put("12", "查询余额失败");
		errorMsgs.put("13", "充值失败");
		errorMsgs.put("14", "手机转移失败");
		errorMsgs.put("15", "手机扩展转移失败");
		errorMsgs.put("16", "取消转移失败");
		errorMsgs.put("17", "发送信息失败");
		errorMsgs.put("18", "发送定时信息失败");
		errorMsgs.put("22", "注销失败");
		errorMsgs.put("27", "查询单条短信费用错误码");
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	@PostConstruct
	public void init()
	{
		client=new Client(serialNo,key);
	}
	@Override
	public void sendSMS(List<String> tels, String content) throws SMSException{
		sendScheduledSMS(tels, content, -1);
	}
	@Override
	public void sendScheduledSMS(List<String> tels, String content,
			long sendTime) throws SMSException {
		if(tels==null||tels.size()==0||StringUtil.isEmpty(content))
			return;
		int resultCode=0;
		try {
			if(sendTime==-1)
				resultCode=client.sendScheduledSMSEx((String[])(tels.toArray()), content,"", CHARSET);
			else	
				resultCode=client.sendScheduledSMSEx((String[])(tels.toArray()), content, DateUtils.formatDate(new Date(sendTime),"yyyyMMddHHmmss"), CHARSET);
			if(resultCode!=0)
				throw new SMSException(errorMsgs.get(resultCode));
		} catch (RemoteException e) {
			logger.error(e.getMessage(),e);
			throw new SMSException("短信平台服务异常");
		}
	}
}
