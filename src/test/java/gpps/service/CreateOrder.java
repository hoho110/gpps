package gpps.service;

import java.math.BigDecimal;
import java.util.Date;

import gpps.dao.IBorrowerDao;
import gpps.dao.IFinancingRequestDao;
import gpps.dao.IGovermentOrderDao;
import gpps.model.FinancingRequest;
import gpps.model.GovermentOrder;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateOrder {
	
	
	public static Integer createSingleOrder(ApplicationContext context, String title, Integer bid, String description, long fstart, long fend, long istart){
		IGovermentOrderService orderService = context.getBean(IGovermentOrderService.class);
		IFinancingRequestDao financingRequestDao=context.getBean(IFinancingRequestDao.class);
		
		FinancingRequest financingRequest=new FinancingRequest();
		financingRequest.setBorrowerID(bid);
		financingRequest.setApplyFinancingAmount(800000);
		financingRequest.setRate("10%");
		financingRequestDao.create(financingRequest);
		
		GovermentOrder or = new GovermentOrder();
		or.setBorrowerId(bid);
		or.setCreatetime((new Date()).getTime());
		or.setFinancingStarttime(fstart);
		or.setFinancingEndtime(fend);
		
		or.setTitle(title);
		or.setIncomeStarttime(istart);
		or.setLastModifytime((new Date()).getTime());
		
		or.setState(GovermentOrder.STATE_PREPUBLISH);
		or.setDescription(description);
		
		or.setFinancingRequestId(financingRequest.getId());
		or = orderService.create(or);
		return or.getId();
	}
	
	
	
	public static Integer create(ApplicationContext context){
		IGovermentOrderDao order = context.getBean(IGovermentOrderDao.class);
		GovermentOrder or = new GovermentOrder();
		or.setId(5);
		or.setBorrowerId(1);
		or.setCreatetime((new Date()).getTime());
		or.setFinancingStarttime((new Date()).getTime()+24L*3600*1000);
		or.setFinancingEndtime((new Date()).getTime()+5L*24*3600*1000);
		or.setTitle("政府采购项目一");
		or.setIncomeStarttime((new Date()).getTime()+6L*24*3600*1000);
		or.setLastModifytime((new Date()).getTime());
		
		or.setState(1);
		order.create(or);
		
		GovermentOrder or2 = new GovermentOrder();
		or2.setId(5);
		or2.setBorrowerId(1);
		or2.setCreatetime((new Date()).getTime());
		or2.setFinancingStarttime((new Date()).getTime()+24L*3600*1000);
		or2.setFinancingEndtime((new Date()).getTime()+5L*24*3600*1000);
		or2.setTitle("市政绿化工程项目");
		or2.setIncomeStarttime((new Date()).getTime()+6L*24*3600*1000);
		or2.setLastModifytime((new Date()).getTime());
		
		or2.setState(1);
		order.create(or2);
		
		
		
		GovermentOrder or3 = new GovermentOrder();
		or3.setId(6);
		or3.setBorrowerId(1);
		or3.setCreatetime((new Date()).getTime());
		or3.setFinancingStarttime((new Date()).getTime()+24L*3600*1000);
		or3.setFinancingEndtime((new Date()).getTime()+5L*24*3600*1000);
		or3.setTitle("办公用品采购项目");
		or3.setIncomeStarttime((new Date()).getTime()+6L*24*3600*1000);
		or3.setLastModifytime((new Date()).getTime());
		
		or3.setState(2);
		order.create(or3);
		
		return or.getId();
	}
}
