package gpps.service;

import gpps.model.CashStream;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.Submit;
import gpps.tools.PayBackCalculateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutePayBackTest {
	
	public static void main(String args[]) throws Exception{
		int amount = 750000;
		Product product = new Product();
		product.setRealAmount(new BigDecimal(amount));
		List<Submit> submits = new ArrayList<Submit>();
		Submit sub1 = new Submit();
		sub1.setAmount(new BigDecimal(100000));
		sub1.setId(1);
		submits.add(sub1);
		
		Submit sub2 = new Submit();
		sub2.setAmount(new BigDecimal(50000));
		sub2.setId(2);
		submits.add(sub2);
		
		Submit sub3 = new Submit();
		sub3.setAmount(new BigDecimal(70000));
		sub3.setId(3);
		submits.add(sub3);
		
		Submit sub4 = new Submit();
		sub4.setAmount(new BigDecimal(200000));
		sub4.setId(4);
		submits.add(sub4);
		
		Submit sub5 = new Submit();
		sub5.setAmount(new BigDecimal(110000));
		sub5.setId(5);
		submits.add(sub5);
		
		Submit sub6 = new Submit();
		sub6.setAmount(new BigDecimal(220000));
		sub6.setId(6);
		submits.add(sub6);
		
		Date from = new Date(2014-1900,4-1,19,8,0,0);
		Date to = new Date(2015-1900,7-1,2,8,0,0);
//		List<PayBack> payBacks = PayBackCalculateUtils.calclatePayBacksForDEBX(amount, 0.18, from.getTime(), to.getTime());
		List<PayBack> payBacks = PayBackCalculateUtils.calclatePayBacksForXXHB(amount, 0.18, from.getTime(), to.getTime());
		
		List<Map<Integer, CashStream>> payBackHistory = new ArrayList<Map<Integer,CashStream>>();
		for(PayBack payBack : payBacks){
			Map<Integer, CashStream> pay = null;
			if(payBack.getType()!=PayBack.TYPE_LASTPAY){
				pay = executeNotLastPayBack(product, payBack, submits);
			}else{
				pay = executeLastPayBack(product, payBack, submits, payBackHistory);
			}
			pintPay(pay);
			payBackHistory.add(pay);
		}
		
		BigDecimal productTotalChief = new BigDecimal(0);
		BigDecimal productTotalInterest = new BigDecimal(0);
		
		BigDecimal restInterest = new BigDecimal(0);
		for(Map<Integer, CashStream> his : payBackHistory){
			restInterest = restInterest.add(his.get(-1)==null?(new BigDecimal(0)):his.get(-1).getInterest());
		}
		System.out.print(-1+":0/"+restInterest.toString()+"\t");
		productTotalInterest = productTotalInterest.add(restInterest);
		
		for(Submit submit : submits){
			BigDecimal totalChief = new BigDecimal(0);
			BigDecimal totalInterest = new BigDecimal(0);
			for(Map<Integer, CashStream> his : payBackHistory){
				totalChief = totalChief.add(his.get(submit.getId()).getChiefamount());
				totalInterest = totalInterest.add(his.get(submit.getId()).getInterest());
			}
			System.out.print(submit.getId()+":"+totalChief.toString()+"/"+totalInterest.toString()+"\t");
			productTotalChief = productTotalChief.add(totalChief);
			productTotalInterest = productTotalInterest.add(totalInterest);
		}
		System.out.println();
		
		
		
		
		System.out.println("product total payback by submits:     "+productTotalChief.toString()+"////"+productTotalInterest.toString());
		System.out.println();
		
		BigDecimal totalPayBackChief = new BigDecimal(0);
		BigDecimal totalPayBackInterest = new BigDecimal(0);
		for(PayBack payback : payBacks){
			System.out.println("single payback: "+payback.getChiefAmount().toString()+"/"+payback.getInterest().toString());
			totalPayBackChief = totalPayBackChief.add(payback.getChiefAmount());
			totalPayBackInterest = totalPayBackInterest.add(payback.getInterest());
		}
		System.out.println("product total payback by paybacks:     "+totalPayBackChief.toString()+"////"+totalPayBackInterest.toString());
		
	}
	
	private static void pintPay(Map<Integer, CashStream> payback){
		for(int i=-1; i<10; i++){
			CashStream cs = payback.get(i);
			if(cs!=null){
				System.out.print(i+":"+cs.getChiefamount().toString()+"/"+cs.getInterest().toString()+"\t");
			}
		}
		System.out.println();
	}
	
	
	
	public static Map<Integer, CashStream> executeNotLastPayBack(Product product, PayBack payBack, List<Submit> submits){
		BigDecimal totalChiefAmount=payBack.getChiefAmount();
		BigDecimal totalInterest=payBack.getInterest();
		Map<Integer, CashStream> res = new HashMap<Integer, CashStream>();
		
		BigDecimal sumChiefAmount = new BigDecimal(0);
		BigDecimal sumInterest = new BigDecimal(0);
		for(int i=0; i<submits.size(); i++){
			Submit submit = submits.get(i);
			
			CashStream cs = new CashStream();
			cs.setAction(CashStream.ACTION_REPAY);
			cs.setInterest(totalInterest.multiply(submit.getAmount()).divide(product.getRealAmount(), 2, BigDecimal.ROUND_DOWN));
			sumInterest = sumInterest.add(cs.getInterest());
			
			//当不是最后一个submit的时候
			if((i+1)<submits.size()){
				cs.setChiefamount(totalChiefAmount.multiply(submit.getAmount()).divide(product.getRealAmount(), 2, BigDecimal.ROUND_DOWN));
				sumChiefAmount = sumChiefAmount.add(cs.getChiefamount());
			}else{
				cs.setChiefamount(totalChiefAmount.subtract(sumChiefAmount));
			}
			res.put(submit.getId(), cs);
		}
		
		if(sumInterest.compareTo(totalInterest)!=0){
			CashStream cs = new CashStream();
			cs.setAction(CashStream.ACTION_REPAY);
			cs.setChiefamount(new BigDecimal(0));
			cs.setInterest(totalInterest.subtract(sumInterest));
			res.put(-1, cs);
		}
		
		return res;
	}
	
	public static Map<Integer, CashStream> executeLastPayBack(Product product, PayBack payBack, List<Submit> submits, List<Map<Integer, CashStream>> payBackHistory) throws Exception{
		BigDecimal totalChiefAmount=payBack.getChiefAmount();
		BigDecimal totalInterest=payBack.getInterest();
		Map<Integer, CashStream> res = new HashMap<Integer, CashStream>();
		
		BigDecimal totalChiefForThisPayBack = new BigDecimal(0);
		BigDecimal totalInterestForThisPayBack = new BigDecimal(0);
		
		for(Submit submit : submits){
			BigDecimal sumChief = new BigDecimal(0);
			for(Map<Integer, CashStream> history : payBackHistory){
				CashStream cs = history.get(submit.getId());
				if(cs!=null){
					sumChief = sumChief.add(cs.getChiefamount());
				}
			}
			BigDecimal thisChief = submit.getAmount().subtract(sumChief);
			totalChiefForThisPayBack = totalChiefForThisPayBack.add(thisChief);
			BigDecimal thisInterest = payBack.getInterest().multiply(submit.getAmount()).divide(product.getRealAmount(), 2, BigDecimal.ROUND_DOWN);
			totalInterestForThisPayBack = totalInterestForThisPayBack.add(thisInterest);
			CashStream cs = new CashStream();
			cs.setAction(CashStream.ACTION_REPAY);
			cs.setChiefamount(thisChief);
			cs.setInterest(thisInterest);
			res.put(submit.getId(), cs);
		}
		
		if(totalChiefForThisPayBack.compareTo(totalChiefAmount)!=0){
			throw new Exception("整个产品计算出错，所有已还的本金不等于实际融资额度！");
		}
		
		if(totalInterestForThisPayBack.compareTo(totalInterest)!=0){
			CashStream cs = new CashStream();
			cs.setAction(CashStream.ACTION_REPAY);
			cs.setChiefamount(new BigDecimal(0));
			cs.setInterest(totalInterest.subtract(totalInterestForThisPayBack));
			res.put(-1, cs);
		}
		return res;
	}
}
