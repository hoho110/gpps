package gpps.dao;

import static org.junit.Assert.fail;
import gpps.TestSupport;
import gpps.model.Borrower;
import gpps.model.BorrowerAccount;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class IBorrowerDaoTest extends TestSupport{
	static IBorrowerAccountDao borrowerAccountDao = null;
	static IBorrowerDao borrowerDao = null;
	static BorrowerAccount account=null;
	static Borrower borrower=null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		borrowerAccountDao =context.getBean(IBorrowerAccountDao.class);
		borrowerDao=context.getBean(IBorrowerDao.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		borrowerDao.delete(borrower.getId());
		Assert.assertNull(borrowerDao.find(borrower.getId()));
		borrowerAccountDao.delete(account.getId());
	}
	@Test
	public void testCreate() {
		account=new BorrowerAccount();
		borrowerAccountDao.create(account);
		borrower=new Borrower();
		borrower.setAccountId(account.getId());
		long time=System.currentTimeMillis();
		borrower.setCreatetime(time);
		borrower.setEmail("test@test.com");
		borrower.setIdentityCard("371223654821213654");
		borrower.setLoginId("LoginId");
		borrower.setMaterial("material");
		borrower.setName("gpps");
		borrower.setPassword("Password");
		borrower.setPrivilege(Borrower.PRIVILEGE_VIEW);
		borrower.setRequest("request");
		borrower.setTel("12365487456");
		borrowerDao.create(borrower);
		Assert.assertNotNull(borrower.getId());
		borrower=borrowerDao.find(borrower.getId());
		Assert.assertEquals(account.getId(), borrower.getAccountId());
		Assert.assertEquals(time, borrower.getCreatetime());
		Assert.assertEquals("test@test.com", borrower.getEmail());
		Assert.assertEquals("371223654821213654", borrower.getIdentityCard());
		Assert.assertEquals("LoginId", borrower.getLoginId());
		Assert.assertEquals("material", borrower.getMaterial());
		Assert.assertEquals("gpps", borrower.getName());
		Assert.assertEquals("Password", borrower.getPassword());
		Assert.assertEquals(Borrower.PRIVILEGE_VIEW, borrower.getPrivilege());
		Assert.assertEquals("request", borrower.getRequest());
		Assert.assertEquals("12365487456", borrower.getTel());
	}
	@Test
	public void testCountAll() {
	}

	@Test
	public void testFind() {
	}

	@Test
	public void testFindByLoginId() {
	}

	@Test
	public void testFindByLoginIdAndPassword() {
	}

	@Test
	public void testChangePrivilege() {
	}

	@Test
	public void testChangePassword() {
	}

	@Test
	public void testFindByPrivilege() {
	}

	@Test
	public void testFindByTel() {
	}

}
