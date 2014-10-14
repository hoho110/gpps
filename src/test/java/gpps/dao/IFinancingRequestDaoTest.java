package gpps.dao;

import org.junit.BeforeClass;
import org.junit.Test;

import gpps.TestSupport;
import gpps.model.Borrower;
import gpps.model.BorrowerAccount;
import gpps.model.FinancingRequest;

public class IFinancingRequestDaoTest extends TestSupport{
	static IFinancingRequestDao financingRequestDao;
	static IBorrowerDao borrowerDao;
	static IBorrowerAccountDao borrowerAccountDao;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		financingRequestDao=context.getBean(IFinancingRequestDao.class);
		borrowerDao=context.getBean(IBorrowerDao.class);
		borrowerAccountDao=context.getBean(IBorrowerAccountDao.class);
	}
	@Test
	public void testCreate() {
		Borrower borrower=new Borrower();
		borrower.setName("calis");
		borrower.setPassword("calis");
		borrower.setTel("12312341234");
		BorrowerAccount account=new BorrowerAccount();
		borrowerAccountDao.create(account);
		borrower.setAccountId(account.getId());
		borrowerDao.create(borrower);
		FinancingRequest request=new FinancingRequest();
		request.setBorrowerID(borrower.getId());
		financingRequestDao.create(request);
	}
}
