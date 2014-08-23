package gpps.dao;


import java.math.BigDecimal;

import gpps.dao.IBorrowerAccountDao;
import gpps.dao.ILenderDao;
import gpps.model.BorrowerAccount;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BorrowerDaoTest {
	static ApplicationContext context = null;
	static IBorrowerAccountDao borrowerAccountDao = null;
	static SqlSessionTemplate sqlSession;
	@BeforeClass
	public static void initContext() {
		context = new FileSystemXmlApplicationContext(
				"/src/main/webapp/WEB-INF/spring/root-context.xml");
		borrowerAccountDao = (IBorrowerAccountDao) context.getBean(IBorrowerAccountDao.class);
		sqlSession=(SqlSessionTemplate) context.getBean("sqlSession");
	}

	@Test
	public void compute() {
		BorrowerAccount account=new BorrowerAccount();
		borrowerAccountDao.create(account);
		Assert.assertNotNull(account.getId());
		Integer id=account.getId();
		borrowerAccountDao.recharge(id, new BigDecimal(10000));
		account=borrowerAccountDao.find(id);
		Assert.assertEquals(10000, account.getTotal().doubleValue(), 0);
		Assert.assertEquals(10000, account.getUsable().doubleValue(), 0);
	}
}
