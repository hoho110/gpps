package gpps.service;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import gpps.dao.IProductSeriesDao;
import gpps.model.ProductSeries;
public class CreateProductSeries {
	public static void create(ApplicationContext context){
	IProductSeriesDao productSeriesDao=context.getBean(IProductSeriesDao.class);
	ProductSeries productSeries=new ProductSeries();
	productSeries.setId(1);
	productSeries.setTitle("稳健型");
	productSeries.setTag("担保");
	productSeries.setDescription("本类型产品具有低风险，高回款流动性的特点，并由专业担保机构进行本金担保，适合稳健型用户的参与。");
	productSeriesDao.create(productSeries);
	
	ProductSeries productSeries2=new ProductSeries();
	productSeries2.setId(2);
	productSeries2.setTitle("均衡型");
	productSeries2.setTag("按月摊还本息");
	productSeries2.setDescription("本类型产品具有中等偏上的收益，按月回款的特点，收益流动性和风险都适中，适合平衡型的的客户参与。");
	productSeriesDao.create(productSeries2);
	
	ProductSeries productSeries3=new ProductSeries();
	productSeries3.setId(3);
	productSeries3.setTitle("进取型");
	productSeries3.setTag("高收益");
	productSeries3.setDescription("本类型产品具有高收益低流动性，到期还本付息的特点，适合追求高收益并能承担一定风险的客户参与。");
	productSeriesDao.create(productSeries3);
	}
}
