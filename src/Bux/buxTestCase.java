package Bux;

import Webdriver.*;

public class buxTestCase extends WebdriverTestCase{

public static Application bux = new Application(Presentation.WICKET, "http://buxjunction.com/", "", "", false);
	
	String acc = "deadrain";
	String pss = "luk31500";
	
	public static void login(WebTester wt, Application app, String acc, String pss) throws Exception {
		wt.loginBux(app, acc, pss);
	}
}
