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
		or.setId(5);
		or.setBorrowerId(1);
		or.setCreatetime((new Date()).getTime());
		or.setFinancingStarttime((new Date()).getTime()+24*3600*1000);
		or.setFinancingEndtime((new Date()).getTime()+5*24*3600*1000);
		or.setTitle("政府采购项目一");
		or.setIncomeStarttime((new Date()).getTime()+6*24*3600*1000);
		or.setIncomeEndtime((new Date()).getTime()+96*24*3600*1000);
		or.setLastModifytime((new Date()).getTime());
		
		or.setState(1);
		order.create(or);
		
		GovermentOrder or2 = new GovermentOrder();
		or2.setId(5);
		or2.setBorrowerId(1);
		or2.setCreatetime((new Date()).getTime());
		or2.setFinancingStarttime((new Date()).getTime()+24*3600*1000);
		or2.setFinancingEndtime((new Date()).getTime()+5*24*3600*1000);
		or2.setTitle("市政绿化工程项目");
		or2.setIncomeStarttime((new Date()).getTime()+6*24*3600*1000);
		or2.setIncomeEndtime((new Date()).getTime()+96*24*3600*1000);
		or2.setLastModifytime((new Date()).getTime());
		
		or2.setState(1);
		order.create(or2);
		
		
		
		GovermentOrder or3 = new GovermentOrder();
		or3.setId(6);
		or3.setBorrowerId(1);
		or3.setCreatetime((new Date()).getTime());
		or3.setFinancingStarttime((new Date()).getTime()+24*3600*1000);
		or3.setFinancingEndtime((new Date()).getTime()+5*24*3600*1000);
		or3.setTitle("办公用品采购项目");
		or3.setIncomeStarttime((new Date()).getTime()+6*24*3600*1000);
		or3.setIncomeEndtime((new Date()).getTime()+96*24*3600*1000);
		or3.setLastModifytime((new Date()).getTime());
		
		or3.setState(2);
		order.create(or3);
		
		return or.getId();
	}
}
