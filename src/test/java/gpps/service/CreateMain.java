package gpps.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateMain {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	public static void main(String args[]){
		CreateBorrower.create(context);
		CreateProductSeries.create(context);
		CreateOrder.create(context);
		CreateProduct.create(context);
		System.exit(0);
	}
}
