package gpps;

import gpps.model.PayBack;
import gpps.model.ProductSeries;
import gpps.service.IPayBackService;

import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class IPayBackServiceTest extends TestSupport{
	private static IPayBackService payBackService;
	@BeforeClass
	public static void init()
	{
		payBackService=context.getBean(IPayBackService.class);
	}
	@Test
	public void testGeneratePayBacks()
	{
		/**
		 * 从2014-12-15 到2015-2-25共73天，其中：
		 * 2014-12-15到2015-1-14 31天
		 * 2015-1-15到2015-2-14   31天
		 * 2015-2-15到2015-2-25	10天
		 */
		Date from=new Date(2014-1900,12-1,15,8,0,0);
		Date to=new Date(2015-1900,2-1,25,8,0,0);
		System.out.println(from);
		System.out.println(to);
		List<PayBack> payBacks=payBackService.generatePayBacks(100*10000, 0.12, ProductSeries.TYPE_FINISHPAYINTERESTANDCAPITAL, from.getTime(), to.getTime());
		Assert.assertEquals(3, payBacks.size());
		
		Assert.assertEquals("0", payBacks.get(0).getChiefAmount().toString());
		Assert.assertEquals("10191.79", payBacks.get(0).getInterest().toString());
		
		Assert.assertEquals("0", payBacks.get(1).getChiefAmount().toString());
		Assert.assertEquals("10191.79", payBacks.get(1).getInterest().toString());
		
		Assert.assertEquals("1000000", payBacks.get(2).getChiefAmount().toString());
		Assert.assertEquals("3287.68", payBacks.get(2).getInterest().toString());
	}
}
