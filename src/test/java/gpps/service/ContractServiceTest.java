package gpps.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ContractServiceTest {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	protected static IContractService contractService = context.getBean(IContractService.class);
	public static void main(String args[]) throws Exception{
//		List<ContractItem> items = contractService.list(2007);
//		for(ContractItem item:items){
//			System.out.println(item.getLenderName()+":"+item.getOrderName()+":"+item.getSeriesName()+":"+item.getAmount().intValue()+":"+item.getSubmitID());
//		}
		boolean flag = contractService.isComplete(2007);
		System.exit(0);
	}
}
