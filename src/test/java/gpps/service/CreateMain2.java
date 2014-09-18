package gpps.service;

import java.util.List;

import gpps.model.Borrower;
import gpps.model.Lender;
import gpps.model.PayBack;
import gpps.service.exception.IllegalConvertException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateMain2 {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	public static void main(String args[]){
		Borrower borrower=BorrowerHandle.create(context);
		Integer series_wj = CreateProductSeries.createWJ(context);
		Integer series_jh = CreateProductSeries.createJH(context);
		Integer series_jq = CreateProductSeries.createJQ(context);
		Integer order1 = CreateOrder.createSingleOrder(context, "政府绿化项目融资", borrower.getId(), "政府绿化项目融资详细介绍，XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		Integer order2 = CreateOrder.createSingleOrder(context, "计算机采购项目融资", borrower.getId(), "计算机采购项目融资XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		Integer order3 = CreateOrder.createSingleOrder(context, "市政道路建设项目融资", borrower.getId(), "市政道路建设项目融资XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		Integer order4 = CreateOrder.createSingleOrder(context, "办公用品采购项目融资", borrower.getId(), "办公用品采购项目融资XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		Integer proid_1=CreateProduct.createSingle(context, order1, series_wj, 2, 800000, 0.08, 1000);
//		CreateProduct.createSingle(context, order1, series_jh, 3, 150000, 0.16, 2000);
//		CreateProduct.createSingle(context, order1, series_jq, 4, 50000, 0.24, 10000);
//		
//		CreateProduct.createSingle(context, order2, series_wj, 2, 160000, 0.1, 1000);
//		CreateProduct.createSingle(context, order2, series_jh, 3, 100000, 0.15, 2000);
//		CreateProduct.createSingle(context, order2, series_jq, 4, 8000, 0.26, 10000);
//		
//		
//		CreateProduct.createSingle(context, order3, series_wj, 2, 500000, 0.12, 1000);
//		CreateProduct.createSingle(context, order3, series_jh, 3, 190000, 0.18, 5000);
//		
//		CreateProduct.createSingle(context, order4, series_jh, 3, 800000, 0.15, 2000);
		
		BorrowerHandle.startOrderFinancing(context, order1);
//		BorrowerHandle.startOrderFinancing(context, 2);
//		
		Lender lender=LenderHandle.createLender(context, "wangm", "1234", "12312341234");
		LenderHandle.recharge(context, lender, 100000);
		LenderHandle.changeLenderLevel(context, lender, 9);
		Integer submitId=LenderHandle.buy(context, lender, proid_1, 50000);
		LenderHandle.pay(context, lender, submitId);
//		
		BorrowerHandle.startProductRepay(context, proid_1);
//		BorrowerHandle.startProductRepay(context, 5);
//		BorrowerHandle.startProductRepay(context, 6);
		BorrowerHandle.startOrderRepay(context, order1);
//		BorrowerHandle.quitProductFinancing(context, 1);
//		BorrowerHandle.quitProductFinancing(context, 2);
//		BorrowerHandle.quitProductFinancing(context, 3);
//		BorrowerHandle.quitOrder(context, 1);
		BorrowerHandle.recharge(context, borrower, 1000000);
		IPayBackService payBackService=context.getBean(IPayBackService.class);
		List<PayBack> payBacks=payBackService.findAll(proid_1);
		for(PayBack payBack:payBacks)
		{
			BorrowerHandle.repay(context, payBack.getId());
		}
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
