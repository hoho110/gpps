package gpps.service;

import java.util.List;

import junit.framework.Assert;
import gpps.TestSupport;
import gpps.dao.IActivityDao;
import gpps.model.Activity;

import org.junit.BeforeClass;
import org.junit.Test;

public class IActivityDaoTest extends TestSupport{
	private static IActivityDao activityDao;
	@BeforeClass
	public static void init(){
		activityDao=context.getBean(IActivityDao.class);
	}
	@Test
	public void test()
	{
		List<Activity> activities=activityDao.findByState(-1, 0, 10);
		Assert.assertEquals(false, activities==null);
	}
}
