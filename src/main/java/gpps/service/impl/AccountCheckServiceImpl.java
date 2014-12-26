package gpps.service.impl;

import gpps.dao.IBorrowerAccountDao;
import gpps.dao.IBorrowerDao;
import gpps.dao.ICashStreamDao;
import gpps.dao.ILenderAccountDao;
import gpps.dao.ILenderDao;
import gpps.model.Borrower;
import gpps.model.BorrowerAccount;
import gpps.model.CashStream;
import gpps.model.Lender;
import gpps.model.LenderAccount;
import gpps.service.CashStreamSum;
import gpps.service.IAccountCheckService;
import gpps.service.thirdpay.Cash;
import gpps.service.thirdpay.IThirdPaySupportService;
import gpps.tools.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountCheckServiceImpl implements IAccountCheckService {
	private FutureTask<String> future;
	@Autowired
	private ILenderAccountDao lenderAccountDao;
	@Autowired
	private IBorrowerAccountDao borrowerAccountDao;
	@Autowired
	private IThirdPaySupportService thirdPaySupportService;
	@Autowired
	private ICashStreamDao cashStreamDao;
	@Autowired
	private ILenderDao lenderDao;
	@Autowired
	private IBorrowerDao borrowerDao;
	private static final int DEFAULT_RECNUM=100;
	private static final String NEWLINE="\r\n";
	Logger logger=Logger.getLogger(AccountCheckServiceImpl.class);
//	@PostConstruct
//	public void init() {
//		checkAccount();
//	}
	@Override
	public void checkAccount() {
		if (future != null && !future.isDone())
			return;
		future = new FutureTask<String>(new Callable<String>() {
			@Override
			public String call() throws Exception {
				StringBuilder sBuilder=new StringBuilder();
				int totallender = 0;
				int successlender = 0;
				int errorlender = 0;
				
				int totalborrower = 0;
				int successborrower = 0;
				int errorborrower = 0;
				try
				{
					int total=lenderDao.countAll();
					for(int i=0;i<(total/DEFAULT_RECNUM+(total%DEFAULT_RECNUM==0?0:1));i++)
					{
						List<Lender> lenders=lenderDao.findAll(i*DEFAULT_RECNUM, DEFAULT_RECNUM);
						if(lenders==null||lenders.size()==0)
							continue;
						for(Lender lender:lenders)
						{
							totallender++;
							
							boolean flag = true;
							
							if(StringUtil.isEmpty(lender.getThirdPartyAccount()))
							{
								successlender++;
								continue;
							}
							//与第三方验证
							//网贷平台子账户可用余额|总可用余额(子账户可用余额+公共账户可用余额)|子账户冻结余额”（例:100.00|200.00|10.00）
							String text=thirdPaySupportService.balanceQuery(lender.getThirdPartyAccount());
							if(StringUtil.isEmpty(text))
							{
								appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), "从第三方支付平台获取账户信息失败.");
								errorlender++;
								continue;
							}
							
							try{
								Thread.sleep(10);
							}catch(Exception e){
								
							}
							
							LenderAccount account=lenderAccountDao.find(lender.getAccountId());
							String[] thirdAccount=text.split("\\|");
							if(thirdAccount.length!=3)
							{
								flag=false;
								appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), 
										"查询账户的乾多多标识错误:"+lender.getThirdPartyAccount());
							}
							else if(!compareAccount(thirdAccount[0], account.getUsable())||!compareAccount(thirdAccount[2], account.getFreeze()))
							{
								flag=false;
								appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), 
										"本地账户与第三方支付平台不符,本地可用|冻结金额为"+account.getUsable().toString()+"|"+account.getFreeze().toString()+";"
										+"第三方可用|冻结金额为"+thirdAccount[0]+"|"+thirdAccount[2]);
							}
							
							
							//验证：总金额=可用金额+冻结金额+已投资金额
							if(account.getFreeze().add(account.getUsable()).add(account.getUsed()).compareTo(account.getTotal())!=0)
							{
								flag=false;
								appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), "账户金额错误,总金额不等于可用金额+冻结金额+已投资金额");
							}
							//可用金额=所有现金流之和
							CashStreamSum sum=cashStreamDao.sumCashStream(lender.getAccountId(), null, null);
							sum=(sum==null)?new CashStreamSum():sum;
							if(account.getUsable().compareTo(sum.getChiefAmount().add(sum.getInterest()))!=0)
							{
								flag=false;
								appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), "可用金额与现金流验证错误,可用金额:"+account.getUsable().toString()+",现金流:"+sum);
							}
							//冻结金额=冻结+解冻
							List<Integer> actions=new ArrayList<Integer>();
							actions.add(CashStream.ACTION_FREEZE);
							actions.add(CashStream.ACTION_UNFREEZE);
							sum=cashStreamDao.sumCashStream(lender.getAccountId(), null, actions);
							sum=(sum==null)?new CashStreamSum():sum;
							if(account.getFreeze().negate().compareTo(sum.getChiefAmount().add(sum.getInterest()))!=0)
							{
								flag=false;
								appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), "冻结金额与现金流验证错误,冻结金额:"+account.getFreeze().toString()+",现金流:"+sum);
							}
							//已投资金额=购买+回款（本金）
							actions=new ArrayList<Integer>();
							actions.add(CashStream.ACTION_PAY);
							actions.add(CashStream.ACTION_REPAY);
							sum=cashStreamDao.sumCashStream(lender.getAccountId(), null, actions);
							sum=(sum==null)?new CashStreamSum():sum;
							if(account.getUsed().negate().compareTo(sum.getChiefAmount())!=0)
							{
								flag=false;
								appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), "已用金额与现金流验证错误,已用金额:"+account.getFreeze().toString()+",现金流:"+sum.getChiefAmount());
							}
							//利息=回款（利息）
							actions=new ArrayList<Integer>();
							actions.add(CashStream.ACTION_REPAY);
							sum=cashStreamDao.sumCashStream(lender.getAccountId(), null, actions);
							sum=(sum==null)?new CashStreamSum():sum;
							if(account.getTotalincome().compareTo(sum.getInterest())!=0)
							{
								flag=false;
								appendMsg(sBuilder, Lender.class, lender.getId(), lender.getThirdPartyAccount(), "已收益金额与现金流验证错误,已收益金额:"+account.getTotalincome().toString()+",现金流:"+sum.getInterest());
							}
							if(flag==false){
								errorlender++;
							}else{
								successlender++;
							}
						}
					}
					
					total=borrowerDao.countAll();
					for(int i=0;i<(total/DEFAULT_RECNUM+(total%DEFAULT_RECNUM==0?0:1));i++)
					{
						List<Borrower> borrowers=borrowerDao.findAll(i*DEFAULT_RECNUM, DEFAULT_RECNUM);
						if(borrowers==null||borrowers.size()==0)
							continue;
						for(Borrower borrower:borrowers)
						{
							totalborrower++;
							
							boolean flag = true;
							
							if(StringUtil.isEmpty(borrower.getThirdPartyAccount()))
							{
								successborrower++;
								continue;
							}
							//与第三方验证
							//网贷平台子账户可用余额|总可用余额(子账户可用余额+公共账户可用余额)|子账户冻结余额”（例:100.00|200.00|10.00）
							String text=thirdPaySupportService.balanceQuery(borrower.getThirdPartyAccount());
							if(StringUtil.isEmpty(text))
							{
								errorborrower++;
								appendMsg(sBuilder, Borrower.class, borrower.getId(), borrower.getThirdPartyAccount(), "从第三方支付平台获取账户信息失败.");
								continue;
							}
							BorrowerAccount account=borrowerAccountDao.find(borrower.getAccountId());
							String[] thirdAccount=text.split("\\|");
							if(thirdAccount.length!=3)
							{
								flag = false;
								appendMsg(sBuilder, Borrower.class, borrower.getId(), borrower.getThirdPartyAccount(), 
										"查询账户的乾多多标识错误:"+borrower.getThirdPartyAccount());
							}
							else if(!compareAccount(thirdAccount[0], account.getUsable())||!compareAccount(thirdAccount[2], account.getFreeze()))
							{
								flag = false;
								appendMsg(sBuilder, Borrower.class, borrower.getId(), borrower.getThirdPartyAccount(), 
										"本地账户与第三方支付平台不符,本地可用|冻结金额为"+account.getUsable().toString()+"|"+account.getFreeze().toString()+";"
										+"第三方可用|冻结金额为"+thirdAccount[0]+"|"+thirdAccount[2]);
							}
							
							
							//验证：总金额=可用金额+冻结金额
							if(account.getFreeze().add(account.getUsable()).compareTo(account.getTotal())!=0)
							{
								flag = false;
								appendMsg(sBuilder, Borrower.class, borrower.getId(), borrower.getThirdPartyAccount(), "账户金额错误,总金额不等于可用金额+冻结金额");
							}
							//可用金额=(充值、取现、冻结、解冻)现金流之和+（支付、还款、存零）现金流之和取反
							List<Integer> actions=new ArrayList<Integer>();
							actions.add(CashStream.ACTION_FREEZE);
							actions.add(CashStream.ACTION_UNFREEZE);
							actions.add(CashStream.ACTION_CASH);
							actions.add(CashStream.ACTION_RECHARGE);
							CashStreamSum sum=cashStreamDao.sumCashStream(null, borrower.getAccountId(), actions);
							sum=(sum==null)?new CashStreamSum():sum;
							actions=new ArrayList<Integer>();
							actions.add(CashStream.ACTION_PAY);
							actions.add(CashStream.ACTION_REPAY);
							actions.add(CashStream.ACTION_STORECHANGE);
							CashStreamSum sum2=cashStreamDao.sumCashStream(null, borrower.getAccountId(), actions);
							sum2=(sum2==null)?new CashStreamSum():sum2;
							sum.setChiefAmount(sum.getChiefAmount().add(sum2.getChiefAmount().negate()));
							sum.setInterest(sum.getInterest().add(sum2.getInterest().negate()));
							if(account.getUsable().compareTo(sum.getChiefAmount().add(sum.getInterest()))!=0)
							{
								flag = false;
								appendMsg(sBuilder, Borrower.class, borrower.getId(), borrower.getThirdPartyAccount(), "可用金额与现金流验证错误,可用金额:"+account.getUsable().toString()+",现金流:"+sum);
							}
							
							//冻结金额=冻结+解冻
							actions=new ArrayList<Integer>();
							actions.add(CashStream.ACTION_FREEZE);
							actions.add(CashStream.ACTION_UNFREEZE);
							sum=cashStreamDao.sumCashStream(null, borrower.getAccountId(), actions);
							sum=(sum==null)?new CashStreamSum():sum;
							if(account.getFreeze().negate().compareTo(sum.getChiefAmount().add(sum.getInterest()))!=0)
							{
								flag = false;
								appendMsg(sBuilder, Borrower.class, borrower.getId(), borrower.getThirdPartyAccount(), "冻结金额与现金流验证错误,冻结金额:"+account.getFreeze().toString()+",现金流:"+sum);
							}
							
							if(flag==false){
								errorborrower++;
							}else{
								successborrower++;
							}
						}
					}
				}catch(Throwable e)
				{
					logger.error(e.getMessage(),e);
					sBuilder.append(e.getMessage());
				}
				
				sBuilder.append("Lender account: total:"+totallender+", success:"+successlender+", error:"+errorlender).append(NEWLINE);
				sBuilder.append("Borrower account: total:"+totalborrower+", success:"+successborrower+", error:"+errorborrower).append(NEWLINE);
				
				return sBuilder.toString();
			}
		});
		new Thread(future).start();
	}
	private static boolean compareAccount(String str,BigDecimal bigDecimal)
	{
		
		if(StringUtil.isEmpty(str)|| !StringUtil.isDigit(str))
			return false;
		if(new BigDecimal(str).compareTo(bigDecimal)==0)
				return true;
		return false;
	}
	private static void appendMsg(StringBuilder sBuilder,Class cls,Integer id,String thirdPartyAccount,String msg)
	{
		sBuilder.append(cls.getSimpleName()).append("[").append("id:").append(id).append(",")
		.append("thirdPartyAccount:").append(thirdPartyAccount).append("]").append(" ").append(msg).append(NEWLINE);
	}
	@Override
	public boolean finished() {
		return (future == null || future.isDone()) ? true : false;
	}

	@Override
	public String getReport() {
		if (future == null)
			return null;
		try {
			return future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

}
