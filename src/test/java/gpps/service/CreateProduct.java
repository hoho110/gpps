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
		pro.setId(2);
		pro.setCreatetime((new Date()).getTime());
		pro.setExpectAmount(new BigDecimal(10000));
		pro.setGovermentorderId(2);
		pro.setLevelToBuy(3);
		pro.setProductseriesId(1);
		pro.setRate(new BigDecimal(0.08));
		pro.setRealAmount(new BigDecimal(0));
		pro.setMinimum(1000);
		pro.setState(1);
		product.create(pro);
		
		Product pro2 = new Product();
		pro2.setId(3);
		pro2.setCreatetime((new Date()).getTime());
		pro2.setExpectAmount(new BigDecimal(10000));
		pro2.setGovermentorderId(2);
		pro2.setLevelToBuy(3);
		pro2.setProductseriesId(2);
		pro2.setRate(new BigDecimal(0.15));
		pro2.setRealAmount(new BigDecimal(0));
		pro2.setMinimum(5000);
		pro2.setState(1);
		product.create(pro2);
		
		Product pro3 = new Product();
		pro3.setId(4);
		pro3.setCreatetime((new Date()).getTime());
		pro3.setExpectAmount(new BigDecimal(10000));
		pro3.setGovermentorderId(2);
		pro3.setLevelToBuy(3);
		pro3.setProductseriesId(3);
		pro3.setRate(new BigDecimal(0.2));
		pro3.setRealAmount(new BigDecimal(0));
		pro3.setMinimum(10000);
		pro3.setState(1);
		product.create(pro3);
	}
}
