package gpps.service;

import java.util.Date;

import gpps.model.Letter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateLetter {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	public static void main(String args[]){
		ILetterService lservice = context.getBean(ILetterService.class);
//		for(int i=0; i<5; i++)
//		{
		
		Integer bid = 10002;
		
		Letter letter = new Letter();
		letter.setContent("尊敬的企业用户【郑州博文源智能化工程有限公司】:<br>企业已经净调完毕并审核通过！！");
		letter.setTitle("站内信-企业净调通过");
		letter.setCreatetime((new Date()).getTime());
		letter.setMarkRead(0);
		letter.setReceiverId(bid);
		letter.setReceivertype(Letter.RECEIVERTYPE_BORROWER);
		lservice.create(letter);
		
		Letter letter2 = new Letter();
		letter2.setContent("尊敬的企业用户【郑州博文源智能化工程有限公司】:<br>您的融资申请已经审核通过，产品已处于预览状态！！");
		letter2.setTitle("站内信-融资审核通过");
		letter2.setCreatetime((new Date()).getTime());
		letter2.setMarkRead(0);
		letter2.setReceiverId(bid);
		letter2.setReceivertype(Letter.RECEIVERTYPE_BORROWER);
		lservice.create(letter2);
		
		Letter letter3 = new Letter();
		letter3.setContent("尊敬的企业用户【郑州博文源智能化工程有限公司】:<br>您的订单【博文源1401】启动融资未成功，请您先开通第三方账户才可启动融资！！");
		letter3.setTitle("站内信-启动融资");
		letter3.setCreatetime((new Date()).getTime());
		letter3.setMarkRead(0);
		letter3.setReceiverId(bid);
		letter3.setReceivertype(Letter.RECEIVERTYPE_BORROWER);
		lservice.create(letter3);
//		}
		System.exit(0);
	}
}
