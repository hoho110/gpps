package gpps.service;

import java.util.Date;

import gpps.model.ProductAction;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.easyservice.support.EasyServiceConstant;

public class AddAction {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	public static void main(String args[]) throws Exception{
		IProductActionService action = context.getBean(IProductActionService.class);
		ProductAction pa = new ProductAction();
		pa.setCreatetime((new Date()).getTime()-15L*24*3600*1000);
		pa.setDetail("项目开始预发布");
		pa.setTitle("预发布");
		pa.setProductId(2);
		pa.setType(1);
		action.create(pa);
		
		ProductAction pa2 = new ProductAction();
		pa2.setCreatetime((new Date()).getTime()-10L*24*3600*1000);
		pa2.setDetail("项目开始正式融资");
		pa2.setTitle("开始融资");
		pa2.setProductId(2);
		pa2.setType(1);
		action.create(pa2);
		
		
		ProductAction pa3 = new ProductAction();
		pa3.setCreatetime((new Date()).getTime()-5L*24*3600*1000);
		pa3.setDetail("项目融资截止，等待管理员审核");
		pa3.setTitle("待审核");
		pa3.setProductId(2);
		pa3.setType(1);
		action.create(pa3);
		
		ProductAction pa4 = new ProductAction();
		pa4.setCreatetime((new Date()).getTime()-4L*24*3600*1000);
		pa4.setDetail("项目审核通过，开始正式计息还款");
		pa4.setTitle("还款中");
		pa4.setProductId(2);
		pa4.setType(1);
		action.create(pa4);
		
		ProductAction pa5 = new ProductAction();
		pa5.setCreatetime((new Date()).getTime()-2L*24*3600*1000);
		pa5.setDetail("项目执行第一次还款");
		pa5.setTitle("第一次还款");
		pa5.setProductId(2);
		pa5.setType(1);
		action.create(pa5);
	}
}
