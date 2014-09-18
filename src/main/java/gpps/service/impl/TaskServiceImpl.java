package gpps.service.impl;

import gpps.dao.IBorrowerDao;
import gpps.dao.ICashStreamDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.ILenderDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductDao;
import gpps.dao.ISubmitDao;
import gpps.dao.ITaskDao;
import gpps.model.Borrower;
import gpps.model.GovermentOrder;
import gpps.model.Lender;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.Submit;
import gpps.model.Task;
import gpps.service.IAccountService;
import gpps.service.IPayBackService;
import gpps.service.ITaskService;
import gpps.service.exception.IllegalConvertException;

import java.math.BigDecimal;
import java.util.List;
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
					//TODO 外层添加异常捕捉放置循环跳出
					Task task=queue.peek();//只取不移除，当任务执行完成后移除
					if(task==null)
					{
						try {
							Thread.sleep(60*1000);
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
				}
			}
		};
		taskThread.setName("TaskThread");
		taskThread.start();
	}

	private void execute(Task task, boolean interrupted) {
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
		//TODO 考虑打断情况
		logger.info("开始执行产品id="+task.getProductId()+"的支付任务taskID="+task.getId());
		List<Submit> submits=submitDao.findAllByProductAndState(task.getProductId(), Submit.STATE_COMPLETEPAY);
		if(submits==null||submits.size()==0)
			return;
		Product product=productDao.find(task.getProductId());
		GovermentOrder order=govermentOrderDao.find(product.getGovermentorderId());
		Borrower borrower=borrowerDao.find(order.getBorrowerId());
		for(Submit submit:submits)
		{
			Lender lender=lenderDao.find(submit.getLenderId());
			try {
				accountService.pay(lender.getAccountId(), borrower.getAccountId(),submit.getAmount(), submit.getId(), "支付");
			} catch (IllegalConvertException e) {
				logger.error(e.getMessage(),e);
			}
			logger.debug("支付任务["+task.getId()+"],Lender["+lender.getId()+"]为Submit["+submit.getId()+"]支付了"+submit.getAmount()+"元");
		}
		logger.info("支付任务["+task.getId()+"]完毕，涉及Submit"+submits.size()+"个");
	}
	private void executeQuitFinancingTask(Task task,boolean interrupted)
	{
		//TODO 考虑打断情况
		logger.info("开始执行产品id="+task.getProductId()+"的流标任务taskID="+task.getId());
		List<Submit> submits=submitDao.findAllByProductAndState(task.getProductId(), Submit.STATE_COMPLETEPAY);
		if(submits==null||submits.size()==0)
			return;
		for(Submit submit:submits)
		{
			Lender lender=lenderDao.find(submit.getLenderId());
			try {
				accountService.unfreezeLenderAccount(lender.getAccountId(), submit.getAmount(), submit.getId(), "流标");
			} catch (IllegalConvertException e) {
				logger.error(e.getMessage(),e);
			}
			logger.debug("流标任务["+task.getId()+"],Lender["+lender.getId()+"]从Submit["+submit.getId()+"]解冻了"+submit.getAmount()+"元");
		}
		logger.info("流标任务["+task.getId()+"]完毕，涉及Submit"+submits.size()+"个");
	}
	private void executeRepayTask(Task task,boolean interrupted)
	{
		//TODO 考虑打断情况
		logger.info("开始执行产品id="+task.getProductId()+"的还款任务taskID="+task.getId());
		List<Submit> submits=submitDao.findAllByProductAndState(task.getProductId(), Submit.STATE_COMPLETEPAY);
		if(submits==null||submits.size()==0)
			return;
		PayBack payBack=payBackDao.find(task.getPayBackId());
		for(Submit submit:submits)
		{
			Lender lender=lenderDao.find(submit.getLenderId());
			BigDecimal lenderChiefAmount=payBack.getChiefAmount().multiply(submit.getAmount()).divide(PayBack.BASELINE, 2, BigDecimal.ROUND_DOWN);
			BigDecimal lenderInterest=payBack.getInterest().multiply(submit.getAmount()).divide(PayBack.BASELINE, 2, BigDecimal.ROUND_DOWN);
			try {
			
				accountService.repay(lender.getAccountId(), payBack.getBorrowerAccountId(), lenderChiefAmount, lenderInterest, submit.getId(), payBack.getId(), "还款");
			} catch (IllegalConvertException e) {
				logger.error(e.getMessage(),e);
			}
			logger.debug("还款任务["+task.getId()+"],Lender["+lender.getId()+"]因Submit["+submit.getId()+"]获取还款本金"+lenderChiefAmount+"元,利息"+lenderInterest+"元");
		}
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
