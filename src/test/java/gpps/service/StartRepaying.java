package gpps.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class StartRepaying {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	public static void main(String args[]) throws Exception{
		IProductService proService = context.getBean(IProductService.class);
		IGovermentOrderService orderService = context.getBean(IGovermentOrderService.class);
		proService.startRepaying(7);
		proService.startRepaying(8);
		orderService.startRepaying(3);
		System.exit(0);
	}
}
