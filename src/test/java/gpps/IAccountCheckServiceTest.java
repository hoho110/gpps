package gpps;

import gpps.service.IAccountCheckService;

public class IAccountCheckServiceTest extends TestSupport {
	private static IAccountCheckService accountCheckService;
	public static void main(String[] args)
	{
		accountCheckService = context.getBean(IAccountCheckService.class);
		accountCheckService.checkAccount();
		try
		{
			Thread.sleep(2*1000);
			while(!accountCheckService.finished())
			{
				Thread.sleep(1*1000);
			}
			System.out.println("运行结果:"+accountCheckService.getReport());
		}catch(Throwable e)
		{
			e.printStackTrace();
		}
		System.exit(-1);
	}
}
