package gpps.service;

import org.junit.BeforeClass;
import org.junit.Test;

import gpps.TestSupport;
import gpps.service.exception.CheckException;

public class PayBackServiceTest extends TestSupport{
	private static IPayBackService payBackService;
	@BeforeClass
	public static void init(){
		payBackService=context.getBean(IPayBackService.class);
	}
	@Test
	public void testCheck()
	{
		Integer payBackId=1;
		try {
			payBackService.checkoutPayBack(payBackId);
		} catch (CheckException e) {
			e.printStackTrace();
		}
	}
}
