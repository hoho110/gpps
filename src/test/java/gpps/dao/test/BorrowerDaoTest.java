package gpps.dao.test;


import java.math.BigDecimal;

import gpps.dao.IBorrowerAccountDao;
import gpps.dao.ILenderDao;
import gpps.model.BorrowerAccount;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BorrowerDaoTest {
	ApplicationContext context = null;
	IBorrowerAccountDao borrowerAccountDao = null;

	@Before
	public void initContext() {
		this.context = new FileSystemXmlApplicationContext(
				"/src/main/webapp/WEB-INF/spring/root-context.xml");
		this.borrowerAccountDao = (IBorrowerAccountDao) context.getBean(IBorrowerAccountDao.class);
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
