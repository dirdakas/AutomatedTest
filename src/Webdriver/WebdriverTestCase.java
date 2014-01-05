package Webdriver;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public class WebdriverTestCase {
	protected WebTester t = null;

	public WebdriverTestCase() {
		t = new WebTester();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		System.out.println("BeforeClass");
	}

	@Before
	public void before() throws Exception {
		 t.init();
	}

	@After
	public void after() throws Exception {
		// destroy();
	}

	public void destroy() throws Exception {
		t.downloadServerLog();
		try {
			t.destroy();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public WebTester getTester() {
		return t;
	}

	public void test() throws Exception {
		System.out.println("test");
	}

}