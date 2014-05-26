package IETests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class testas extends test{
	private WebDriver _driver;
	
	@Test
	public void IEconfiguration() throws Exception {
	    System.setProperty("webdriver.ie.driver",
	    "C:/Users/lietutis/workspace/AutomatedTest.git/trunk/src/WebExplorer/IEDriverServer.exe");
	    _driver = new InternetExplorerDriver();
	    login();
	}
	
	public void login() throws Exception {
	    _driver.get("http://www.facebook.com");
	    _driver.findElement(By.id("pass")).sendKeys("lukas.dirda@gmail.com");
	}
	
	
}