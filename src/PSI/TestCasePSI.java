package PSI;

import Webdriver.*;

public class TestCasePSI extends WebdriverTestCase{
	
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
	public String accA = "adminas@test.com";
	public String pssA = "test";
	
	public String klientuWEB = "http://hd.eugiw.com/site";
	public String accK = "666";
	public String pssK = "test";
	
	public static String paslauga =null;
	public static String kodas =null;
	public static String darbuotojas =null;
	public static String klientas = null;
	public static String kreipinys = null;
	public static String pastas = null;
	public static String numeris = null;
	public static String aprasymas = null;
	public static String atstovas = null;
	
	
	public static final String DEFAULT_SERVER = System.getProperty("serverAddress", "http://hd.eugiw.com");
	
	public static Application HelpDeskStaff = new Application(Presentation.WICKET, "http://hd.eugiw.com", "site", "/staff", false);
	public static Application HelpDeskClient = new Application(Presentation.WICKET, "http://hd.eugiw.com", "site", "", false);
	
	public static void login(WebTester wt, Application app, String acc, String pss) throws Exception {
		wt.login(app, acc, pss);
	}
	
	
	public void updateKreipiniView() throws Exception {
		kreipinys = Data.incString();
		pildytiKreipini();
		
		t.clickButton("Išsaugoti kreipinį");
		t.sleep(2);
	}


	private void pildytiKreipini() throws Exception {
		t.click("//*[contains(@data-id, 'Kreipinys_kr_k_id')]");
		t.click("(//*[text()='Vasara, UAB'])[last()]");
		
		t.click("//*[contains(@data-id, 'Kreipinys_kr_p_id')]");
		t.click("(//*[text()='Virtualus serveris'])[last()]");
		
		t.click("//*[contains(@data-id, 'Kreipinys_kr_tipas')]");
		t.click("(//*[text()='Incidentas'])[last()]");
		
		t.setInputById("Kreipinys_kr_pavadinimas", kreipinys);
		aprasymas = Data.generateRandomString(50);
		t.setInputById("Kreipinys_kr_aprasas", aprasymas);
	}
	
	
	public void updateKlienta() throws Exception {
		klientas = Data.incString();
		aprasymas = Data.generateRandomString(25);
		kodas = Data.incString();
		pildykKlienta();
		t.clickButton("Atnaujinti");
		
		t.sleep(2);
		t.setInputById("paieska", klientas);
		t.sleep(2);
		t.findAll(klientas);
		t.sleep(5);
	}
	
	public void updateKlientaView() throws Exception {
		klientas = Data.incString();
		aprasymas = Data.generateRandomString(25);
		kodas = Data.incString();
		pildykKlienta();
		t.clickButton("Atnaujinti");
		
		t.sleep(2);
		
	}
	
	public void updatePaslauga() throws Exception {
		paslauga = Data.incString();
		t.sleep(1);
		kodas = Data.incString();
		
		pildykPaslauga();
		t.clickButton("Atnaujinti paslaugą");
		t.sleep(2);
		t.setInputById("paieska", paslauga);
		t.sleep(2);
		t.findAll(paslauga, "20:15", "15:15");
		t.sleep(5);
	}
	
	public void updateSutartis() throws Exception {
		kodas = Data.incString();
		
		pildykSutarti();
		t.clickButton("Atnaujinti");
		t.sleep(2);
		t.setInputById("paieska", kodas);
		t.sleep(2);
		t.findAll(kodas);
		t.sleep(5);
	}
	
	public void updateDarbuotojas() throws Exception {
		darbuotojas = Data.incString();
		
		pildykDarbuotoja();
		t.clickButton("Atnaujinti");
		t.sleep(2);
		t.setInputById("paieska", darbuotojas);
		t.sleep(2);
		t.findAll("V"+darbuotojas,"P"+darbuotojas ,numeris);
		t.sleep(5);
	}
	
	public void sukurtiAtstova() throws Exception{
		t.click("Pridėti naują atstovą");
		atstovas = Data.incString();
		t.setInputById("Atstovas_a_vardas", "V"+atstovas);
		t.setInputById("Atstovas_a_pavarde", "P"+atstovas);
		numeris = "+370"+Data.generateRandomNumber(8);
		t.setInputById("TelNrList", numeris);
		pastas = Data.generateEmail(10);
		t.setInputById("EmailList",pastas);
		//t.selectItemByDataIdAndText("Atstovas_a_busena","Aktyvus",1);
		
		t.click("//*[contains(@data-id, 'Atstovas_a_busena')]");
		t.click("(//*[text()='Aktyvus'])[last()]");
		
		t.sleep(2);
		t.clickButton("Pridėti atstovą");
		t.sleep(2);
		//t.findAll("V"+atstovas, "P"+atstovas, "Aktyvus");
	}
	
