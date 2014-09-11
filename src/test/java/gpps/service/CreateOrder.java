package gpps.service;

import java.util.Date;

import gpps.dao.IBorrowerDao;
import gpps.dao.IGovermentOrderDao;
import gpps.model.GovermentOrder;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateOrder {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	public static void main(String args[]){
		IGovermentOrderDao order = context.getBean(IGovermentOrderDao.class);
		GovermentOrder or = new GovermentOrder();
		or.setBorrowerId(2);
		or.setCreatetime((new Date()).getTime());
		or.setFinancingStarttime((new Date()).getTime()+24*3600*1000);
		or.setFinancingEndtime((new Date()).getTime()+5*24*3600*1000);
		or.setState(1);
		order.create(or);
	}
}
