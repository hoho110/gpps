package gpps.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateMain {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	public static void main(String args[]){
		Integer bid = CreateBorrower.create(context);
		Integer series_wj = CreateProductSeries.createWJ(context);
		Integer series_jh = CreateProductSeries.createJH(context);
		Integer series_jq = CreateProductSeries.createJQ(context);
		Integer order1 = CreateOrder.createSingleOrder(context, "政府绿化项目融资", bid, "政府绿化项目融资详细介绍，XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		Integer order2 = CreateOrder.createSingleOrder(context, "计算机采购项目融资", bid, "计算机采购项目融资XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		Integer order3 = CreateOrder.createSingleOrder(context, "市政道路建设项目融资", bid, "市政道路建设项目融资XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		Integer order4 = CreateOrder.createSingleOrder(context, "办公用品采购项目融资", bid, "办公用品采购项目融资XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		CreateProduct.createSingle(context, order1, series_wj, 0, 800000, 0.08, 1000);
		CreateProduct.createSingle(context, order1, series_jh, 0, 150000, 0.16, 2000);
		CreateProduct.createSingle(context, order1, series_jq, 1, 50000, 0.24, 10000);
		
		CreateProduct.createSingle(context, order2, series_wj, 0, 160000, 0.1, 1000);
		CreateProduct.createSingle(context, order2, series_jh, 0, 100000, 0.15, 2000);
		CreateProduct.createSingle(context, order2, series_jq, 1, 8000, 0.26, 10000);
		
		
		CreateProduct.createSingle(context, order3, series_wj, 0, 500000, 0.12, 1000);
		CreateProduct.createSingle(context, order3, series_jh, 0, 190000, 0.18, 5000);
		
		CreateProduct.createSingle(context, order4, series_jh, 0, 800000, 0.15, 2000);
		
//		BorrowerHandle.startOrderFinancing(context, 2);
		
//		BorrowerHandle.startProductRepay(context, 1);
//		BorrowerHandle.startProductRepay(context, 2);
//		BorrowerHandle.startProductRepay(context, 3);
//		BorrowerHandle.startOrderRepay(context, 1);
		
		System.exit(0);
	}
}
