package gpps.service.impl;

import gpps.service.exception.SMSException;
import gpps.service.message.Client;
import gpps.service.message.IMessageSupportService;
import gpps.service.thirdpay.IHttpClientService;
import gpps.tools.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;

import org.apache.http.client.utils.DateUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
public class MessageSupportServiceImpl implements IMessageSupportService {
	public static final String CHARSET="UTF-8";
	private String serialNo="0SDK-EBB-0130-JIUMN";// 软件序列号,请通过亿美销售人员获取
	private String key="503497";// 序列号首次激活时自己设定
	private String password="503497";// 密码,,请通过亿美销售人员获取
	private String baseUrl="http://sdkhttp.eucp.b2m.cn";
	private Client client=null;
	private static Logger logger=Logger.getLogger(MessageSupportServiceImpl.class.getName());
	private static Map<String, String> urls=new HashMap<String, String>();
	public static final String ACTION_REGISTDETAILINFO="0";
	public static final String ACTION_REGIST="1";
	public static final String ACTION_SENDSMS="2";
	public static final String ACTION_SENDTIMESMS="4";
	public static final String ACTION_GETMO="5";
	static {
		urls.put(ACTION_REGISTDETAILINFO, "/sdkproxy/registdetailinfo.action");
		urls.put(ACTION_REGIST, "/sdkproxy/regist.action");
		urls.put(ACTION_SENDSMS, "/sdkproxy/sendsms.action");
		urls.put(ACTION_SENDTIMESMS, "/sdkproxy/sendtimesms.action");
		urls.put(ACTION_GETMO, "/sdkproxy/getmo.action");
	}
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
	@Autowired
	IHttpClientService httpClientService;
    DocumentBuilder builder =null;  
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
//	@PostConstruct
//	public void init()
//	{
//		client=new Client(serialNo,key,password,baseUrl);
//		try {
//			int value=client.registDetailInfo("企业名称", "联系人", "01058750425","13000000000", "sjfkls@yahoo.cn", "01058750500", "企业地址", "056900");
//			System.out.println("企业注册返回码："+value);
//			value = client.registEx(password);
//			System.out.println("序列号激活返回码:" + value);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//	}
	private String getUrl(String action)
	{
		return baseUrl+urls.get(action);
	}
	private String getResultCode(String text)
	{
		 Document doc = null;  
         try {  
             doc = DocumentHelper.parseText(text.trim());
             Element root = doc.getRootElement();// 指向根节点
             return root.element("error").getText();
         } catch (DocumentException e) {  
             logger.error(e.getMessage(),e);
         }  
         return null;
	}
//	@PostConstruct
	public void init()
	{
		/**
		 * 注册企业信息
		 * cdkey	用户序列号。
			password	用户密码
			ename	企业名称(最多60字节)，必须输入
			linkman	联系人姓名(最多20字节)，必须输入
			phonenum	联系电话(最多20字节)，必须输入
			mobile	联系手机(最多15字节)，必须输入
			email	电子邮件(最多60字节)，必须输入
			fax	联系传真(最多20字节)，必须输入
			address	公司地址(最多60字节)，必须输入
			postcode	邮政编码(最多6字节)，必须输入

		 */
		logger.info("短信服务注册企业信息");
		Map<String, String> params=new HashMap<String, String>();
		params.put("cdkey", serialNo);
		params.put("password", password);
		params.put("ename", "春雷投资");
		params.put("linkman", "王东");
		params.put("phonenum", "13601114578");
		params.put("mobile", "13601114578");
		params.put("email", "test@calis.edu.cn");
		params.put("fax", "01062758880");
		params.put("address", "北京中关村");
		params.put("postcode", "100080");

		String resp=httpClientService.post(getUrl(ACTION_REGISTDETAILINFO), params);
		String resultCode=getResultCode(resp);
		if(!resultCode.equals("0"))
			throw new RuntimeException(errorMsgs.get(resultCode));
		
		logger.info("短信服务激活");
		params.clear();
		params.put("cdkey", serialNo);
		params.put("password", password);

		resp=httpClientService.post(getUrl(ACTION_REGIST), params);
		resultCode=getResultCode(resp);
		if(!resultCode.equals("0"))
			throw new RuntimeException(errorMsgs.get(resultCode));
	}
	@Override
	public void sendSMS(List<String> tels, String content) throws SMSException{
		if(tels==null||tels.size()==0||StringUtil.isEmpty(content))
			return;
		StringBuilder sBuilder=new StringBuilder();
		for(int i=0;i<tels.size();i++)
		{
			if(i!=0)
				sBuilder.append(",");
			sBuilder.append(tels.get(i));
		}
		Map<String, String> params=new HashMap<String, String>();
		params.put("cdkey", serialNo);
		params.put("password", password);
		params.put("phone", sBuilder.toString());
		params.put("message", content);
		String resp=httpClientService.post(getUrl(ACTION_SENDSMS), params);
		String resultCode=getResultCode(resp);
		if(!resultCode.equals("0"))
			throw new RuntimeException(errorMsgs.get(resultCode));
	}
	@Override
	public void sendScheduledSMS(List<String> tels, String content,
			long sendTime) throws SMSException {
//		if(tels==null||tels.size()==0||StringUtil.isEmpty(content))
//			return;
//		int resultCode=0;
//		try {
//			if(sendTime==-1)
//				resultCode=client.sendScheduledSMSEx((String[])(tels.toArray()), content,"", CHARSET);
//			else	
//				resultCode=client.sendScheduledSMSEx((String[])(tels.toArray()), content, DateUtils.formatDate(new Date(sendTime),"yyyyMMddHHmmss"), CHARSET);
//			if(resultCode!=0)
//				throw new SMSException(errorMsgs.get(resultCode));
//		} catch (RemoteException e) {
//			logger.error(e.getMessage(),e);
//			throw new SMSException("短信平台服务异常");
//		}
		if(tels==null||tels.size()==0||StringUtil.isEmpty(content))
			return;
		StringBuilder sBuilder=new StringBuilder();
		for(int i=0;i<tels.size();i++)
		{
			if(i!=0)
				sBuilder.append(",");
			sBuilder.append(tels.get(i));
		}
		Map<String, String> params=new HashMap<String, String>();
		params.put("cdkey", serialNo);
		params.put("password", password);
		params.put("phone", sBuilder.toString());
		params.put("message", content);
		params.put("sendtime", getDateStr(sendTime));
		String resp=httpClientService.post(getUrl(ACTION_SENDTIMESMS), params);
		String resultCode=getResultCode(resp);
		if(!resultCode.equals("0"))
			throw new RuntimeException(errorMsgs.get(resultCode));
	}
	private String getDateStr(long time){
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(new Date(time));
	}
	@Override
	public void getUpSMS() throws SMSException {
//		try {
//			List<Mo> mos = client.getMO();
//			if(mos==null||mos.size()==0)
//			{
//				logger.debug("未获取到上行短信");
//				return;
//			}
//			for(Mo mo:mos)
//			{
//				System.out.println("短信内容:" + mo.getSmsContent());
//				System.out.println("通道号:" + mo.getChannelnumber());
//				System.out.println("手机号:" + mo.getMobileNumber());
//				System.out.println("附加码:" + mo.getAddSerialRev());
//				System.out.println("附加码:" + mo.getAddSerial());
//			}
//		} catch (RemoteException e) {
//			logger.error(e.getMessage(),e);
//			throw new SMSException("短信平台服务异常");
//		}
		Map<String, String> params=new HashMap<String, String>();
		params.put("cdkey", serialNo);
		params.put("password", password);
		String resp=httpClientService.post(getUrl(ACTION_GETMO), params);
		
		System.out.println(resp);
	}
}
