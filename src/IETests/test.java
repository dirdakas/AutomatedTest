package IETests;

import WebExplorer.*;

public class test extends WebdriverTestCase{

	
	public String localIndex = "http://localhost/index.php";
	public String localAcc = "lukas@gmail.com";
	public String localPss = "test";
	
	public String index = "http://hd.eugiw.com/site";
	public String acc = "lukas.dirda@gmail.com";
	public String pss = "test";
	
	public String darbuotojuWEB = "http://hd.eugiw.com/site/staff";
	public String accV = "vadovas@test.com";
	public String pssV = "test";
	public String accI = "Inzinierius@test.com";
	public String pssI = "test";
	public String accA = "lukas.dirda@gmail.com";
	public String pssA = "DirD0101";
	
	public String klientuWEB = "http://hd.eugiw.com/site";
	public String accK = "666";
	public String pssK = "test";
	
	
	
	public static final String DEFAULT_SERVER = System.getProperty("serverAddress", "http://www.google.com");
	
	public static Application FaceBook = new Application(Presentation.WICKET, "http://www.facebook.com", "", "", false);
	
	
	public static void login(WebTester wt, Application app, String acc, String pss) throws Exception {
		wt.loginFB(app, acc, pss);
	}
	
	
	
	
}
