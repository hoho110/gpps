package gpps;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import gpps.service.exception.SMSException;
import gpps.service.message.IMessageSupportService;

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
		tels.add("13601122581");
		try {
			messageSupportService.sendSMS(tels, "您好，您中大奖啦!!");
		} catch (SMSException e) {
			e.printStackTrace();
		}
	}
}
