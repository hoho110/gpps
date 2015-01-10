package gpps.service;

import gpps.model.Admin;
import gpps.model.Letter;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateAdmin {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	public static void main(String args[]) throws Exception{
		IAdminService adminservice = context.getBean(IAdminService.class);
		Admin admin = new Admin();
		admin.setCreatetime((new Date()).getTime());
		admin.setEmail("admin@calis.edu.cn");
		admin.setLoginId("admin");
		admin.setName("管理员一");
		admin.setPassword("111111");
		admin.setPrivilege(Admin.PRIVILEGE_ALL);
		admin.setTel("13477756745");
		try{
		adminservice.register(admin);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.exit(0);
	}
}
