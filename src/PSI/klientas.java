package PSI;

import org.junit.Test;

public class klientas extends TestCasePSI{

	@Override
	public void after() throws Exception {
		 //super.after();
	}
	
	@Test
	public void prisijunkti() throws Exception{
		login(t, HelpDeskClient, accK, pssK);
	}
}