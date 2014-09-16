package gpps.service;

import gpps.dao.IBorrowerAccountDao;
import gpps.dao.IBorrowerDao;
import gpps.model.Borrower;
import gpps.model.BorrowerAccount;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CreateBorrower {
	public static Integer create(ApplicationContext context){
		IBorrowerService borrowerService = context.getBean(IBorrowerService.class);
		IBorrowerDao borrowerDao = context.getBean(IBorrowerDao.class);
		IBorrowerAccountDao accountDao = context.getBean(IBorrowerAccountDao.class);
		
		BorrowerAccount account = new BorrowerAccount();
		accountDao.create(account);
		
		Borrower borrower=new Borrower();
		borrower.setEmail("test@calis.edu.cn");
		borrower.setIdentityCard("231550215402021533");
		borrower.setLoginId("test");
		borrower.setName("测试");
		borrower.setPassword("123123");
		borrower.setTel("1333333");
		borrower.setCreditValue(10000);
		borrower.setPrivilege(12);
		borrower.setAccountId(account.getId());
		borrowerDao.create(borrower);
		return borrower.getId();
	}
}
