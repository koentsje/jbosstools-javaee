package org.jboss.tools.jsf.jsp.ca.test;

import org.jboss.tools.jst.jsp.test.ca.ContentAssistantTestCase;
import org.jboss.tools.test.util.TestProjectProvider;

public class CAForJSF2BeansTest extends ContentAssistantTestCase{
	TestProjectProvider provider = null;
	boolean makeCopy = true;
	private static final String PROJECT_NAME = "JSF2Beans";
	private static final String PAGE_NAME = "/src/test/beans/inputname.xhtml";
	
	public void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.tools.jsf.test", null, PROJECT_NAME, makeCopy); 
		project = provider.getProject();
	}

	protected void tearDown() throws Exception {
		if(provider != null) {
			provider.dispose();
		}
	}
	
	/**
	 * JBIDE-5941
	 */
	public void testCAForJSF2Beans(){
		String[] proposals = {
			"mybean1", "mybean2"
		};

		checkProposals(PAGE_NAME, "#{myb}", 5, proposals, false);

	}
}