	public void sukurtiKreipini() throws Exception{
		t.click("Pridėti naują kreipinį");
		pildytiKreipini();
		
		t.clickButton("Sukurti kreipinį");
		t.sleep(2);
		
		t.find("Kreipiniai");
		t.setInputById("paieska", kreipinys);
		t.sleep(2);
		t.findAll(kreipinys, aprasymas);
		t.sleep(5);
	}
	
	public void sukurtiKreipiniView() throws Exception{
		t.click("Pridėti naują kreipinį");
		pildytiKreipini();
		
		t.clickButton("Sukurti kreipinį");
		t.sleep(2);
	}
	
	public void sukurtiSutarti() throws Exception {
		t.click("Pridėti naują sutartį");
		
		pildykSutarti();
		t.clickButton("Pridėti sutartį");
		t.sleep(2);
		
		t.find("Sutartys");
		t.setInputById("paieska", kodas);
		t.sleep(2);
		t.findAll(kodas, "Test");
		t.sleep(5);
	}
	
	public void sukurtiSutartiView() throws Exception {
		t.click("Pridėti naują sutartį");
		
		pildykSutarti();
		t.clickButton("Pridėti sutartį");
		t.sleep(2);
	}
	
	public void sukurtiPaslauga() throws Exception {
		t.click("Pridėti naują paslaugą");
		
		pildykPaslauga();
		t.clickButton("Pridėti paslaugą");
		t.sleep(2);
		
		t.find("Paslaugos");
		t.setInputById("paieska", paslauga);
		t.sleep(2);
		t.findAll(paslauga, "20:15", "15:15");
		t.sleep(5);
	}
	
	public void sukurtiDarbuotoja() throws Exception {
		t.click("Pridėti naują darbuotoją");
		pildykDarbuotoja();
		
		t.clickButton("Pridėti darbuotoją");
		//t.sleep(2);
		
		t.find("Darbuotojai");
		t.setInputById("paieska", darbuotojas);
		t.sleep(2);
		t.findAll("V"+darbuotojas,"P"+darbuotojas, numeris);
		t.sleep(5);
	}
	
	public void sukurtiKlienta() throws Exception {
		t.click("Pridėti naują klientą");
		klientas = Data.incString();
		aprasymas = Data.generateRandomString(25);
		kodas = Data.incString();
		pildykKlienta();
		numeris = "+370"+ Data.generateRandomNumber(8);
		pastas = Data.generateEmail(10);
		
		t.setInputById("TelNrList", numeris);
		t.setInputById("EmailList", pastas);
		
		t.clickButton("Pridėti klientą");
		//t.sleep(2);
		
		t.find("Klientai");
		t.setInputById("paieska", klientas);
		t.sleep(2);
		t.findAll(klientas);
		t.sleep(2);
	}

	public void pildykSutarti() throws Exception {
		t.setInputById("Sutartis_s_kodas", kodas);
		t.click("//*[contains(@data-id, 'Sutartis_s_k_id')]");
		//t.click("(//*[text()='Test'])[last()]");
		t.click("(//*[text()='Vasara, UAB'])[last()]");
		
		
		t.click("//*[contains(@data-id, 'PaslaugosList')]");
		//t.click("(//*[text()='Internetas'])[last()]");
		t.click("(//*[text()='Virtualus serveris'])[last()]");
		
		t.click("//*[contains(@data-id, 'Sutartis_s_iki')]");
		t.click("(//*[text()='3 mėn.'])[last()]");
		
		aprasymas = Data.generateRandomString(50);
		t.setInputById("Sutartis_s_pastabos", aprasymas);
		t.sleep(2);
	}
	
	public void pildykPaslauga() throws Exception {
		t.setInputById("Paslauga_p_pavadinimas", paslauga);
		aprasymas = Data.generateRandomString(50);
		t.setInputById("Paslauga_p_aprasas", aprasymas);
		t.setInputById("Paslauga_p_inc_terminas", "20:15");
		t.setInputById("Paslauga_p_req_terminas", "15:15");
		t.setInputById("Paslauga_p_kodas", kodas);
		t.sleep(2);
	}
	
	public void pildykDarbuotoja() throws Exception {
		t.setInputById("Darbuotojas_d_vardas", "V"+darbuotojas);
		t.setInputById("Darbuotojas_d_pavarde", "P"+darbuotojas);
		pastas = Data.generateEmail(12);
		t.setInputById("Darbuotojas_d_el_pastas", pastas);
		t.click("//*[contains(@data-id, 'Darbuotojas_d_pareigos_id')]");
		t.click("(//*[text()='Inžinierius'])[last()]");
		numeris = "+370"+Data.generateRandomNumber(8);
		t.setInputById("Darbuotojas_d_tel_nr", numeris);
		t.sleep(2);
	}
	
	public void pildykKlienta() throws Exception {
		t.setInputById("Klientas_k_kodas", kodas);
		t.setInputById("Klientas_k_pavadinimas", klientas);
		t.setInputById("Klientas_k_adresas", aprasymas);

	}
}
