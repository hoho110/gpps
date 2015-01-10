package gpps.service.impl;

import gpps.dao.IBorrowerAccountDao;
import gpps.dao.IBorrowerDao;
import gpps.dao.ICashStreamDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.ILenderDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductDao;
import gpps.dao.ISubmitDao;
import gpps.dao.ITaskDao;
import gpps.model.Borrower;
import gpps.model.CashStream;
import gpps.model.GovermentOrder;
import gpps.model.Lender;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.Submit;
import gpps.model.Task;
import gpps.service.IAccountService;
import gpps.service.IPayBackService;
import gpps.service.IProductService;
import gpps.service.ITaskService;
import gpps.service.exception.IllegalConvertException;
import gpps.service.exception.SMSException;
import gpps.service.message.IMessageService;
import gpps.service.thirdpay.IThirdPaySupportService;
import gpps.service.thirdpay.Transfer.LoanJson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskServiceImpl implements ITaskService {
	@Autowired
	ITaskDao taskDao;
	Logger logger = Logger.getLogger(this.getClass());
	BlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
	@Autowired
	IProductDao productDao;
	@Autowired
	IProductService productService;
	@Autowired
	ISubmitDao submitDao;
	@Autowired
	ICashStreamDao cashStreamDao;
	@Autowired
	IAccountService accountService;
	@Autowired
	ILenderDao lenderDao;
	@Autowired
	IBorrowerDao borrowerDao;
	@Autowired
	IGovermentOrderDao govermentOrderDao;
	@Autowired
	IPayBackDao payBackDao;
	@Autowired
	IPayBackService payBackService;
	@Autowired
	IThirdPaySupportService thirdPaySupportService;
	@Autowired
	IBorrowerAccountDao borrowerAccountDao;
	@PostConstruct
	public void init() {
		try {
			List<Task> interruptedTasks = taskDao
					.findByState(Task.STATE_PROCESSING);
			if (interruptedTasks != null && interruptedTasks.size() > 0) {
				logger.info("重新加载" + interruptedTasks.size() + "个中断任务到执行队列");
				for (Task task : interruptedTasks) {
					queue.put(task);
				}
			}
			List<Task> initTasks=taskDao.findByState(Task.STATE_INIT);
			if(initTasks!=null&&initTasks.size()>0)
			{
				logger.info("重新加载" + initTasks.size() + "个未执行任务到执行队列");
				for (Task task : initTasks) {
					queue.put(task);
				}
			}
		} catch (InterruptedException e) {
			logger.error("加载执行任务失败,系统退出,请检查故障原因");
			logger.error(e.getMessage(),e);
			System.exit(-1);
		}
		Thread taskThread=new Thread(){
			public void run()
			{
				logger.info("任务执行线程已启动");
				while(true)
				{
					try
					{
						// 外层添加异常捕捉防止循环跳出
						Task task=queue.peek();//只取不移除，当任务执行完成后移除
						if(task==null)
						{
							try {
								Thread.sleep(1*1000);
								continue;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						logger.info("开始处理任务:"+task);
						boolean interrupted=false;
						if(task.getState()==Task.STATE_INIT)
						{
							taskDao.changeState(task.getId(), Task.STATE_PROCESSING);
							task.setState(Task.STATE_PROCESSING);
						}
						else if(task.getState()==Task.STATE_PROCESSING)
							interrupted=true;
						else if(task.getState()==Task.STATE_FINISH)
						{
							//任务完成，状态未保存成功,基本不会出现
							taskDao.changeState(task.getId(), Task.STATE_FINISH);
							continue;
						}
						execute(task, interrupted);
						task.setState(Task.STATE_FINISH);
						taskDao.changeState(task.getId(), Task.STATE_FINISH);
						queue.poll();
						logger.info("任务:"+task+"处理完毕");
					}catch (Throwable e) {
						logger.error(e.getMessage(),e);
						//执行出错，线程休眠1分钟
						try {
							Thread.sleep(60*1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		};
		taskThread.setName("TaskThread");
		taskThread.start();
	}

	private void execute(Task task, boolean interrupted) throws Exception{
		switch (task.getType()) {
		case Task.TYPE_PAY:
			executePayTask(task,interrupted);
			break;
		case Task.TYPE_QUITFINANCING:
			executeQuitFinancingTask(task,interrupted);
			break;
		case Task.TYPE_REPAY:
			executeRepayTask(task, interrupted);
			break;
		default:
			throw new RuntimeException("不支持的任务类型");
		}
	}
	private void executePayTask(Task task,boolean interrupted)
	{
		//考虑打断情况
		logger.info("开始执行产品id="+task.getProductId()+"的支付任务taskID="+task.getId());
		if(interrupted)
			logger.info("该支付任务曾被打断过，现在继续执行");
		List<Submit> submits=submitDao.findAllByProductAndState(task.getProductId(), Submit.STATE_COMPLETEPAY);
		if(submits==null||submits.size()==0)
			return;
		List<String> loanNos=new ArrayList<String>();
		loop:for(Submit submit:submits)
		{
			CashStream freezeCS=null;
			List<CashStream> cashStreams=cashStreamDao.findSubmitCashStream(submit.getId());
			for(CashStream cashStream:cashStreams)
			{
				if(cashStream.getAction()==CashStream.ACTION_PAY&&cashStream.getState()==CashStream.STATE_SUCCESS)
				{
					logger.debug("支付任务["+task.getId()+"],Submit["+submit.getId()+"]已执行过。");
					continue loop;
				}
				if(cashStream.getAction()==CashStream.ACTION_FREEZE&&cashStream.getState()==CashStream.STATE_SUCCESS)
					freezeCS=cashStream;
			}
			if(freezeCS==null)
				continue;
			loanNos.add(freezeCS.getLoanNo());
		}
		thirdPaySupportService.check(loanNos, 1);
		logger.info("支付任务["+task.getId()+"]完毕，涉及Submit"+submits.size()+"个");
	}
	private void executeQuitFinancingTask(Task task,boolean interrupted)
	{
		// 考虑打断情况
		logger.info("开始执行产品id="+task.getProductId()+"的流标任务taskID="+task.getId());
		if(interrupted)
			logger.info("该支付任务曾被打断过，现在继续执行");
		List<Submit> submits=submitDao.findAllByProductAndState(task.getProductId(), Submit.STATE_COMPLETEPAY);
		if(submits==null||submits.size()==0)
			return;
		List<String> loanNos=new ArrayList();
		loop:for(Submit submit:submits)
		{
			CashStream freezeCS=null;
			List<CashStream> cashStreams=cashStreamDao.findSubmitCashStream(submit.getId());
			for(CashStream cashStream:cashStreams)
			{
				if(cashStream.getAction()==CashStream.ACTION_UNFREEZE&&cashStream.getState()==CashStream.STATE_SUCCESS)
				{
					logger.debug("流标任务["+task.getId()+"],Submit["+submit.getId()+"]已执行过。");
					continue loop;
				}
				if(cashStream.getAction()==CashStream.ACTION_FREEZE&&cashStream.getState()==CashStream.STATE_SUCCESS)
					freezeCS=cashStream;
			}
			loanNos.add(freezeCS.getLoanNo());
		}
		thirdPaySupportService.check(loanNos, 2);
		logger.info("流标任务["+task.getId()+"]完毕，涉及Submit"+submits.size()+"个");
	}
	private void executeRepayTask(Task task,boolean interrupted) throws Exception
	{
		//考虑打断情况
		logger.info("开始执行产品id="+task.getProductId()+"的还款任务taskID="+task.getId());
		if(interrupted)
			logger.info("该支付任务曾被打断过，现在继续执行");
		List<Submit> submits=submitDao.findAllByProductAndState(task.getProductId(), Submit.STATE_COMPLETEPAY);
		if(submits==null||submits.size()==0)
			return;
		PayBack payBack=payBackService.find(task.getPayBackId());
		Product product=productDao.find(payBack.getProductId());
		GovermentOrder order=govermentOrderDao.find(product.getGovermentorderId());
		Borrower borrower=borrowerDao.find(order.getBorrowerId());
		BigDecimal totalChiefAmount=payBack.getChiefAmount();
		BigDecimal totalInterest=payBack.getInterest();
		//TODO 取付款人乾多多标识，收款人乾多多标识，网贷平台订单号，金额（本金+利息）
		//accountService.repay 去掉changeCashStreamState
		List<LoanJson> loanJsons=new ArrayList<LoanJson>();
		loop:for(int i=0;i<submits.size();i++)
		{
			Submit submit=submits.get(i);
			if(interrupted)
			{
				List<CashStream> cashStreams=cashStreamDao.findRepayCashStream(submit.getId(), payBack.getId());
				if(cashStreams!=null&&cashStreams.size()>0)
				{
					CashStream cashStream=cashStreams.get(0);
					totalChiefAmount.subtract(cashStream.getChiefamount());
					totalInterest.subtract(cashStream.getInterest());
					logger.debug("还款任务["+task.getId()+"],Submit["+submit.getId()+"]已执行过。");
					continue loop;
				}
			}
			Lender lender=lenderDao.find(submit.getLenderId());
			BigDecimal lenderChiefAmount=null;
			BigDecimal lenderInterest=null;
//			if(i==(submits.size()-1))
//			{
//				lenderChiefAmount=totalChiefAmount;
//				lenderInterest=totalInterest;
//			}
//			else
//			{
				if(payBack.getType()==PayBack.TYPE_LASTPAY)
				{
					List<CashStream> cashStreams=cashStreamDao.findRepayCashStream(submit.getId(), null);
					BigDecimal repayedChiefAmount=BigDecimal.ZERO;
					if(cashStreams!=null&&cashStreams.size()>0)
					{
						for(CashStream cashStream:cashStreams)
						{
							repayedChiefAmount=repayedChiefAmount.add(cashStream.getChiefamount());
						}
					}
					lenderChiefAmount=submit.getAmount().subtract(repayedChiefAmount);
				}
				else {
					if(i==(submits.size()-1))
					{
						lenderChiefAmount=totalChiefAmount;
					}
					else
						lenderChiefAmount=payBack.getChiefAmount().multiply(submit.getAmount()).divide(product.getRealAmount(), 2, BigDecimal.ROUND_UP);
				}
				lenderInterest=payBack.getInterest().multiply(submit.getAmount()).divide(product.getRealAmount(), 2, BigDecimal.ROUND_DOWN);
				totalChiefAmount=totalChiefAmount.subtract(lenderChiefAmount);
				totalInterest=totalInterest.subtract(lenderInterest);
//			}
			try {
				Integer cashStreamId=accountService.repay(lender.getAccountId(), payBack.getBorrowerAccountId(), lenderChiefAmount, lenderInterest, submit.getId(), payBack.getId(), "还款");
				LoanJson loadJson=new LoanJson();
				loadJson.setLoanOutMoneymoremore(borrower.getThirdPartyAccount());
				loadJson.setLoanInMoneymoremore(lender.getThirdPartyAccount());
				loadJson.setOrderNo(String.valueOf(cashStreamId));
				loadJson.setBatchNo(String.valueOf(product.getId()));
				loadJson.setAmount(lenderChiefAmount.add(lenderInterest).toString());
				loanJsons.add(loadJson);
			} catch (IllegalConvertException e) {
				logger.error(e.getMessage(),e);
			}
			logger.debug("还款任务["+task.getId()+"],Lender["+lender.getId()+"]因Submit["+submit.getId()+"]获取还款本金"+lenderChiefAmount+"元,利息"+lenderInterest+"元");
		}
		BigDecimal change=totalChiefAmount.add(totalInterest);
		if(change.compareTo(BigDecimal.ZERO)>0 && change.compareTo(new BigDecimal(0.01*submits.size()))<=0)
		{
			//有余额则放入自有账户中
			Integer cashStreamId=accountService.storeChange(payBack.getBorrowerAccountId(),payBack.getId(),totalChiefAmount,totalInterest, "存零");
			LoanJson loadJson=new LoanJson();
			loadJson.setLoanOutMoneymoremore(borrower.getThirdPartyAccount());
			loadJson.setLoanInMoneymoremore(thirdPaySupportService.getPlatformMoneymoremore());
			loadJson.setOrderNo(String.valueOf(cashStreamId));
			loadJson.setBatchNo(String.valueOf(product.getId()));
			loadJson.setAmount(change.toString());
			loanJsons.add(loadJson);
		}else if(change.compareTo(BigDecimal.ZERO)==0){
			//等于0意味着刚刚好，什么都不用做
		}
		else{
			logger.error("还款"+payBack.getId()+"金额计算有问题，请检查！");
			throw new Exception("还款"+payBack.getId()+"金额计算有问题，请检查！");
		}
		thirdPaySupportService.repay(loanJsons, payBack);
		payBackService.changeState(payBack.getId(), PayBack.STATE_FINISHREPAY);
		logger.info("还款任务["+task.getId()+"]完毕，涉及Submit"+submits.size()+"个");
	}
	@Override
	public void submit(Task task) {
		task.setState(Task.STATE_INIT);
		task.setCreateTime(System.currentTimeMillis());
		taskDao.create(task);
		try {
			queue.put(task);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
}
