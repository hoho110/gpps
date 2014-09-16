package gpps.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import gpps.TestSupport;
import gpps.model.ProductSeries;
import gpps.model.ref.TagCol;
import gpps.model.ref.TagCol.Item;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class IProductSeriesDaoTest extends TestSupport{
	private static IProductSeriesDao productSeriesDao;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		productSeriesDao=context.getBean(IProductSeriesDao.class);
	}
	@Test
	public void test() {
		ProductSeries productSeries=new ProductSeries();
		productSeries.setTitle("title");
		productSeries.setDescription("description");
		TagCol tagCol=new TagCol();
		List<Item> tags=new ArrayList<Item>();
		tags.add(new Item("有担保","低风险"));
		tags.add(new Item("无担保","搞风险"));
		tagCol.setItems(tags);
		productSeries.setTagCol(tagCol);
		productSeriesDao.create(productSeries);
		productSeries=productSeriesDao.find(productSeries.getId());
		
		
		productSeriesDao.delete(productSeries.getId());
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
}
