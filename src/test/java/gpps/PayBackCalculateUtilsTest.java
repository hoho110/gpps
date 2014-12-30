package gpps;

import gpps.model.PayBack;
import gpps.tools.PayBackCalculateUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class PayBackCalculateUtilsTest {
	@Test
	public void testGetDays(){
		//2014年10月1号到2015年10月1号，共365天
		Date from0 = new Date(2014-1900,10-1,1,8,0,0);
		Date to0 = new Date(2015-1900,10-1,1,8,0,0);
		int days = PayBackCalculateUtils.getDays(from0.getTime(), to0.getTime());
		Assert.assertEquals(365, days);
		
		
		//2014年2月3号到3月3号，共28天
		Date from1 = new Date(2014-1900,2-1,3,8,0,0);
		Date to1 = new Date(2014-1900,3-1,3,8,0,0);
		days = PayBackCalculateUtils.getDays(from1.getTime(), to1.getTime());
		Assert.assertEquals(28, days);
		
		//2012年2月3号到3月1号，共27天（闰年）
		Date from2 = new Date(2012-1900,2-1,3,8,0,0);
		Date to2 = new Date(2012-1900,3-1,1,8,0,0);
		days = PayBackCalculateUtils.getDays(from2.getTime(), to2.getTime());
		Assert.assertEquals(27, days);
		
		//2012年2月3号到2013年2月3号，共27天（闰年）
		Date from3 = new Date(2012-1900,2-1,3,8,0,0);
		Date to3 = new Date(2013-1900,2-1,3,8,0,0);
		days = PayBackCalculateUtils.getDays(from3.getTime(), to3.getTime());
		Assert.assertEquals(366, days);
		
		//起始日期在终止日期之后
		Date from4 = new Date(2014-1900,2-1,3,8,0,0);
		Date to4 = new Date(2013-1900,2-1,3,8,0,0);
		days = PayBackCalculateUtils.getDays(from4.getTime(), to4.getTime());
		Assert.assertEquals(0, days);
	}
	
	
	
	
	@Test
	public void testJHGeneratePayBacks(){
		
		int amount = 100*10000;
		float rate = 0.18f;
		
		
		/**
		 * 从2014年12月19日到2015年7月2号
		 * 
		 * */
		Date from = new Date(2014-1900,12-1,19,8,0,0);
		Date to = new Date(2015-1900,7-1,2,8,0,0);
		
		List<PayBack> payBacks=PayBackCalculateUtils.calclatePayBacksForXXHB(amount, rate, from.getTime(), to.getTime());
		
		Assert.assertEquals(7, payBacks.size());
		
		
		//30天利息
		BigDecimal averinterest = (new BigDecimal(amount)).multiply(new BigDecimal(rate)).multiply(new BigDecimal(30)).divide(new BigDecimal(365),2,BigDecimal.ROUND_DOWN);
				
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
		
		
		/**
		 * 从2014年12月19日到2015年7月2号
		 * 
		 * */
		Date from = new Date(2015-1900,1-1,29,8,0,0);
		Date to = new Date(2016-1900,8-1,8,8,0,0);
		
		List<PayBack> payBacks=PayBackCalculateUtils.calclatePayBacksForXXHB(amount, rate, from.getTime(), to.getTime());
		
		Assert.assertEquals(19, payBacks.size());
		
		
		//30天利息
		BigDecimal averinterest = (new BigDecimal(amount)).multiply(new BigDecimal(rate)).multiply(new BigDecimal(30)).divide(new BigDecimal(365),2,BigDecimal.ROUND_DOWN);
				
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
	public void testWJGeneratePayBacks(){
		int amount = 75*10000;
		float rate = 0.12f;
		
		
		/**
		 * 从2015年1月15日到2015年7月15号
		 * 
		 * */
		Date from = new Date(2015-1900,1-1,15,8,0,0);
		Date to = new Date(2015-1900,7-1,15,8,0,0);
		
		List<PayBack> payBacks = PayBackCalculateUtils.calclatePayBacksForDEBX(amount, rate, from.getTime(), to.getTime());
		Assert.assertEquals(6, payBacks.size());
		
		BigDecimal realChief = new BigDecimal(0);
		BigDecimal realInterest = new BigDecimal(0);
		for(PayBack payBack:payBacks){
			System.out.println("还款时间："+(new Date(payBack.getDeadline())).toLocaleString()+"     还款本金："+payBack.getChiefAmount().floatValue()+"      还款利息："+payBack.getInterest().floatValue()+"        总额："+(payBack.getChiefAmount().add(payBack.getInterest()).floatValue()));
			realInterest = realInterest.add(payBack.getInterest());
			realChief = realChief.add(payBack.getChiefAmount());
		}
		System.out.println("总还款利息为："+realInterest+"      总还款本金为："+realChief);
		Assert.assertEquals(realChief.intValue(), amount);
	}
}
