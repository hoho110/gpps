package gpps.service;

import gpps.model.Help;
import gpps.model.Letter;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateHelp {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	public static void main(String args[]){
		IHelpService hservice = context.getBean(IHelpService.class);
		for(int i=0; i<20; i++)
		{
		Help help = new Help();
		help.setCreatetime((new Date()).getTime());
		help.setPublicType(0);
		help.setQuestion("萨芬撒旦法"+i);
		help.setQuestionerType(0);
		help.setType(1);
		help.setQuestionerId(280);
		
		hservice.createPrivate(help);
		}
		System.exit(0);
	}
}
