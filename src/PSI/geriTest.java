package PSI;

import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

import Webdriver.WebTester;

public class geriTest extends WebTester{
	String index = "http://localhost/index.php";
	String acc = "lukas@gmail.com";
	String pss = "test";
	
	@Test
	public void prisijunkti() throws Exception{
		login(index, acc, pss);
		//findElementByXpath("//*/div[2]/a");
		//find("//*/div[2]/a", "xpath").click();;
		findAll("Kreipiniai", "Pavadinimas");
		//click("Kodas");
		//selectDropDownText("Kreipinių sprendimas ", "Inžinieriai");
		selectDropDownText("Registrai ", "Klientai");
		click("Pridėti naują klientą");
		setInputById("Klientas_k_kodas", "kodas123");
		//setContext("//div[3]//div/div[2]");
		//setInput("Kliento kodas", "kodas123",2);
		//setInput("Pavadinimas", "pavadinimas123");
		//setInput("Adresas", "Adresas123");
	}
}
