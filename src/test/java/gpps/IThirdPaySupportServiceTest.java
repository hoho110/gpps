package gpps;

import org.junit.BeforeClass;
import org.junit.Test;

import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.IllegalOperationException;
import gpps.service.thirdpay.IThirdPaySupportService;

public class IThirdPaySupportServiceTest extends TestSupport{
	private static IThirdPaySupportService thirdPaySupportService;
	@BeforeClass
	public static void init()
	{
		thirdPaySupportService=context.getBean(IThirdPaySupportService.class);
	}
	@Test
	public void testCheckWithThirdPay(){
		try {
			thirdPaySupportService.checkWithThirdPay(2003);
		} catch (IllegalOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalConvertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
