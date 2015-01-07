package gpps.service;

import gpps.model.ref.Contactor.Single;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BorrowerContactor {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	static IBorrowerService borrowerService = context.getBean(IBorrowerService.class);
	public static void main(String args[]) throws Exception{
		
		try{
		borrowerService.addContactor(2001, new Single("王1","13811179462","老板"));
		borrowerService.addContactor(2001, new Single("刘1","13426329462","老板娘"));
		borrowerService.addContactor(2001, new Single("张1","13426329462","闲杂人等"));
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		List<Single> cs = borrowerService.findContactor(2001);
		System.out.println(cs.size());
		System.exit(0);
	}
}
