package Bux;

import org.junit.Test;

import Webdriver.Data;


public class bux extends buxTestCase{

	
	@Override
	public void after() throws Exception {
		 //super.after();
	}
	
	@Test
	public void buxai() throws Exception{
		login(t, bux,acc,pss);
		t.click("SURF");
		t.sleep(2);
		//t.click("//*[contains(@color, '6c685c')]",4);
		
		int i;
		for (i= 0; i <= t.findCount("//*[contains(@color, '6c685c')]"); i++){
			t.clickLabelOpenPopup("//*[contains(@color, '6c685c')]",i);
			t.sleep(30);
			t.closePopUp();
		}
		
	}
}