package gpps.service.impl;

import gpps.dao.ICashStreamDao;
import gpps.dao.IProductDao;
import gpps.dao.ISubmitDao;
import gpps.dao.ITaskDao;
import gpps.model.Submit;
import gpps.model.Task;
import gpps.service.ITaskService;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskServiceImpl implements ITaskService {
//	@Autowired
	ITaskDao taskDao;
	Logger logger = Logger.getLogger(this.getClass());
	BlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
//	@Autowired
	IProductDao productDao;
//	@Autowired
	ISubmitDao submitDao;
//	@Autowired
	ICashStreamDao cashStreamDao;
//	@PostConstruct
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
		List<Submit> submits=submitDao.findAllByProductAndState(task.getProductId(), Submit.STATE_COMPLETEPAY);
	}
	private void executeQuitFinancingTask(Task task,boolean interrupted)
	{
	}
	private void executeRepayTask(Task task,boolean interrupted)
	{
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
