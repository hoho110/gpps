package gpps.dao;

import gpps.TestSupport;
import gpps.model.Submit;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ISubmitDaoTest extends TestSupport {
	static ISubmitDao submitDao;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		submitDao=context.getBean(ISubmitDao.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testCreate() {
		Submit submit=new Submit();
		submit.setLenderId(1);
		submit.setState(Submit.STATE_APPLY);
		submitDao.create(submit);
		submit=new Submit();
		submit.setLenderId(1);
		submit.setState(Submit.STATE_COMPLETEPAY);
		submitDao.create(submit);
	}

	@Test
	public void testFindAllByLender() {
	}

	@Test
	public void testFindAllByLenderAndStates() {
		List<Integer> states=new ArrayList<Integer>();
		states.add(Submit.STATE_APPLY);
		states.add(Submit.STATE_COMPLETEPAY);
		List<Submit> list=submitDao.findAllByLenderAndStates(1,states);
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void testFindAllByProduct() {
	}

	@Test
	public void testFind() {
	}

	@Test
	public void testChangeState() {
	}

}
