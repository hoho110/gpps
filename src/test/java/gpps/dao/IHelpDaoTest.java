package gpps.dao;

import gpps.TestSupport;
import gpps.model.Help;
import gpps.service.IHelpService;

import org.junit.BeforeClass;
import org.junit.Test;

public class IHelpDaoTest extends TestSupport{
	private static IHelpService helpService;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		helpService=context.getBean(IHelpService.class);
	}
	@Test
	public void testCreate()
	{
		Help help=new Help();
		help.setType(Help.TYPE_PUBLIC);
		help.setPublicType(Help.PUBLICTYPE_BEGINNER);
		help.setQuestion("问题");
		help.setAnswer("回答");
		helpService.createPublic(help);
	}
}
