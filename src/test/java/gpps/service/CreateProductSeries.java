package gpps.service;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import gpps.dao.IProductSeriesDao;
import gpps.model.ProductSeries;
public class CreateProductSeries {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	public static void main(String args[]){
	IProductSeriesDao productSeriesDao=context.getBean(IProductSeriesDao.class);
	ProductSeries productSeries=new ProductSeries();
	productSeries.setTitle("稳健型");
	productSeries.setMessage("担保");
	productSeries.setDecstription("本类型产品具有低风险，高回款流动性的特点，并由专业担保机构进行本金担保，适合稳健型用户的参与。");
	productSeriesDao.create(productSeries);
	
	ProductSeries productSeries2=new ProductSeries();
	productSeries2.setTitle("均衡型");
	productSeries2.setMessage("按月摊还本息");
	productSeries2.setDecstription("本类型产品具有中等偏上的收益，按月回款的特点，收益流动性和风险都适中，适合平衡型的的客户参与。");
	productSeriesDao.create(productSeries2);
	
	ProductSeries productSeries3=new ProductSeries();
	productSeries3.setTitle("进取型");
	productSeries3.setMessage("高收益");
	productSeries3.setDecstription("本类型产品具有高收益低流动性，到期还本付息的特点，适合追求高收益并能承担一定风险的客户参与。");
	productSeriesDao.create(productSeries3);
	}
}
