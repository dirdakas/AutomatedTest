package PSI;

import org.junit.Test;

import Webdriver.Data;

public class adminas extends TestCasePSI{

	@Override
	public void after() throws Exception {
		 //super.after();
	}
	
	//kuriant kreipini nera nauju klientu sukurtu
	//neleidzia koreguoti kliento. Pakoregavus baltas langas
	
	//pabaigti
	
	@Test
	public void kreipinysTikPerView() throws Exception{
		login(t, HelpDeskStaff, accA, pssA);
		t.selectDropDownText("Kreipinių sprendimas ", "Kreipiniai");
		kreipinys = Data.incString();
		sukurtiKreipini();
		
		t.click("View");
		//t.find(klientas,2);
		t.sleep(2);
		
		//Update per view
		////*[contains(text(),'Redaguoti')]
		t.click("//div/a[1]",2);
		updateKreipiniView();
		
		//Delete per view
		//t.click("View");
		t.click("//a[2]/span");
		t.sleep(3);
		t.acceptConfirmation();
		t.selectDropDownText("Paskyra ", " Atsijungti");
	}
	
	@Test
	public void klientasTikPerView() throws Exception{
		login(t, HelpDeskStaff, accA, pssA);
		t.selectDropDownText("Registrai ", "Klientai");
		
		klientas = Data.incString();
		sukurtiKlienta();
		
		t.click("View");
		//t.find("Klientas ""+klientas+""");
		t.sleep(3);
		
		//Update per view
		////*[contains(text(),'Redaguoti')]
		//Atkomentinti -=-----------------------------
		t.click("//div/a/span",0);
		updateKlientaView();
		
		//Prideti atstova
		sukurtiAtstova();
		
		//sukurti sutarti
		sukurtiSutartiView();
		
		//sukurti kreipini
		sukurtiKreipiniView();
		
		//Delete per view
		//t.click("View");
		t.click("//div[2]/div/input",1);
		//t.clickButton("Trinti");
		t.sleep(3);
		t.acceptConfirmation();
		t.selectDropDownText("Paskyra ", " Atsijungti");
	}
	
	@Test
	public void klientasTikNePerView() throws Exception{
		login(t, HelpDeskStaff, accA, pssA);
		t.selectDropDownText("Registrai ", "Klientai");
		
		klientas = Data.incString();
		sukurtiKlienta();
		
		//t.click("View");
		//t.find(darbuotojas,2);
		//t.sleep(10);
		
		//----
		//Update
		//----
		t.click("Update");
		updateKlienta();
		
		//----
		//Delete
		//----
		t.click("Delete");
		t.sleep(3);
		t.acceptConfirmation();
		t.selectDropDownText("Paskyra ", " Atsijungti");
	}
	
	@Test
	public void sutartisTikPerView() throws Exception{
		login(t, HelpDeskStaff, accA, pssA);
		t.selectDropDownText("Registrai ", "Sutartys");
		
		kodas = Data.incString();
		sukurtiSutarti();
		
		t.click("View");
		t.find(kodas,2);
		t.sleep(10);
		
		//Update per view
		////*[contains(text(),'Redaguoti')]
		t.click("//div/a[1]",2);
		updateSutartis();
		
		//Delete per view
		t.click("View");
		t.click("//a[2]/span");
		t.sleep(3);
		t.acceptConfirmation();
		t.selectDropDownText("Paskyra ", " Atsijungti");
	}
	
	@Test
	public void sutartisTikNePerView() throws Exception{
		login(t, HelpDeskStaff, accA, pssA);
		t.selectDropDownText("Registrai ", "Sutartys");
		
		kodas = Data.incString();
		sukurtiSutarti();
		
		//t.click("View");
		//t.find(darbuotojas,2);
		//t.sleep(10);
		
		//----
		//Update
		//----
		t.click("Update");
		updateSutartis();
		
		//----
		//Delete
		//----
		t.click("Delete");
		t.sleep(3);
		t.acceptConfirmation();
		t.selectDropDownText("Paskyra ", " Atsijungti");
	}
	
	@Test
	public void darbuotojasTikPerView() throws Exception{
		login(t, HelpDeskStaff, accA, pssA);
		t.selectDropDownText("Registrai ", "Darbuotojai");
		
		darbuotojas = Data.incString();
		sukurtiDarbuotoja();
		
		t.click("View");
		t.find("V"+darbuotojas,2);
		t.sleep(10);
		
		//Update per view
		////*[contains(text(),'Redaguoti')]
		t.click("//div/a[1]",2);
		updateDarbuotojas();
		
		//Delete per view
		t.click("View");
		t.click("//a[2]/span");
		t.sleep(3);
		t.acceptConfirmation();
		t.selectDropDownText("Paskyra ", " Atsijungti");
	}
	
	@Test
	public void darbuotojasTikNePerView() throws Exception{
		login(t, HelpDeskStaff, accA, pssA);
		t.selectDropDownText("Registrai ", "Darbuotojai");
		
		darbuotojas = Data.incString();
		sukurtiDarbuotoja();
		
		//t.click("View");
		//t.find(darbuotojas,2);
		//t.sleep(10);
		
		//----
		//Update
		//----
		t.click("Update");
		updateDarbuotojas();
		
		//----
		//Delete
		//----
		t.click("Delete");
		//t.sleep(3);
		t.acceptConfirmation();
		t.selectDropDownText("Paskyra ", " Atsijungti");
	}
	
	
	@Test
	public void paslaugosTikPerView() throws Exception{
		login(t, HelpDeskStaff, accA, pssA);
		
		t.selectDropDownText("Registrai ", "Paslaugos");
		
		paslauga = Data.incString();
		t.sleep(1);
		kodas = Data.incString();
		sukurtiPaslauga();
		
		//----
		//View
		//----
		t.click("View");
		t.find(paslauga,2);
		t.sleep(10);
		
		//Update per view
		t.click("//div/a[1]",2);
		
		updatePaslauga();
		
		//Delete per view
		t.click("View");
		t.click("//a[2]/span");
		t.sleep(20);
		t.acceptConfirmation();
		t.selectDropDownText("Paskyra ", " Atsijungti");
	}

	@Test
	public void paslaugosTikNePerView() throws Exception{
		login(t, HelpDeskStaff, accA, pssA);
		
		t.selectDropDownText("Registrai ", "Paslaugos");
		
		paslauga = Data.incString();
		t.sleep(1);
		kodas = Data.incString();
		sukurtiPaslauga();
		
		//----
		//Update
		//----
		t.click("Update");
		updatePaslauga();
		
		//----
		//Delete
		//----
		t.click("Delete");
		t.sleep(3);
		t.acceptConfirmation();
		t.selectDropDownText("Paskyra ", " Atsijungti");
	}
	
}