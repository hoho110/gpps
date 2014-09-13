package gpps.service;

import java.math.BigDecimal;
import java.util.Date;

import gpps.dao.IProductDao;
import gpps.model.Product;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateProduct {
	public static void create(ApplicationContext context){
		IProductDao product = context.getBean(IProductDao.class);
		Product pro = new Product();
		pro.setCreatetime((new Date()).getTime());
		pro.setExpectAmount(new BigDecimal(100000));
		pro.setGovermentorderId(4);
		pro.setLevelToBuy(2);
		pro.setProductseriesId(1);
		pro.setRate(new BigDecimal(0.06));
		pro.setRealAmount(new BigDecimal(120000));
		pro.setMinimum(1000);
		pro.setState(4);
		product.create(pro);
		
		Product pro2 = new Product();
		pro2.setCreatetime((new Date()).getTime());
		pro2.setExpectAmount(new BigDecimal(200000));
		pro2.setGovermentorderId(4);
		pro2.setLevelToBuy(3);
		pro2.setProductseriesId(2);
		pro2.setRate(new BigDecimal(0.15));
		pro2.setRealAmount(new BigDecimal(100000));
		pro2.setMinimum(2000);
		pro2.setState(4);
		product.create(pro2);
		
		Product pro3 = new Product();
		pro3.setCreatetime((new Date()).getTime());
		pro3.setExpectAmount(new BigDecimal(10000));
		pro3.setGovermentorderId(4);
		pro3.setLevelToBuy(3);
		pro3.setProductseriesId(3);
		pro3.setRate(new BigDecimal(0.2));
		pro3.setRealAmount(new BigDecimal(0));
		pro3.setMinimum(10000);
		pro3.setState(4);
		product.create(pro3);
	}
}
