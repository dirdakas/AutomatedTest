package Webdriver;



import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;

public class Data {
	public static final char[] balsiai = "AEIOUY".toCharArray();
	public static final char[] priebalsiai = "BCDFGHJKLMNPRSTVZ".toCharArray();

	public static long incLong() {
		return (System.currentTimeMillis() / 500) - 1345560000L;
	}

	public static String incStringOld() {
		long d = incLong();
		String txt = "";
		while (d > 0) {
			txt += (char) (d % 26 + 65);
			d = d / 26;
		}
		return txt;
	}

	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	public static String incString() {
		long d = incLong();
		StringBuilder txt = new StringBuilder();
		int i = 0;
		while (d > 0) {
			int liekana = (int) (d % (priebalsiai.length + balsiai.length));
			txt.append(priebalsiai[liekana % priebalsiai.length]);
			if (i++ % 2 > 0) {
				txt.append(balsiai[liekana % balsiai.length]);
			}
			d = d / (priebalsiai.length + balsiai.length);
		}
		return txt.toString();
	}
	
	public static String generateEmail(int length) {
		String allowedChars="abcdefghijklmnopqrstuvwxyz" +   //alphabets
				"1234567890" +   //numbers
				"_-.";   //special characters
		String email="";
		String temp=RandomStringUtils.random(length,allowedChars);
		email=temp.substring(0,temp.length()-9)+"@test.org";
		return email;
	}
	
	
	public static String generateRandomString(int length){
		return RandomStringUtils.randomAlphabetic(length);
	}
	
	public static String generateRandomNumber(int length){
		return RandomStringUtils.randomNumeric(length);
	}
	
	public static String generateRandomAlphaNumeric(int length){
		return RandomStringUtils.randomAlphanumeric(length);
	}
	
	public static String generateStringWithAllobedSplChars(int length,String allowdSplChrs){
		String allowedChars="abcdefghijklmnopqrstuvwxyz" +   //alphabets
				"1234567890" +   //numbers
				allowdSplChrs;
		return RandomStringUtils.random(length, allowedChars);
	}
	
}