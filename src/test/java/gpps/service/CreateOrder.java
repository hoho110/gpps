package gpps.service;

import java.math.BigDecimal;
import java.util.Date;

import gpps.dao.IBorrowerDao;
import gpps.dao.IGovermentOrderDao;
import gpps.model.GovermentOrder;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateOrder {
	public static Integer create(ApplicationContext context){
		IGovermentOrderDao order = context.getBean(IGovermentOrderDao.class);
		GovermentOrder or = new GovermentOrder();
		or.setId(4);
		or.setBorrowerId(1);
		or.setCreatetime((new Date()).getTime());
		or.setFinancingStarttime((new Date()).getTime()+24*3600*1000);
		or.setFinancingEndtime((new Date()).getTime()+5*24*3600*1000);
		or.setTitle("计算机工程项目");
		or.setIncomeStarttime((new Date()).getTime()+6*24*3600*1000);
		or.setIncomeEndtime((new Date()).getTime()+96*24*3600*1000);
		
		or.setState(4);
		order.create(or);
		return or.getId();
	}
}
