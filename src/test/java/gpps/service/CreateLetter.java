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
		for(int i=0; i<5; i++)
		{
		Letter letter = new Letter();
		letter.setContent("你好啊小美女"+i+"！");
		letter.setCreatetime((new Date()).getTime());
		letter.setMarkRead(0);
		letter.setReceiverId(340);
		letter.setReceivertype(Letter.RECEIVERTYPE_BORROWER);
		lservice.create(letter);
		}
		System.exit(0);
	}
}
