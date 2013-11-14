package Webdriver;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebTester {
	
	protected List<String> log = new ArrayList<String>();
	protected FirefoxDriver driver = new FirefoxDriver();
	private boolean highlightReference = Boolean.parseBoolean(System.getProperty("highlightReference", "true"));
	private boolean highlightTarget = Boolean.parseBoolean(System.getProperty("highlightTarget", "true"));
	private String context = "//html";
	
	
	public void login(String url, String usr, String pass) throws Exception{
		//FirefoxDriver driver = new FirefoxDriver();
		driver.get(url);
		highlight(driver.findElement(By.id("LoginForm_username"))).sendKeys(usr);
		highlight(driver.findElement(By.id("LoginForm_password"))).sendKeys(pass);
		highlight(driver.findElement(By.xpath("//*/button"))).click();
		sleep(1);
	}
	
	/*public void click(String text, String type) throws Exception {
		log("[" + text + "]");
		WebElement element = null;
		switch (type) {
		
		case  "name" :   element = driver.findElement(By.name(text));
						element.submit();
							break;
							
		case  "id" :   element = driver.findElementById(text);
						element.submit();
						break;
							
		case  "xpath" :   element = driver.findElementByXPath(text);
					element.submit();
							break;
							
		case  "className" :   element = driver.findElementByClassName(text);
						element.submit();
							break;
							
			default : break;
		}
	}*/

	public void log(String message) throws Exception {
		log.add(message);
		System.out.println(message);
	}
	
	
	
	public boolean isXpath(String labelOrPath) {
		return StringUtils.containsAny(labelOrPath, "[=@") || (labelOrPath != null && (labelOrPath.startsWith("//")) || labelOrPath.startsWith("./"));
	}
	
		
	public void implicitMilis(int sec) {
		driver.manage().timeouts().implicitlyWait(sec, TimeUnit.MILLISECONDS);
	}
	
	public WebElement highlight(WebElement we) throws Exception {
		return highlightTarget ? highlight(we, "2px solid red") : we;
	}

	public WebElement highlight(WebElement we, String border) throws Exception {
		waitForProcessingFinished();
		Assert.assertNotNull(we);
		runJavaScript(we, "style.border='" + border + "'");
		return we;
	}
	
	
	public void waitForProcessingFinished() {
			((JavascriptExecutor) driver).executeScript("if (typeof wicketAjaxBusy  == 'undefined') wicketAjaxBusy = function () {" + "for (var channelName in Wicket.channelManager.channels) {"
					+ "if (Wicket.channelManager.channels[channelName].busy) { return true; }" + "return false;}};");
			ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					try {
						return (Boolean) ((JavascriptExecutor) d).executeScript("return !wicketAjaxBusy()");
					} catch (WebDriverException wde) {
						if (wde.getMessage().contains("is not defined")) {
							return true;
						} else {
							throw wde;
						}
					}
				}
			};
			(new WebDriverWait(driver, getTimeoutSec(), 400)).withMessage("tk.activeAjaxCount should be 0").until(e);
			return;
		}
	
	public int getTimeoutSec() {
		return 60;
	}
		
	
	public WebElement runJavaScript(WebElement we, String js) throws Exception {
		if (driver instanceof JavascriptExecutor) {
			try {
				((JavascriptExecutor) driver).executeScript("arguments[0]." + js, we);
			} catch (WebDriverException wde) {
				if (wde.getMessage().contains("arguments[0] is undefined")) {
					log("Warning: arguments[0] is undefined");
				} else
					throw wde;
			}
		}
		return we;
	}
	
	public boolean isHighlightTarget() {
		return highlightTarget;
	}
	
	public void implicitSec(int sec) {
		driver.manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
	}
	

	public WebElement findElementByXpath(String xpath) throws Exception {
		return highlight(driver.findElementByXPath(xpath));
		
	}
	
	public List<WebElement> findVisibleElements(By by) throws Exception {
		waitForProcessingFinished();
		List<WebElement> visibleElements = new ArrayList<WebElement>();
		List<WebElement> foundElements = null;
			foundElements = driver.findElements(by);
			for (WebElement e : foundElements) {
				if (e.isDisplayed()) {
					visibleElements.add(e);
				}
			}
			return visibleElements;
		}
		
	
	public WebElement highlightDashed(WebElement we) throws Exception {
		return highlightReference ? highlight(we, "2px dashed red") : we;
	}
	
	public String buildLabelXpath(String element, String labelorPath) {
			return "//" + element + "[text()='" + labelorPath + "' or @title='" + labelorPath + "']";
		}
	
	
	public void doubleClick(String text, String by) throws Exception {
		log("[" + text + "]");
		highlight(find(text, by));
		Actions action = new Actions(driver);
		action.doubleClick(find(text, by));
		action.perform();

	}
	
	/*public WebElement find(String text, String by) throws Exception {
		
		switch (by) {
        
		case "id":  return highlight(driver.findElementById(text));
                 	
        case "xpath": return highlight(driver.findElementByXPath(text));
     				 
        default: return driver.findElementById(text);
	}
	}*/
	
	public void isElementPresent(String xpath) throws Exception{
		if(driver.findElement(By.xpath(xpath))!= null){
			highlight(driver.findElement(By.xpath(xpath)));
			System.out.println("Element is Present");
			log.add("Element is Present");
			}else{
			System.out.println("Element is Absent");
			log.add("Element is dead");
			}
	}
	
	public WebElement findVisibleElement(By by) throws Exception {
		waitForProcessingFinished();
			List<WebElement> visibleElements = findVisibleElements(by);
			if (!visibleElements.isEmpty()) {
				return visibleElements.get(0);
			} else {
				throw new NoSuchElementException("Element is not displayed " + by.toString());
			}
		}
	
	
	public WebElement find(String labelorPath) throws Exception {
		return find(labelorPath, 0);
	}
	
		public WebElement find(String labelorPath, int index) throws Exception {
		return find(labelorPath, "*", index);
	}
	
		public WebElement find(String labelorPath, String element, int index) throws Exception {
		// waitForProcessingFinished();
		String builtXpath = isXpath(labelorPath) ? labelorPath : buildLabelXpath(element, labelorPath);
		builtXpath = builtXpath.replaceFirst("//", "./descendant::");
		
		By by = By.xpath(builtXpath);
		if (index == 0) {
			try {
				return highlight(findVisibleElement(by));
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException("window " + driver.getTitle() + " " + e.getMessage());
			}
		} else {
			try {
				return highlight(findVisibleElements(by).get(index));
			} catch (IndexOutOfBoundsException e) {
				throw new NoSuchElementException("window " + driver.getTitle() + " " + by.toString());
			}
		}
	}
	
		public WebElement find(String labelorPath, String element) throws Exception {
			return find(labelorPath, element, 0);
		}	
		
		
		public void findAll(String... labels) throws Exception {
			for (String label : labels) {
				highlight(find(label));
			}
		}
		
		
		public void click(String text, String element, int index) throws Exception {
			log("[" + text + ":" + element + ":" + index + "]");
			highlight(find(text, element, index)).click();
		}

		public void click(String text, String element) throws Exception {
			log("[" + text + ":" + element + "]");
			highlight(find(text, element)).click();
		}

		public void click(String text, int index) throws Exception {
			log("[" + text + "]");
			highlight(find(text, index)).click();
		}

		public void click(String text) throws Exception {
			click(text, 0);
			find(text);
		}
		
		
		
		public void setInput(String label, String value, int index) throws Exception {
			synchronized (WebTester.class) {
				log(label + "=" + value);
				WebElement input = findNear(label, 0, "input[@type='text']", index);
				Assert.assertTrue(label + " input should be enabled.", input.isEnabled());
				String existing = input.getAttribute("value");
				if (!value.equals(existing)) {
					if (!"".equals(existing)) {
						input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
					}
					input.sendKeys(value, Keys.TAB);
				}
			}
		}

		public void setInput(String label, String value) throws Exception {
			setInput(label, value, 0);
		}
		
		public void setInputById(String id, String text) throws Exception {
			highlight(driver.findElement(By.id(id))).sendKeys(text);
		}
		
		public void setSelectText(String referenceLabelorPath, String visibleText, int index) throws Exception {
			log(referenceLabelorPath + "=<" + visibleText + ">");

			WebElement e = findNear(referenceLabelorPath, "select");
			(new Select(e)).selectByVisibleText(visibleText);
			findNear(referenceLabelorPath, "select").sendKeys(Keys.TAB);

		}

		public void setSelectText(String referenceLabelorPath, String visibleText) throws Exception {
			setSelectText(referenceLabelorPath, visibleText, 0);
		}
		
		
		
		public WebElement findAfter(String labelOrPath, String element) throws Exception {
			return highlight(highlightDashed(find(labelOrPath)).findElement(By.xpath("./ancestor-or-self::*/following::" + element)));
		}

		public WebElement findNear(String referenceLabelorPath, String targetElement) throws Exception {
			return findNear(referenceLabelorPath, 0, targetElement, 0);
		}
		
		
		public WebElement findNear(String referenceLabelorPath, int referenceIndex, String targetElement, int targetIndex) throws Exception {
			int level = 0;
			try {
				WebElement reference = highlightDashed(find(referenceLabelorPath, referenceIndex));
				implicitMilis(300);
				WebElement descendant = null;
				do {
					try {
						descendant = reference.findElements(By.xpath("./descendant::" + targetElement)).get(targetIndex);
					} catch (IndexOutOfBoundsException e) {
					}
					reference = reference.findElement(By.xpath("./parent::*"));
					if (level++ > 500 || "html".equalsIgnoreCase(reference.getTagName()))
						throw new NoSuchElementException("Too long path for parent.");
				} while (descendant == null);
				return highlight(descendant);
			} finally {
				implicitSec(getTimeoutSec());
			}
		}
		
		public void selectDropDownText(String mainMenu, String subMenu) throws Exception{
			Actions builder = new Actions(driver);
			builder.moveToElement(find(mainMenu)).build().perform();
			builder.moveToElement(find(subMenu)).build().perform();
			find(subMenu).click();
		}
		
		
		
		public void sleep(long sec) throws Exception {
			sleep(sec, TimeUnit.SECONDS);
		}
		
		public void sleep(long amount, TimeUnit tu) throws Exception {
			long millis = tu.toMillis(amount);
			if (millis > 999) {
				System.out.println("sleep " + amount + " " + tu.toString());
			}
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
			public void setContext(String xpath) throws Exception {
			this.context = xpath;
		}
		
			public void setDefaultContext() throws Exception {
			setContext("//html");
		}
}