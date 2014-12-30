package gpps;

import gpps.model.PayBack;
import gpps.model.ProductSeries;
import gpps.service.IPayBackService;
import gpps.tools.PayBackCalculateUtils;

import java.math.BigDecimal;
import java.util.Calendar;
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
	public void testJHGeneratePayBacks(){
		
		int amount = 100*10000;
		float rate = 0.18f;
		
		//30天利息
		BigDecimal averinterest = (new BigDecimal(amount)).multiply(new BigDecimal(rate)).multiply(new BigDecimal(30)).divide(new BigDecimal(365),2,BigDecimal.ROUND_DOWN);
		
		/**
		 * 从2014年12月19日到2015年7月2号
		 * 
		 * */
		Date from = new Date(2014-1900,12-1,19,8,0,0);
		Date to = new Date(2015-1900,7-1,2,8,0,0);
		
		List<PayBack> payBacks=payBackService.generatePayBacks(amount, rate, ProductSeries.TYPE_FIRSTINTERESTENDCAPITAL, from.getTime(), to.getTime());
		
		Assert.assertEquals(7, payBacks.size());
		
		BigDecimal realInterest = new BigDecimal(0);
		int i=0;
		for(PayBack payBack:payBacks){
			i++;
			
			//除最后一个月外（天数不定），其余月份都在28-31天，因此和30天之间的利息差不会超过平均值的1/10
			if(i<payBacks.size()){
				BigDecimal flag = payBack.getInterest().subtract(averinterest).abs().divide(averinterest, 2, BigDecimal.ROUND_DOWN);
				Assert.assertTrue(flag.floatValue()<0.1f);
			}
			
			System.out.println("还款时间："+(new Date(payBack.getDeadline())).toLocaleString()+"     还款本金："+payBack.getChiefAmount().floatValue()+"      还款利息："+payBack.getInterest().floatValue()+"        总额："+(payBack.getChiefAmount().add(payBack.getInterest()).floatValue()));
			realInterest = realInterest.add(payBack.getInterest());
		}
		System.out.println("总还款利息为："+realInterest);
		
		int days = PayBackCalculateUtils.getDays(from.getTime(), to.getTime());
		BigDecimal interest = (new BigDecimal(amount)).multiply(new BigDecimal(rate)).multiply(new BigDecimal(days)).divide(new BigDecimal(365),2,BigDecimal.ROUND_DOWN);
		System.out.println("计算还款利息为："+interest);
		
		Assert.assertEquals(interest.toString(), realInterest.toString());
	}
	
	
	@Test
	public void testJQGeneratePayBacks(){
		
		int amount = 150*10000;
		float rate = 0.24f;
		
		//30天利息
		BigDecimal averinterest = (new BigDecimal(amount)).multiply(new BigDecimal(rate)).multiply(new BigDecimal(30)).divide(new BigDecimal(365),2,BigDecimal.ROUND_DOWN);
		
		/**
		 * 从2014年12月19日到2015年7月2号
		 * 
		 * */
		Date from = new Date(2015-1900,1-1,29,8,0,0);
		Date to = new Date(2016-1900,8-1,8,8,0,0);
		
		List<PayBack> payBacks=payBackService.generatePayBacks(amount, rate, ProductSeries.TYPE_FIRSTINTERESTENDCAPITAL, from.getTime(), to.getTime());
		
		Assert.assertEquals(19, payBacks.size());
		
		BigDecimal realInterest = new BigDecimal(0);
		int i=0;
		for(PayBack payBack:payBacks){
			i++;
			
			//除最后一个月外（天数不定），其余月份都在28-31天，因此和30天之间的利息差不会超过平均值的1/10
			if(i<payBacks.size()){
				BigDecimal flag = payBack.getInterest().subtract(averinterest).abs().divide(averinterest, 2, BigDecimal.ROUND_DOWN);
				Assert.assertTrue(flag.floatValue()<0.1f);
			}
			
			System.out.println("还款时间："+(new Date(payBack.getDeadline())).toLocaleString()+"     还款本金："+payBack.getChiefAmount().floatValue()+"      还款利息："+payBack.getInterest().floatValue()+"        总额："+(payBack.getChiefAmount().add(payBack.getInterest()).floatValue()));
			realInterest = realInterest.add(payBack.getInterest());
		}
		System.out.println("总还款利息为："+realInterest);
		
		int days = PayBackCalculateUtils.getDays(from.getTime(), to.getTime());
		BigDecimal interest = (new BigDecimal(amount)).multiply(new BigDecimal(rate)).multiply(new BigDecimal(days)).divide(new BigDecimal(365),2,BigDecimal.ROUND_DOWN);
		System.out.println("计算还款利息为："+interest);
		
		Assert.assertEquals(interest.toString(), realInterest.toString());
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
		Assert.assertEquals("10191.78", payBacks.get(0).getInterest().toString());
		
		Assert.assertEquals("0", payBacks.get(1).getChiefAmount().toString());
		Assert.assertEquals("10191.78", payBacks.get(1).getInterest().toString());
		
		Assert.assertEquals("1000000", payBacks.get(2).getChiefAmount().toString());
		Assert.assertEquals("3287.67", payBacks.get(2).getInterest().toString());
		
		int days = PayBackCalculateUtils.getDays(from.getTime(), to.getTime());
		BigDecimal interest = (new BigDecimal(100*10000)).multiply(new BigDecimal(0.12)).multiply(new BigDecimal(days)).divide(new BigDecimal(365),2,BigDecimal.ROUND_DOWN);
		Assert.assertEquals("23671.23", interest.toString());
		
		payBacks=payBackService.generatePayBacks(100*10000, 0.12, ProductSeries.TYPE_AVERAGECAPITALPLUSINTEREST, from.getTime(), to.getTime());
		Assert.assertEquals(3, payBacks.size());
		
		Assert.assertEquals("330022.11", payBacks.get(0).getChiefAmount().toString());
		Assert.assertEquals("10000.00", payBacks.get(0).getInterest().toString());
		
		Assert.assertEquals("333322.33", payBacks.get(1).getChiefAmount().toString());
		Assert.assertEquals("6699.78", payBacks.get(1).getInterest().toString());
		
		Assert.assertEquals("336655.56", payBacks.get(2).getChiefAmount().toString());
		Assert.assertEquals("3366.56", payBacks.get(2).getInterest().toString());
		
		
		for(int i=1;i<=10000;i++)
		{
			payBacks=payBackService.generatePayBacks(i*1000, 0.16, ProductSeries.TYPE_AVERAGECAPITALPLUSINTEREST, from.getTime(), to.getTime());
			Assert.assertEquals(3, payBacks.size());
			BigDecimal totalChiefAmout=BigDecimal.ZERO;
			for(PayBack payBack:payBacks)
				totalChiefAmout=totalChiefAmout.add(payBack.getChiefAmount());
			Assert.assertEquals(0, new BigDecimal(i*1000).compareTo(totalChiefAmout));
			if(i%1000==0)
				System.out.println("已校验数："+i);
		}
		
	}
	
}
