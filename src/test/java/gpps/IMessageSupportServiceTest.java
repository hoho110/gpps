package gpps;

import gpps.service.exception.SMSException;
import gpps.service.message.IMessageSupportService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class IMessageSupportServiceTest extends TestSupport {
	private static IMessageSupportService messageSupportService;
	@BeforeClass
	public static void init()
	{
		messageSupportService=context.getBean(IMessageSupportService.class);
	}
	@Test
	public void testSendSMS()
	{
		List<String> tels=new ArrayList<String>();
//		tels.add("15901097711");
//		tels.add("15176156009");
//		tels.add("13811502837");
		tels.add("323232323");
		try {
			messageSupportService.sendSMS(tels, "【春雷投资】您的验证码为222222");
//			long time=60L*15*1000+System.currentTimeMillis();
//			messageSupportService.sendScheduledSMS(tels, "【春雷投资】您的定时验证码为123321,请求发送时间为"+getDateStr(time), time);//定时一s分钟后发送
//			System.out.println("定时接收时间："+getDateStr(time));
//			messageSupportService.getUpSMS();
		} catch (SMSException e) {
			e.printStackTrace();
		}
	}
	private String getDateStr(long time){
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(new Date(time));
	}
}
