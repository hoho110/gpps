package gpps.dao;


import gpps.dao.ILenderDao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class UserServiceTest {
	ApplicationContext context = null;
	ILenderDao lenderDao = null;

	@Before
	public void initContext() {
		this.context = new FileSystemXmlApplicationContext(
				"/src/main/webapp/WEB-INF/spring/root-context.xml");
		this.lenderDao = (ILenderDao) context.getBean("lenderDao");
	}

	@Test
	public void countAll() {
		System.out.println("数据库中的记录条数:" + lenderDao.countAll());
	}
}
