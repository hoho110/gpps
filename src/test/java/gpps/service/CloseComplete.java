package gpps.service;

import gpps.model.ProductAction;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CloseComplete {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	public static void main(String args[]) throws Exception{
		IGovermentOrderService order = context.getBean(IGovermentOrderService.class);
		order.closeComplete(1);
		System.exit(0);
	}
}
