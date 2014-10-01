package gpps.service;

import java.util.Date;

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
		long istart1 = (new Date(2014-1900, 9, 3)).getTime();
		long fstart1 = (new Date(2014-1900, 9, 1, 8, 0, 0)).getTime();
		long fend1 = (new Date(2014-1900, 9, 2, 18, 0, 0)).getTime();
		Integer order1 = CreateOrder.createSingleOrder(context, "政府绿化项目融资", bid, "政府绿化项目融资详细介绍，XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", fstart1, fend1, istart1);
		
		long istart2 = (new Date(2014-1900, 9, 6)).getTime();
		long fstart2 = (new Date(2014-1900, 9, 3, 8, 0, 0)).getTime();
		long fend2 = (new Date(2014-1900, 9, 5, 18, 0, 0)).getTime();
		Integer order2 = CreateOrder.createSingleOrder(context, "计算机采购项目融资", bid, "计算机采购项目融资XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", fstart2, fend2, istart2);
		
		long istart3 = (new Date(2014-1900, 9, 9)).getTime();
		long fstart3 = (new Date(2014-1900, 9, 6, 8, 0, 0)).getTime();
		long fend3 = (new Date(2014-1900, 9, 8, 18, 0, 0)).getTime();
		Integer order3 = CreateOrder.createSingleOrder(context, "市政道路建设项目融资", bid, "市政道路建设项目融资XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", fstart3, fend3, istart3);
		
		long istart4 = (new Date(2014-1900, 9, 15)).getTime();
		long fstart4 = (new Date(2014-1900, 9, 12, 8, 0, 0)).getTime();
		long fend4 = (new Date(2014-1900, 9, 14, 18, 0, 0)).getTime();
		Integer order4 = CreateOrder.createSingleOrder(context, "办公用品采购项目融资", bid, "办公用品采购项目融资XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", fstart4, fend4, istart4);
		
		long iend11 = (new Date(2014-1900, 12, 3)).getTime();
		long iend12 = (new Date(2015-1900, 3, 3)).getTime();
		long iend13 = (new Date(2015-1900, 9, 3)).getTime();
		CreateProduct.createSingle(context, order1, series_wj, 0, 800000, 0.08, 1000, 100, iend11);
		CreateProduct.createSingle(context, order1, series_jh, 0, 150000, 0.16, 2000, 200, iend12);
		CreateProduct.createSingle(context, order1, series_jq, 1, 50000, 0.24, 10000, 1000, iend13);
		
		
		long iend21 = (new Date(2014-1900, 12, 6)).getTime();
		long iend22 = (new Date(2015-1900, 3, 6)).getTime();
		long iend23 = (new Date(2015-1900, 9, 6)).getTime();
		CreateProduct.createSingle(context, order2, series_wj, 0, 160000, 0.1, 1000, 100, iend21);
		CreateProduct.createSingle(context, order2, series_jh, 0, 100000, 0.15, 2000, 200, iend22);
		CreateProduct.createSingle(context, order2, series_jq, 1, 8000, 0.26, 10000, 1000, iend23);
		
		
		long iend31 = (new Date(2014-1900, 12, 9)).getTime();
		long iend32 = (new Date(2015-1900, 3, 9)).getTime();
		CreateProduct.createSingle(context, order3, series_wj, 0, 500000, 0.12, 1000, 100, iend31);
		CreateProduct.createSingle(context, order3, series_jh, 0, 190000, 0.18, 5000, 200, iend32);
		
		long iend41 = (new Date(2015-1900, 3, 15)).getTime();
		CreateProduct.createSingle(context, order4, series_jh, 0, 800000, 0.15, 2000, 200, iend41);
		
//		BorrowerHandle.startOrderFinancing(context, 2);
		
//		BorrowerHandle.startProductRepay(context, 1);
//		BorrowerHandle.startProductRepay(context, 2);
//		BorrowerHandle.startProductRepay(context, 3);
//		BorrowerHandle.startOrderRepay(context, 1);
		
		System.exit(0);
	}
}
