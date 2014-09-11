package gpps.service;

import java.math.BigDecimal;
import java.util.Date;

import gpps.dao.IProductDao;
import gpps.model.Product;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateProduct {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	public static void main(String args[]){
		IProductDao product = context.getBean(IProductDao.class);
		Product pro = new Product();
		pro.setCreatetime((new Date()).getTime());
		pro.setExpectAmount(new BigDecimal(10000));
		pro.setGovermentorderId(2);
		pro.setLevelToBuy(3);
		pro.setProductseriesId(2);
		pro.setRate(new BigDecimal(0.16));
		pro.setRealAmount(new BigDecimal(0));
		pro.setState(1);
		product.create(pro);
	}
}
