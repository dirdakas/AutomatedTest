package PSI;

import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

import Webdriver.WebTester;

public class geriTest extends WebTester{
	String adresas = "localhost";
	
	@Test
	public void prisijunkti() throws Exception{
		login(adresas, "acc", "pss");
	}
}
