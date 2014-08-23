package gpps.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import gpps.TestSupport;
import gpps.model.CashStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ICashStreamDaoTest  extends TestSupport{
	static ICashStreamDao cashStreamDao;
	List<CashStream> list=new ArrayList<CashStream>();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		cashStreamDao=context.getBean(ICashStreamDao.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testCreate() {
		CashStream cashStream=new CashStream();
		cashStream.setSubmitId(1);
		cashStreamDao.create(cashStream);
		cashStream=new CashStream();
		cashStream.setSubmitId(1);
		cashStreamDao.create(cashStream);
	}

	@Test
	public void testChangeCashStreamState() {
	}

	@Test
	public void testFindSubmitCashStream() {
		Assert.assertEquals(2, (cashStreamDao.findSubmitCashStream(1)).size());
	}

}
