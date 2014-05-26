package WebExplorer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebTester {
	protected WebDriver mainDriver = null;
	protected WebDriver driver = null;

	protected String mainHandle = null;
	protected List<String> log = new ArrayList<String>();

	public static String LOG_SESSION_ID = "logSessionId";
	protected String logId = "logId";
	protected String server = System.getProperty("serverAddress", "NONE");
	private Application application = null;

	private Stack<String> handleStack = new Stack<String>();

	private List<byte[]> serverLogs = new ArrayList<byte[]>();

	private boolean highlightReference = Boolean.parseBoolean(System.getProperty("highlightReference", "true"));
	private boolean highlightTarget = Boolean.parseBoolean(System.getProperty("highlightTarget", "true"));
	private String context = "//html";

	public WebTester() {
	}

	public void init() throws Exception {
		driver = createDriver();
		implicitSec(getTimeoutSec());
		//if (maximizeWindow()) {
		//	setWindowMaximized();
		//}
		mainDriver = driver;
		mainHandle = mainDriver.getWindowHandle();
		handleStack.push(mainHandle);
	}

	public WebDriver getDriver() {
		return driver;
	}
	
	public String getInputValue(String label) throws Exception {
		return getInputValue(label, 0);
	}

	public String getInputValue(String label, int index) throws Exception {
		WebElement input = findNear(label, 0, "input[@type='text']", index);
		String value = input.getAttribute("value");
		log("getInput " + label + ": " + value);
		return value;
	}

	public void setContext(String xpath) throws Exception {
		this.context = xpath;
	}

	public void setDefaultContext() throws Exception {
		setContext("//html");
	}

	public WebElement getContextElement() throws Exception {
		return highlight(driver.findElement(By.xpath(context)), "2px dashed blue");
	}

	public void setApplication(Application newApp) throws Exception {
		if (application == null) {
			if (newApp.isServerLogAvailable()) {
				logId = getContent(newApp.getServer() + "/startLog.jsp");
			}
		} else if (!application.equals(newApp)) {
			downloadServerLog();
			if (newApp.isServerLogAvailable()) {
				logId = getContent(newApp.getServer() + "/startLog.jsp");
			}
		}
		application = newApp;
	}

	public Application getApplication() {
		return application;
	}

	public void downloadServerLog() throws Exception {
		if (application != null && application.isServerLogAvailable()) {
			serverLogs.add(getBytes(application.getServer() + "/fullLog.jsp?id=" + logId));
			getStream(application.getServer() + "/destroyLog.jsp?id=" + logId);
		}
	}

	public List<byte[]> getServerLogs() {
		return serverLogs;
	}

	public boolean maximizeWindow() {
		return Boolean.parseBoolean(System.getProperty("maximize", "true"));
	}

	public void setWindowMaximized() {
		driver.manage().window().maximize();
	}

	public void destroy() {
		try {
			driver.quit();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		try {
			mainDriver.quit();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public List<String> getTestLog() {
		return log;
	}

	/*public WebDriver createDriver() throws Exception {
		synchronized (WebTester.class) {
			FirefoxProfile ffp = new FirefoxProfile();
			ffp.setEnableNativeEvents(true);
			return new FirefoxDriver(ffp);
		}
	}*/
	
	//////
/*	public WebDriver createDriver() throws Exception {
		synchronized (WebTester.class) {
			//File file = new File("C:/Users/lietutis/workspace/AutomatedTest.git/trunk/src/WebExplorer/IEDriverServer.exe");
			//System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			System.setProperty("webdriver.ie.driver", "C:/Users/lietutis/workspace/AutomatedTest.git/trunk/src/WebExplorer/IEDriverServer.exe");
			WebDriver driver = new InternetExplorerDriver();
			return driver;
		}
	}*/
	
	public WebDriver createDriver() throws Exception {
		synchronized (WebTester.class) {
			File file =new File("C:/Users/lietutis/workspace/AutomatedTest.git/trunk/src/WebExplorer/IEDriverServer.exe");

            System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

            DesiredCapabilities cap= new DesiredCapabilities();

            cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

           // cap.setCapability("initialBrowserUrl", URL);

            cap.setJavascriptEnabled(true);

            driver=new InternetExplorerDriver(cap);
		}
		return driver;
	}

	/*public synchronized WebDriver createDriverOlder() throws Exception {
		Class<?> driverClass = Class.forName(System.getProperty("webdriver", "org.openqa.selenium.firefox.FirefoxDriver"));
		if (driverClass.equals(org.openqa.selenium.firefox.FirefoxDriver.class)) {
			FirefoxProfile ffp = new FirefoxProfile();
			ffp.setEnableNativeEvents(true);
			return new FirefoxDriver(ffp);
		} else {
			DesiredCapabilities c = DesiredCapabilities.firefox();
			c.setJavascriptEnabled(true);
			return (WebDriver) driverClass.getConstructor(Capabilities.class).newInstance(c);
		}
	}*/

	public void implicitSec(int sec) {
		driver.manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
	}

	public void implicitMilis(int sec) {
		driver.manage().timeouts().implicitlyWait(sec, TimeUnit.MILLISECONDS);
	}

	public int getTimeoutSec() {
		return 240;
	}

	public String getServerAndApplication() {
		return server + "/" + application;
	}

	public Stack<String> getHandleStack() {
		return handleStack;
	}

	public List<WebElement> findVisibleElements(By by) throws Exception {
		waitForProcessingFinished();
		List<WebElement> visibleElements = new ArrayList<WebElement>();
		List<WebElement> foundElements = null;
		if (Presentation.ZK.equals(application.getPresentation()) || Presentation.NET.equals(application.getPresentation())) {
			try {
				foundElements = getContextElement().findElements(by);
			} catch (StaleElementReferenceException ex) {
				foundElements = getContextElement().findElements(by); // FIXME
			}
			for (WebElement e : foundElements) {
				if (e.isDisplayed()) {
					visibleElements.add(e);
				}
			}
			return visibleElements;
		}
		if (Presentation.ADF.equals(application.getPresentation()) || Presentation.WICKET.equals(application.getPresentation())) {
			foundElements = driver.findElements(by);
			for (WebElement e : foundElements) {
				if (e.isDisplayed()) {
					visibleElements.add(e);
				}
			}
			return visibleElements;
		}
		throw unsupported();
	}

	public WebElement findVisibleElement(By by) throws Exception {
		waitForProcessingFinished();
		if (Presentation.ZK.equals(application.getPresentation()) || Presentation.NET.equals(application.getPresentation()) || Presentation.ADF.equals(application.getPresentation())) {
			List<WebElement> visibleElements = findVisibleElements(by);
			if (!visibleElements.isEmpty()) {
				return visibleElements.get(0);
			} else {
				throw new NoSuchElementException("Element is not displayed " + by.toString());
			}
		} else {
			return driver.findElement(by);
		}
	}

	public boolean isXpath(String labelOrPath) {
		return StringUtils.containsAny(labelOrPath, "[=@") || (labelOrPath != null && (labelOrPath.startsWith("//")) || labelOrPath.startsWith("./"));
	}

	public int findCount(String labelorPath) {
		return driver.findElements(By.xpath(labelorPath)).size();
	}

	//paskutinis try catch blokas pridetas
	public WebElement find(String labelorPath, String element, int index) throws Exception {
		// waitForProcessingFinished();
		String builtXpath = isXpath(labelorPath) ? labelorPath : buildLabelXpath(element, labelorPath);
		if (Presentation.ZK.equals(application.getPresentation()) || Presentation.NET.equals(application.getPresentation())) {
			builtXpath = builtXpath.replaceFirst("//", "./descendant::");
		}
		By by = By.xpath(builtXpath);
		if (index == 0) {
			try {
				return highlight(findVisibleElement(by));
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException("window " + driver.getTitle() + " " + e.getMessage(), e);
			}
		} else {
			try {
				return highlight(findVisibleElements(by).get(index));
			} catch (IndexOutOfBoundsException e) {
				try{
					return highlight(findVisibleElement(By.xpath("//*[contains(text(), '" + labelorPath + "')]")));
					//throw new NoSuchElementException("window " + driver.getTitle() + " " + by.toString(), e);
				} catch (NoSuchElementException ex){
					throw new NoSuchElementException("window " + driver.getTitle() + " " + by.toString(), e);
				}
			}
		}
	}

	public WebElement find(String labelorPath, int index) throws Exception {
		return find(labelorPath, "*", index);
	}

	public WebElement find(String labelorPath, String element) throws Exception {
		return find(labelorPath, element, 0);
	}

	public WebElement find(String labelorPath) throws Exception {
		return highlight(find(labelorPath, 0));
	}

	public WebElement findQuiet(String labelorPath) throws Exception {
		try {
			return find(labelorPath);
		} catch (NoSuchElementException e) {
			return null;
		}
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

	public void clickAll(String... labels) throws Exception {

		for (String label : labels) {

			click(label);
		}

	}

	public void findAll(String... labels) throws Exception {
		for (String label : labels) {
			highlight(find(label));
		}
	}

	public void failIfFound(String element, String attribute, String value) throws Exception {
		waitForProcessingFinished();
		implicitMilis(300);
		try {
			find("//" + element + "[@" + attribute + "'" + value + "']");
			Assert.fail("Element shoud not be found with value: " + value);
		} catch (NoSuchElementException e) {
		} finally {
			implicitSec(getTimeoutSec());
		}
	}

	public void failIfFoundByText(String... texts) throws Exception {
		// waitForProcessingFinished();
		implicitMilis(100);
		for (String text : texts) {
			try {
				find(text);
				implicitSec(getTimeoutSec());
				Assert.fail("Element shoud not be found: " + text);
			} catch (NoSuchElementException e) {
			}
		}
		implicitSec(getTimeoutSec());
	}

	public boolean isTextPresent(String text) throws Exception {
		waitForProcessingFinished();
		implicitMilis(100);
		try {
			find(text);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} finally {
			implicitSec(getTimeoutSec());
		}
	}

	public void rightClick(String rightClickOn, String select) throws Exception {
		WebElement parentMenu = find(rightClickOn);

		Actions act = new Actions(driver); // Create Action object for Driver

		act.contextClick(parentMenu).build().perform(); // Context Click

		click(select);

	}

	public void doubleClickNear(String label, String element) throws Exception {
		doubleClickNear(label, element, 0);
	}

	public void doubleClickNear(String label, String element, int index) throws Exception {

		log("[" + label + "]..." + element + " (click near)");

		highlight(findNear(label, index, element, 0));
		Actions action = new Actions(driver);
		action.doubleClick(findNear(label, index, element, 0));
		action.perform();
	}

	public void clickNear(String label, String element, int index) throws Exception {
		log("[" + label + "]..." + element + " (click near)");
		findNear(label, index, element, 0).click();
	}

	public void clickNear(String label, String element) throws Exception {
		clickNear(label, element, 0);
	}

	public WebElement highlight(WebElement we) throws Exception {
		return highlightTarget ? highlight(we, "2px solid red") : we;
	}

	public WebElement highlightDashed(WebElement we) throws Exception {
		return highlightReference ? highlight(we, "2px dashed red") : we;
	}

	public WebElement highlight(WebElement we, String border) throws Exception {
		waitForProcessingFinished();
		Assert.assertNotNull(we);
		runJavaScript(we, "style.border='" + border + "'");
		return we;
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
	}

	public void doubleClick(String text) throws Exception {
		doubleClick(text, 0);
	}

	public void doubleClick(String text, int index) throws Exception {
		log("[" + text + "]");
		highlight(find(text, index));
		Actions action = new Actions(driver);
		action.doubleClick(find(text, index));
		action.perform();

	}

	public void login(Application app, String usr, String pass) throws Exception {
		setApplication(app);
		log(application.getFullPath() + " " + usr + "/" + pass);
		get(application.getFullPath());

		highlight(driver.findElement(By.id("LoginForm_username"))).sendKeys(usr);
		highlight(driver.findElement(By.id("LoginForm_password"))).sendKeys(pass);
		highlight(driver.findElement(By.xpath("//*/button"))).click();
		log("Submited");
	}
	
	public void loginFB(Application app, String usr, String pass) throws Exception {
		setApplication(app);
		log(application.getFullPath() + " " + usr + "/" + pass);
		get(application.getFullPath());

		//highlight(driver.findElement(By.xpath("//*[contains(@id,'email')]")));
		//highlight(driver.findElement(By.xpath("//*[contains(@id,'pass')]")));
		//highlight(driver.findElement(By.xpath("//*[contains(@value,'Log In')]"))).click();
		//log("Submited");
	}
	
	public void loginBux(Application app, String usr, String pass) throws Exception {
		setApplication(app);
		log(application.getFullPath() + " " + usr + "/" + pass);
		get(application.getFullPath());

		highlight(driver.findElement(By.xpath("//*[contains(@src, 'images2/login.jpg')]"))).click();
		highlight(driver.findElement(By.name("username"))).sendKeys(usr);
		highlight(driver.findElement(By.name("password"))).sendKeys(pass);
		log("10s laukiama, kol ivesite capathca");
		sleep(10);
		highlight(driver.findElement(By.xpath("//*[contains(@value, 'Submit')]"))).click();
		//highlight(driver.findElement(By.xpath("//*/button"))).click();
		log("Submited");
	}
	
		public void selectDropDownText(String mainMenu, String subMenu) throws Exception{
			Actions builder = new Actions(driver);
			builder.moveToElement(find(mainMenu)).build().perform();
			builder.moveToElement(find(subMenu)).build().perform();
			highlight(find(subMenu)).click();
		}
		
		public void selectItemByDataIdAndText(String mainMenu, String subMenu, int index) throws Exception{
			click("//*[contains(@data-id, "+mainMenu+")]", index);
			sleep(1);
			click("(//*[text()='"+subMenu+"'])[last()]",index);
		}
		
		public void selectItemByDataIdAndText(String mainMenu, String subMenu) throws Exception{
			click("//*[contains(@data-id, "+mainMenu+")]", 0);
			sleep(1);
			click("(//*[text()='"+subMenu+"'])[last()]", 0);
		}
		
	public void casLogin(Application app, String usr, String pass) throws Exception {
		setApplication(app);
		log(application.getFullPath() + " " + usr + "/" + pass);
		get(application.getFullPath());

		driver.findElement(By.name("username")).sendKeys(usr);
		driver.findElement(By.name("password")).sendKeys(pass);
		driver.findElement(By.name("submit")).submit();
		log("Submited");
	}

	
	public byte[] screenshot() throws IOException {
		if (driver instanceof TakesScreenshot) {
			return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		} else {
			return new byte[] {};
		}
	}

	public void assertNoError() throws Exception {
		failIfFoundByText("KLAIDA", "Klaida", "klaida", "Klaidos", "KLAIDOS", "Exception", "exception", "ERROR", "Error");
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public void get(String url) {
		driver.get(url);
		driver.manage().deleteCookieNamed(LOG_SESSION_ID);
		driver.manage().addCookie(new Cookie(LOG_SESSION_ID, logId, null, new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(10))));
	}

	public void log(String message) throws Exception {
		log.add(message);
		System.out.println(message);
	}

	public void logToServer(String message) throws Exception {
		if (application.isServerLogAvailable()) {
			String url = server + "/message.jsp?" + URLEncodedUtils.format(Arrays.asList(new BasicNameValuePair[] { new BasicNameValuePair("id", logId), new BasicNameValuePair("msg", "===========CLIENT===========: " + message) }), "UTF-8");
			(new DefaultHttpClient()).execute(new HttpGet(url));
		}
	}

	public void addToken(String token) throws Exception {
		if (application.isServerLogAvailable()) {
			String url = server + "/addToken.jsp?" + URLEncodedUtils.format(Arrays.asList(new BasicNameValuePair[] { new BasicNameValuePair("token", token) }), "UTF-8");
			(new DefaultHttpClient()).execute(new HttpGet(url));
		}
	}

	public void waitForProcessingFinished() {
		if (Presentation.ADF.equals(application.getPresentation())) {
			ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					try {
						return !(Boolean) ((JavascriptExecutor) d).executeScript("return _pprBlocking");
					} catch (WebDriverException wde) {
						if (wde.getMessage().contains("Modal dialog present") || wde.getMessage().contains("is not defined")) {
							return false;
						} else {
							throw wde;
						}
					} catch (NullPointerException npe) {
						return false;
					}
				}
			};
			WebDriverWait wait = new WebDriverWait(driver, getTimeoutSec(), 400);
			wait.withMessage("_pprBlocking should be false");
			wait.until(e);
			return;
		}
		if (Presentation.ZK.equals(application.getPresentation())) {
			ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					try {
						return ((JavascriptExecutor) d).executeScript("return zAu.processing()") == null;
					} catch (WebDriverException wde) {
						if (wde.getMessage().contains("is not defined")) {
							return true;
						} else {
							throw wde;
						}
					}
				}
			};
			(new WebDriverWait(driver, getTimeoutSec(), 400)).withMessage("zAu.processing() should be false").until(e);
			return;
		}

		if (Presentation.WICKET.equals(application.getPresentation())) {
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
		if (Presentation.NET.equals(application.getPresentation())) {
			try {
				sleep(600, TimeUnit.MILLISECONDS);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					boolean insoft_ajax_processing = false;
					try {
						insoft_ajax_processing = (Boolean) ((JavascriptExecutor) d).executeScript("return insoft_ajax_processing == 0");
					} catch (WebDriverException wde) {
						if (!wde.getMessage().contains("Modal dialog present") && !wde.getMessage().contains("is not defined")) {
							throw wde;
						}
					} catch (NullPointerException npe) {
					}
					if (!insoft_ajax_processing) {
						return true;
					}
					boolean tIconTLoadingDisplayed = false;
					try {
						tIconTLoadingDisplayed = driver.findElement(By.className("t-icon t-loading")).isDisplayed();
					} catch (NoSuchElementException e2) {
					}
					if (!tIconTLoadingDisplayed) {
						return true;
					}
					boolean globalLoadingDialogDisplayed = false;
					try {
						globalLoadingDialogDisplayed = driver.findElement(By.id("global-loading-dialog")).isDisplayed();
					} catch (NoSuchElementException e2) {
					}
					return !globalLoadingDialogDisplayed;
				}
			};
			WebDriverWait wait = new WebDriverWait(driver, getTimeoutSec(), 1000);
			wait.withMessage("Partial loading was too long.");
			wait.until(e);
			return;
		}
		throw unsupported();
	}

	
	public void setInputById(String id, String text) throws Exception{
		WebElement element = highlight(driver.findElement(By.id(id)));
		//clear if there is info
		element.clear();
		element.sendKeys(text);
		log("id= "+id+" input= "+text);
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

	public void setSelectText(String referenceLabelorPath, String visibleText, int index) throws Exception {
		log(referenceLabelorPath + "=<" + visibleText + ">");
		if (Presentation.ZK.equals(application.getPresentation())) {
			clickNear(referenceLabelorPath, "i[@class='z-combobox-btn']", index);
			List<WebElement> options = getDriver().findElements(By.xpath("//td[@class='z-comboitem-text']"));
			for (WebElement option : options) {
				if (option.getText().contains(visibleText))
					option.click();
			}
		}
		if (Presentation.ADF.equals(application.getPresentation())) {
			WebElement e = findNear(referenceLabelorPath, "select");
			(new Select(e)).selectByVisibleText(visibleText);
			// perselektinam is naujo
			findNear(referenceLabelorPath, "select").sendKeys(Keys.TAB);
		}
		if (Presentation.WICKET.equals(application.getPresentation())) {
			WebElement e = findNear(referenceLabelorPath, "select");
			(new Select(e)).selectByVisibleText(visibleText);
			// perselektinam is naujo
			findNear(referenceLabelorPath, "select").sendKeys(Keys.TAB);
		}
		if (Presentation.NET.equals(application.getPresentation())) {
			try {
				String vtisDropdownId = findNear(referenceLabelorPath, "input[@class='ui-autocomplete-input ui-widget']").getAttribute("id");
				click("//input[@id='" + vtisDropdownId + "']/following-sibling::button[@title='Pasirinkti']");
				List<WebElement> elementsToSelect = findVisibleElements(By.xpath("//li[@class='ui-menu-item']/a"));
				for (WebElement element: elementsToSelect) {
					if (element.getText().contains(visibleText)) {
						element.click();
						break;
					}
				}
			} catch (NoSuchElementException e) {
				WebElement select = findNear(referenceLabelorPath, "select");
				(new Select(select)).selectByVisibleText(visibleText);
				findNear(referenceLabelorPath, "select").sendKeys(Keys.TAB);
			}
		}
	}

	public void setSelectText(String referenceLabelorPath, String visibleText) throws Exception {
		setSelectText(referenceLabelorPath, visibleText, 0);
	}

	public void uploadFile(String labelorPath, int index) throws Exception {
		log(" (upload)");
		LocalFileDetector detector = new LocalFileDetector();

		((RemoteWebElement) findNear(labelorPath, "input[@type='file']")).setFileDetector(detector);
		findNear(labelorPath, index, "input[@type='file']", 0).sendKeys(photoFromClasspathToFile().getAbsolutePath());

	}

	public void uploadFile(String labelorPath, String fileName) throws Exception {
		log(" (upload)");
		// if (Presentation.ZK.equals(application.getPresentation())) {
		LocalFileDetector detector = new LocalFileDetector();
		// File file = detector.getLocalFile();
		((RemoteWebElement) findNear(labelorPath, "input[@type='file']")).setFileDetector(detector);
		findNear(labelorPath, "input[@type='file']").sendKeys(photoFromClasspathToFile(fileName).getAbsolutePath());
		// }
	}

	public void uploadFile(String labelorPath) throws Exception {
		uploadFile(labelorPath, "pdftest.pdf");
	}

	public void uploadFilePDF(String labelorPath, int index) throws Exception {

		log(" (upload)");
		LocalFileDetector detector = new LocalFileDetector();
		((RemoteWebElement) findNear(labelorPath, "input[@type='file']")).setFileDetector(detector);
		findNear(labelorPath, index, "input[@type='file']", 0).sendKeys(photoFromClasspathToFile("pdftest.pdf").getAbsolutePath());

	}

	private static File photoFromClasspathToFile(String name) throws Exception {
		File file = new File(System.getProperty("java.io.tmpdir") + name);
		if (!file.exists()) {
			FileOutputStream fos = new FileOutputStream(file, false);
			try {
				IOUtils.copy(WebTester.class.getClassLoader().getResourceAsStream(name), fos);
			} finally {
				IOUtils.closeQuietly(fos);
			}
		}
		return file;
	}

	private static File photoFromClasspathToFile() throws Exception {
		return photoFromClasspathToFile("mickey.jpg");
	}

	public void uploadPhoto(String labelorPath) throws Exception {
		log(labelorPath + " (upload photo)");
		findNear(labelorPath, "input[@type='file']").sendKeys(photoFromClasspathToFile().getAbsolutePath());
	}

	public void setTextArea(String label, int labelIndex, String value, int valueIndex) throws Exception {
		log(label + "=" + value);
		findNear(label, labelIndex, "textarea", valueIndex).sendKeys(value, Keys.TAB);
	}

	public void setTextArea(String label, String value) throws Exception {
		setTextArea(label, 0, value, 0);
	}

	public void setTextArea(String label, String value, int index) throws Exception {
		setTextArea(label, 0, value, index);
	}

	public void switchWindow(String handle) throws Exception {
		driver = driver.switchTo().window(handle);
		log("switched to " + driver.getTitle());
	}

	public void switchFrame(int frameNo) {
		driver = driver.switchTo().frame(frameNo);
	}

	public void switchFrame(WebElement we) {
		driver = driver.switchTo().frame(we);
	}

	public void switchMainWindow() {
		driver = driver.switchTo().window(mainHandle);
		handleStack.clear();
		handleStack.push(mainHandle);
	}

	public void clickLabelOpenClassifierPopup(String label) throws Exception {
		clickNear(label, "img[contains(@src, 'classifSel')]");
		waitForProcessingFinished();
		assertNoError();
		waitForWindowOpen();
		switchWindow(handleStack.push(findNewWindow()));
		switchFrame(0);
		sleepMillis(200);
		waitForProcessingFinished();
	}

	public void clickLabelOpenPopup(String text, int index) throws Exception {
		click(text, index);
		waitForProcessingFinished();
		assertNoError();
		waitForWindowOpen();
		switchWindow(handleStack.push(findNewWindow()));
		switchFrame(0);
		sleepMillis(200);
		waitForProcessingFinished();
	}

	public void clickLabelOpenBirtPopup(String text) throws Exception {
		click(text, 0);
		waitForProcessingFinished();
		assertNoError();
		waitForWindowOpen();
		switchWindow(handleStack.push(findNewWindow()));
		switchFrame(0);
		sleepMillis(600);
		List<WebElement> loadingMessage = null;
		boolean popupLoading = true;
		while (popupLoading) {
			loadingMessage = getDriver().findElements(By.xpath("//*[contains(text(), 'Apdorojama, prašom palaukti ...')]"));
			if (loadingMessage.size() > 0)
				if (loadingMessage.get(0).isDisplayed())
					sleep(5);
				else
					popupLoading = false;
			else
				popupLoading = false;
		}
	}

	public void waitUntilElementDoesntExist(By by, int waitSeconds, int noOfRetries) {
		getDriver().manage().timeouts().implicitlyWait(waitSeconds, TimeUnit.SECONDS);
		boolean elementDisappeared = false;
		for (int i = 0; i < noOfRetries; i++) {
			System.out.println("index " + i);
			try {
				getDriver().findElement(by);
				sleep(waitSeconds);
			} catch (Exception e) {
				elementDisappeared = true;
				break;
			}
		}
	}

	public void clickLabelOpenPopup(String text) throws Exception {
		clickLabelOpenPopup(text, 0);
	}

	public void clickLabelAcceptConfirmation(String text) throws Exception {
		click(text);
		Alert pranesimas = driver.switchTo().alert();
		log("confirmation: " + pranesimas.getText());
		pranesimas.accept();
	}

	public void acceptConfirmation() throws Exception {
		Alert pranesimas = driver.switchTo().alert();
		log("confirmation: " + pranesimas.getText());
		pranesimas.accept();
	}

	public void waitForWindowOpen() {
		WebDriverWait wait = new WebDriverWait(driver, getTimeoutSec(), 900);
		wait.withMessage("window set should change");
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				Set<String> handles = driver.getWindowHandles();
				handles.removeAll(handleStack);
				return !handles.isEmpty();
			}
		});
	}

	public void waitForWindowsClose() {
		WebDriverWait wait = new WebDriverWait(driver, 60, 900);
		wait.withMessage("window set should diminish");
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return driver.getWindowHandles().size() < handleStack.size();
			}
		});
	}

	public void waitForSingleWindow() {
		WebDriverWait wait = new WebDriverWait(driver, getTimeoutSec(), 300);
		wait.withMessage("window set should diminish");
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return driver.getWindowHandles().size() == 1;
			}
		});
	}

	public String findNewWindow() {
		Set<String> handles = driver.getWindowHandles();
		handles.removeAll(handleStack);
		try {
			return handles.iterator().next();
		} catch (java.util.NoSuchElementException n) {
			throw new IllegalStateException("New popup handle can not be null.", n);
		}
	}

	public void clickLabelClosePopup(String text) throws Exception {
		clickLabelClosePopup(text, 0);
	}

	public void clickLabelClosePopup(String text, String targetElement, int index) throws Exception {
		click(text, targetElement, index);
		alterFrame();
	}

	public void clickLabelClosePopup(String text, int index) throws Exception {
		click(text, index);
		alterFrame();
	}

	public void closePopUp() throws Exception{
		driver.close();
		alterFrame();
	}
	
	private void alterFrame() throws Exception {
		waitForWindowsClose();
		handleStack.pop();
		switchWindow(handleStack.peek());
		sleepMillis(300);
		if (!mainHandle.equals(handleStack.peek())) {
			switchFrame(0);
		}
		sleepMillis(300);
	}

	public void clickLabelCloseAndOpenPopup(String text) throws Exception {
		click(text);
		// waitForProcessingFinished();
		waitForWindowOpen();
		handleStack.pop();
		switchWindow(handleStack.push(findNewWindow()));
		switchFrame(0);
		sleepMillis(300);
		waitForProcessingFinished();
	}


	public String buildLabelXpath(String element, String labelorPath) {
		if (Presentation.ADF.equals(application.getPresentation()) || Presentation.WICKET.equals(application.getPresentation()) || Presentation.NET.equals(application.getPresentation())) {
			return "//" + element + "[text()='" + labelorPath + "' or @title='" + labelorPath + "']";
		}
		if (Presentation.ZK.equals(application.getPresentation())) {
			return "//" + element + "[contains(text(),'" + labelorPath + "') or contains(@title,'" + labelorPath + "')]";
		}
		throw unsupported();
	}

	public RuntimeException unsupported() {
		return new IllegalStateException("Unsupported presentation " + application.getPresentation());
	}

	public void clickButton(String text, int index) throws Exception {
		waitForProcessingFinished();
		click(text, "button", index);
	}

	public void clickButton(String text) throws Exception {
		clickButton(text, 0);
	}

	public void sleep(long sec) throws Exception {
		sleep(sec, TimeUnit.SECONDS);
	}

	public void sleepMillis(long millis) throws Exception {
		sleep(millis, TimeUnit.MILLISECONDS);
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

	private byte[] getBytes(String url) throws Exception {
		return IOUtils.toByteArray(getStream(url));
	}

	private InputStream getStream(String url) throws Exception {
		return (new DefaultHttpClient()).execute(new HttpGet(url)).getEntity().getContent();
	}

	private String getContent(String url) throws Exception {
		return IOUtils.toString(getStream(url), "UTF-8");
	}

	public List<String> getServerTailLog() throws Exception {
		if (!serverLogs.isEmpty()) {
			byte[] bytes = serverLogs.get(serverLogs.size() - 1);
			int from = (bytes.length - 1000) > 0 ? (bytes.length - 1000) : 0;
			BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes, from, bytes.length - from - 1), "UTF-8"), 10000);
			List<String> result = new ArrayList<String>();
			int i = 0;
			while (br.ready()) {
				String line = br.readLine();
				if (i++ > 0) {
					result.add(line);
				}
			}
			return Collections.unmodifiableList(result);
		} else {
			return Arrays.asList("none");
		}
	}

	public boolean isHighlightReference() {
		return highlightReference;
	}

	public void setHighlightReference(boolean highlightReference) {
		this.highlightReference = highlightReference;
	}

	public boolean isHighlightTarget() {
		return highlightTarget;
	}

	public void setHighlightTarget(boolean highlightTarget) {
		this.highlightTarget = highlightTarget;
	}

	public void acceptConfirmationClosePopup() throws Exception {
		acceptConfirmation();
		waitForWindowsClose();
		handleStack.pop();
		switchWindow(handleStack.peek());
		sleepMillis(300);
		if (!mainHandle.equals(handleStack.peek())) {
			switchFrame(0);
		}
		sleepMillis(300);
	}

	public void clickByCoordinates(WebElement el, int x, int y) throws Exception {
		Actions clicker = new Actions(driver);
		clicker.moveToElement(el).moveByOffset(x, y).click().perform();
	}

}