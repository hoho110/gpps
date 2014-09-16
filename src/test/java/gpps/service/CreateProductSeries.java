package gpps.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import gpps.dao.IProductSeriesDao;
import gpps.model.ProductSeries;
import gpps.model.ref.TagCol;
import gpps.model.ref.TagCol.Item;
public class CreateProductSeries {
	
	public static Integer createWJ(ApplicationContext context){
		IProductSeriesService productSeriesService=context.getBean(IProductSeriesService.class);
		ProductSeries productSeries=new ProductSeries();
		productSeries.setTitle("稳健型");
		
		TagCol tc = new TagCol();
		List<Item> its = new ArrayList<TagCol.Item>();
		its.add(new Item("担保", "如何如何担保"));
		its.add(new Item("按日还款", "如何如何按日还款"));
		tc.setItems(its);
		productSeries.setTagCol(tc);
		productSeries.setDescription("本类型产品具有低风险，高回款流动性的特点，并由专业担保机构进行本金担保，适合稳健型用户的参与。");
		productSeriesService.create(productSeries);
		return productSeries.getId();
	}
	
	public static Integer createJH(ApplicationContext context){
		IProductSeriesService productSeriesService=context.getBean(IProductSeriesService.class);
		ProductSeries productSeries=new ProductSeries();
		productSeries.setTitle("均衡型");
		
		TagCol tc = new TagCol();
		List<Item> its = new ArrayList<TagCol.Item>();
		its.add(new Item("按月摊还本息", "如何如何按月还本付息"));
		its.add(new Item("高收益", "如何如何高收益"));
		tc.setItems(its);
		productSeries.setTagCol(tc);
		productSeries.setDescription("本类型产品具有中等偏上的收益，按月回款的特点，收益流动性和风险都适中，适合平衡型的的客户参与。");
		productSeriesService.create(productSeries);
		return productSeries.getId();
	}
	
	
	public static Integer createJQ(ApplicationContext context){
		IProductSeriesService productSeriesService=context.getBean(IProductSeriesService.class);
		ProductSeries productSeries=new ProductSeries();
		productSeries.setTitle("进取型");
		
		TagCol tc = new TagCol();
		List<Item> its = new ArrayList<TagCol.Item>();
		its.add(new Item("不定期限", "如何如何不定期限"));
		its.add(new Item("超高收益", "如何如何高收益"));
		tc.setItems(its);
		productSeries.setTagCol(tc);
		productSeries.setDescription("本类型产品具有高收益低流动性，到期还本付息的特点，适合追求高收益并能承担一定风险的客户参与。");
		productSeriesService.create(productSeries);
		return productSeries.getId();
	}
}
