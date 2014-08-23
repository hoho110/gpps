/**
 * 
 */
package gpps.dao;

import gpps.TestSupport;
import gpps.model.BorrowerAccount;

import java.math.BigDecimal;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author wangm
 *
 */
public class IBorrowerAccountDaoTest extends TestSupport{
	static IBorrowerAccountDao borrowerAccountDao = null;
	static BorrowerAccount account=null;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		borrowerAccountDao = (IBorrowerAccountDao) context.getBean(IBorrowerAccountDao.class);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		borrowerAccountDao.delete(account.getId());
		Assert.assertNull(borrowerAccountDao.find(account.getId()));
	}

	/**
	 * Test method for {@link gpps.dao.IBorrowerAccountDao#create(gpps.model.BorrowerAccount)}.
	 */
	@Test
	public void testCreate() {
		account=new BorrowerAccount();
		borrowerAccountDao.create(account);
		Assert.assertNotNull(account.getId());
	}

	/**
	 * Test method for {@link gpps.dao.IBorrowerAccountDao#find(java.lang.Integer)}.
	 */
	@Test
	public void testFind() {
		BorrowerAccount borrowerAccount=borrowerAccountDao.find(account.getId());
		Assert.assertEquals(borrowerAccount.getId(), account.getId());
	}

	/**
	 * Test method for {@link gpps.dao.IBorrowerAccountDao#recharge(java.lang.Integer, java.math.BigDecimal)}.
	 */
	@Test
	public void testRecharge() {
		borrowerAccountDao.recharge(account.getId(), new BigDecimal(10000));
		account=borrowerAccountDao.find(account.getId());
		Assert.assertEquals(10000, account.getTotal().doubleValue(), 0);
		Assert.assertEquals(10000, account.getUsable().doubleValue(), 0);
		Assert.assertEquals(0, account.getFreeze().doubleValue(), 0);
	}

	/**
	 * Test method for {@link gpps.dao.IBorrowerAccountDao#freeze(java.lang.Integer, java.math.BigDecimal)}.
	 */
	@Test
	public void testFreeze() {
		borrowerAccountDao.freeze(account.getId(), new BigDecimal(5000));
		account=borrowerAccountDao.find(account.getId());
		Assert.assertEquals(10000, account.getTotal().doubleValue(), 0);
		Assert.assertEquals(5000, account.getUsable().doubleValue(), 0);
		Assert.assertEquals(5000, account.getFreeze().doubleValue(), 0);
	}

	/**
	 * Test method for {@link gpps.dao.IBorrowerAccountDao#pay(java.lang.Integer, java.math.BigDecimal)}.
	 */
	@Test
	public void testPay() {
		borrowerAccountDao.pay(account.getId(), new BigDecimal(2000));
		account=borrowerAccountDao.find(account.getId());
		Assert.assertEquals(12000, account.getTotal().doubleValue(), 0);
		Assert.assertEquals(7000, account.getUsable().doubleValue(), 0);
		Assert.assertEquals(5000, account.getFreeze().doubleValue(), 0);
	}

	/**
	 * Test method for {@link gpps.dao.IBorrowerAccountDao#repay(java.lang.Integer, java.math.BigDecimal)}.
	 */
	@Test
	public void testRepay() {
		borrowerAccountDao.repay(account.getId(), new BigDecimal(3000));
		account=borrowerAccountDao.find(account.getId());
		Assert.assertEquals(9000, account.getTotal().doubleValue(), 0);
		Assert.assertEquals(7000, account.getUsable().doubleValue(), 0);
		Assert.assertEquals(2000, account.getFreeze().doubleValue(), 0);
	}

	/**
	 * Test method for {@link gpps.dao.IBorrowerAccountDao#cash(java.lang.Integer, java.math.BigDecimal)}.
	 */
	@Test
	public void testCash() {
		borrowerAccountDao.cash(account.getId(), new BigDecimal(7000));
		account=borrowerAccountDao.find(account.getId());
		Assert.assertEquals(2000, account.getTotal().doubleValue(), 0);
		Assert.assertEquals(0, account.getUsable().doubleValue(), 0);
		Assert.assertEquals(2000, account.getFreeze().doubleValue(), 0);
	}

}
