package gpps.service;

import java.math.BigDecimal;
import java.util.Date;

import gpps.dao.IProductDao;
import gpps.model.Product;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateProduct {
	
	public static Integer createSingle(ApplicationContext context, Integer oid, Integer sid, int leveltobuy, int amount, double rate, int min, int miniadd, long iend){
		IProductService productService = context.getBean(IProductService.class);
		Product pro = new Product();
		pro.setCreatetime((new Date()).getTime());
		pro.setExpectAmount(new BigDecimal(amount));
		pro.setGovermentorderId(oid);
		pro.setLevelToBuy(leveltobuy);
		pro.setProductseriesId(sid);
		pro.setRate(new BigDecimal(rate));
		pro.setRealAmount(new BigDecimal(0));
		pro.setMinimum(min);
		pro.setState(1);
		pro.setMiniAdd(miniadd);
		pro.setIncomeEndtime(iend);
		productService.create(pro);
		return pro.getId();
	}	
	
	public static void create(ApplicationContext context){
		IProductDao product = context.getBean(IProductDao.class);
		Product pro = new Product();
		pro.setCreatetime((new Date()).getTime());
		pro.setExpectAmount(new BigDecimal(100000));
		pro.setGovermentorderId(1);
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
		pro2.setGovermentorderId(1);
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
		pro3.setGovermentorderId(1);
		pro3.setLevelToBuy(4);
		pro3.setProductseriesId(3);
		pro3.setRate(new BigDecimal(0.2));
		pro3.setRealAmount(new BigDecimal(0));
		pro3.setMinimum(10000);
		pro3.setState(4);
		product.create(pro3);
		
		
		Product pro11 = new Product();
		pro11.setCreatetime((new Date()).getTime());
		pro11.setExpectAmount(new BigDecimal(120000));
		pro11.setGovermentorderId(2);
		pro11.setLevelToBuy(2);
		pro11.setProductseriesId(1);
		pro11.setRate(new BigDecimal(0.08));
		pro11.setRealAmount(new BigDecimal(120000));
		pro11.setMinimum(1000);
		pro11.setState(1);
		product.create(pro11);
		
		Product pro12 = new Product();
		pro12.setCreatetime((new Date()).getTime());
		pro12.setExpectAmount(new BigDecimal(200000));
		pro12.setGovermentorderId(2);
		pro12.setLevelToBuy(3);
		pro12.setProductseriesId(2);
		pro12.setRate(new BigDecimal(0.16));
		pro12.setRealAmount(new BigDecimal(200000));
		pro12.setMinimum(2000);
		pro12.setState(1);
		product.create(pro12);
		
		Product pro13 = new Product();
		pro13.setCreatetime((new Date()).getTime());
		pro13.setExpectAmount(new BigDecimal(100000));
		pro13.setGovermentorderId(2);
		pro13.setLevelToBuy(4);
		pro13.setProductseriesId(3);
		pro13.setRate(new BigDecimal(0.24));
		pro13.setRealAmount(new BigDecimal(100000));
		pro13.setMinimum(10000);
		pro13.setState(1);
		product.create(pro13);
		
		
		Product pro21 = new Product();
		pro21.setCreatetime((new Date()).getTime());
		pro21.setExpectAmount(new BigDecimal(300000));
		pro21.setGovermentorderId(3);
		pro21.setLevelToBuy(2);
		pro21.setProductseriesId(1);
		pro21.setRate(new BigDecimal(0.1));
		pro21.setRealAmount(new BigDecimal(0));
		pro21.setMinimum(1000);
		pro21.setState(2);
		product.create(pro21);
		
		Product pro22 = new Product();
		pro22.setCreatetime((new Date()).getTime());
		pro22.setExpectAmount(new BigDecimal(1200000));
		pro22.setGovermentorderId(3);
		pro22.setLevelToBuy(3);
		pro22.setProductseriesId(2);
		pro22.setRate(new BigDecimal(0.18));
		pro22.setRealAmount(new BigDecimal(0));
		pro22.setMinimum(2000);
		pro22.setState(2);
		product.create(pro22);
		
		Product pro23 = new Product();
		pro23.setCreatetime((new Date()).getTime());
		pro23.setExpectAmount(new BigDecimal(10000));
		pro23.setGovermentorderId(3);
		pro23.setLevelToBuy(4);
		pro23.setProductseriesId(3);
		pro23.setRate(new BigDecimal(0.24));
		pro23.setRealAmount(new BigDecimal(0));
		pro23.setMinimum(10000);
		pro23.setState(2);
		product.create(pro23);
	}
}
