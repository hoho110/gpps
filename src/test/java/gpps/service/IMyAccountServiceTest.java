package gpps.service;

import org.junit.BeforeClass;
import org.junit.Test;

import gpps.TestSupport;

public class IMyAccountServiceTest extends TestSupport{
	static IMyAccountService myAccountService;
	@BeforeClass
	public static void init()
	{
		myAccountService=context.getBean(IMyAccountService.class);
	}
	@Test
	public void test()
	{
		
	}
}
