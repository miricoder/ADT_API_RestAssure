package com.adt.ibp.ui_automation.Privacy.utilities;

import com.google.common.base.Function;
import com.google.common.io.Files;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.AssertJUnit;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.log4testng.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SeleniumGlobalLibraries {
	final static Logger logger = Logger.getLogger(SeleniumGlobalLibraries.class);
	private WebDriver driver;
	private DesiredCapabilities capability;
	private static String screenshotPath;
	public String screenshotName;
	private boolean isRemote;
	static WebElement dropdown;
	// For capturing locators in Reports
	Base b = new Base();
//[USED FOR REPORTER NG SCREENSHOT]
	public static String screenShotName;
	public static Properties LOC = new Properties();
	public List<String> errorScreenshots;

	public SeleniumGlobalLibraries(WebDriver _driver) {
		driver = _driver;
	}


	/**
	 * Delete Files Older than n number Of days Using Java
	 * n=30days in the ase of extent reports
	 */
	public void removeOldFiles(long days, String fileExtension) {

		File folder = new File("src/test/resources/reports");

		if (folder.exists()) {

			File[] listFiles = folder.listFiles();

			long eligibleForDeletion = System.currentTimeMillis() -
					(days * 24 * 60 * 60 * 1000L);

			for (File listFile : listFiles) {

				if (listFile.getName().endsWith(fileExtension) &&
						listFile.lastModified() < eligibleForDeletion) {

					if (!listFile.delete()) {

						System.out.println("Sorry Unable to Delete Files..");

					}
				}
			}
		}
	}


	/**
	 * Driver/Browsers/Test Envoriment Setup
	 * 
	 * @return
	 */
	// [MAVEN COMMAND]
	public WebDriver mvnParamterizedBrowserCommand() {
		String browserName = System.getProperty("paramBrowser");
		try {
			if (browserName.contains("chrome")) {
				driver = startChromeDriver();
			} else if (browserName.contains("firefox")) {
				driver = StartFireFoxDriver();
			} else if (browserName.contains("ie")) {
				driver = startIEDriver();
			} else if (browserName.contains("--headless")) {
				driver = startChromeHeadless();
			} else {
				driver = startIEDriver();
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		return driver;
	}

	// [LOCAL/CODE LEVEL COMMAND]
	public WebDriver startLocalBrowsers(String browser) {
		try {
			if (browser.contains("chrome")) {
				driver = startChromeDriver();
			} else if (browser.contains("firefox")) {
				driver = StartFireFoxDriver();
			} else if (browser.contains("ie")) {
				driver = startIEDriver();
			} else if (browser.contains("--headless")) {
				driver = startChromeHeadless();

			} else {
				driver = startIEDriver();
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		return driver;
	}

	// [REMOTE_GridEXTRA/CODE LEVEL COMMAND]
	// [Troubleshoot>Grid Extra] Drivers are automatically placed inside //tmp
	// ___________________________folder from where node cannot read them and
	// therefore tests fail..

	public WebDriver startRemoteBrowser(String hubURL, String browser) {
		DesiredCapabilities cap = null;
		ChromeOptions options = new ChromeOptions();
		try {
			if (browser.contains("chrome")) {
//				cap = DesiredCapabilities.chrome();
				cap.setBrowserName("chrome");
				options.addArguments("--start-maximized");
				cap.setCapability(ChromeOptions.CAPABILITY, options);
			} else if (browser.contains("ie")) {
				// [TROUBLESHOOT]: IE is not working for Grid Extra
//				cap = DesiredCapabilities.internetExplorer();
				cap.setBrowserName("iexplore");
				cap.setCapability("ignoreProtectedModeSettings", true);
				cap.setCapability("ie.ensureCleanSession", true);
				cap.setPlatform(Platform.ANY);

			} else if (browser.contains("firefox")) {
				// [TROUBLESHOOT]: FireFox Window not maximizing for Grid Extra
//				cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				cap.setPlatform(Platform.ANY);

			} else {
				logger.info("You choose: '" + browser + "', Currently, framework does't support it.");
				logger.info("starting default browser 'Internet Explorer'");
//				cap = DesiredCapabilities.internetExplorer();
				cap.setCapability("ignoreProtectedModeSettings", true);
				cap.setCapability("ie.ensureCleanSession", true);
			}
			driver = new RemoteWebDriver(new URL(hubURL), cap);
			isRemote = true;
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	// [REMOTE_SE_GRID3/CODE LEVEL COMMAND Control via below mentioned]:
	// __________________________1. Parameters (browsers via testNG XML files)
	@Parameters("grid3Browser")
	public WebDriver gridBrowsers(String hubURL, @Optional String grid3Browser) {
		DesiredCapabilities capGrid3 = null;
		ChromeOptions options = new ChromeOptions();
		try {
			if (grid3Browser.contains("firefox")) {
				// [TROUBLESHOOT]: FireFox Window not maximizing for Selenium Grid 3
//				capGrid3 = DesiredCapabilities.firefox();
			} else if (grid3Browser.equals("chrome")) {
//				capGrid3 = DesiredCapabilities.chrome();
				options.addArguments("--start-maximized");
				capGrid3.setCapability(ChromeOptions.CAPABILITY, options);
				capGrid3.setPlatform(Platform.ANY);
			} else if (grid3Browser.equals("iexplore")) {
				// [TROUBLESHOOT]: IE is not working for Selenium Grid 3
//				capGrid3 = DesiredCapabilities.internetExplorer();
				capGrid3.setPlatform(Platform.WINDOWS);
				capGrid3.setBrowserName("iexplore");
				capGrid3.setCapability("ignoreProtectedModeSettings", true);
				capGrid3.setCapability("ie.ensureCleanSession", true);

			} else {
				// [TROUBLESHOOT]: IE is not working for Selenium Grid 3
//				DesiredCapabilities.internetExplorer();
				capGrid3.setBrowserName("iexplore");
				capGrid3.setPlatform(Platform.WINDOWS);
			}
			driver = new RemoteWebDriver(new URL(hubURL), capGrid3);
			isRemote = true;
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;

	}

	public WebDriver startChromeHeadless() throws IOException {

		try {
			LoggingPreferences loggingPreferences = new LoggingPreferences();
			loggingPreferences.enable(LogType.PERFORMANCE, Level.ALL);
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setCapability( "goog:loggingPrefs", loggingPreferences );
			chromeOptions.addArguments("--headless");
			System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");
			driver = new ChromeDriver(chromeOptions);
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		return driver;
	}

	public WebDriver startChromeDriver() {
		try {
			System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");
//			DesiredCapabilities cap = DesiredCapabilities.firefox();
			/***
			 * Update on Friday December 17 7:08PM
			 * Below API/Objects are used for capturing WSS response in Subscriber Class which is in Admin Page
			 * LoggingPreferences
			 * ChromeOptions
			 * driver = new ChromeDriver(chromeOptions);
			 * Previous version driver = new ChromeDriver(chromeOptions);
			 */
			LoggingPreferences loggingPreferences = new LoggingPreferences();
			loggingPreferences.enable(LogType.PERFORMANCE, Level.ALL);
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setCapability( "goog:loggingPrefs", loggingPreferences );
//			driver = new ChromeDriver();
			driver = new ChromeDriver(chromeOptions);
			driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		return driver;
	}
//	public WebDriver startChromeDriver() {
//		try {
//			System.setProperty("webdriver.chrome.driver", "src/test/resources/com/MC/drivers/chromedriver.exe");
//			DesiredCapabilities cap = DesiredCapabilities.firefox();
//			// driver = new ChromeDriver();
//			WebDriverManager.chromedriver().setup();
//			driverChrome = new ChromeDriver();
//			driverChrome.manage().window().maximize();
//			driverChrome.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
//			driverChrome.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//		} catch (Exception e) {
//			logger.error("Error: ", e);
//		}
//		return driver;
//	}

	public WebDriver startIEDriver() {
		try {
			System.setProperty("webdriver.ie.driver", "src/test/resources/com/MC/drivers/IEDriverServer.exe");
//			capability = DesiredCapabilities.internetExplorer();
			capability.setCapability("ignoreProtectedModeSettings", true);
			capability.setCapability("ie.ensureCleanSession", true);
			driver = new InternetExplorerDriver(capability);
			driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("Error: ", e);
			AssertJUnit.assertTrue(false);
		}
		return driver;
	}
//	public WebDriver startIEDriver() {
//		try {
//			System.setProperty("webdriver.ie.driver", "src/test/resources/com/MC/drivers/IEDriverServer.exe");
//			capability = DesiredCapabilities.internetExplorer();
//			capability.setCapability("ignoreProtectedModeSettings", true);
//			capability.setCapability("ie.ensureCleanSession", true);
//			// driver = new InternetExplorerDriver(capability);
//			WebDriverManager.iedriver().setup();
//			driverIE = new InternetExplorerDriver();
//			driverIE.manage().window().maximize();
//			driverIE.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
//			driverIE.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//		} catch (Exception e) {
//			logger.error("Error: ", e);
//			AssertJUnit.assertTrue(false);
//		}
//		return driver;
//	}

	public WebDriver StartFireFoxDriver() {

		try {
			// how to handle untrusted SSL Certificates in Fire Fox
			FirefoxProfile profile = new FirefoxProfile();
			profile.setAcceptUntrustedCertificates(true);
			// Handling proxy issues
			System.setProperty("webdriver.gecko.driver", "src/test/resources/com/MC/drivers/geckodriver.exe");
			driver = new FirefoxDriver();
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("Error: ", e);
			AssertJUnit.assertTrue(false);
		}
		return driver;
	}

	/**
	 * Waits / Customer/Explicit/Fluent
	 */

	public void customeWait(double unitSeconds) {
		try {
			Thread.sleep((long) unitSeconds * 1000);
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
	}

	/***
	 * This is an explicitWait for element presence
	 * 
	 * @param by
	 * @return
	 */
	public WebElement waitUntilElementVisibility(By by) {
		WebElement dynamicElem = null;
		try {
			dynamicElem = (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return dynamicElem;
	}

	/***
	 * This is an explicitWait for element presence
	 * 
	 * @param by
	 * @return
	 */
	public WebElement waitUntilElementPresence(By by) {
		WebElement dynamicElem = null;
		try {
			dynamicElem = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return dynamicElem;
	}

	/***
	 * This is an explicitWait for element to be clickable
	 * 
	 * @param by
	 * @return
	 */
	public WebElement waitUntilElementClickable(By by) {
		WebElement dynamicElem = null;
		try {
			dynamicElem = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return dynamicElem;
	}

	// [INFO] Used for LOC
	public WebElement waitUntilElementClickable2(WebElement by) {
		WebElement dynamicElem = null;
		try {
			dynamicElem = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return dynamicElem;
	}

	/***
	 * This is a fluent wait, waits dynamically for a WebElement and polls the
	 * source html
	 * 
	 * @param by
	 * @return WebElement
	 */
	public WebElement fluentWait(final By by) {
		WebElement targetElem = null;
		try {
			@SuppressWarnings("deprecation")
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(60))
					.pollingEvery(Duration.ofSeconds(3)).ignoring(NoSuchElementException.class);
			targetElem = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(by);
				}
			});
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return targetElem;
	}

	// [INFO] Used for LOC
	public WebElement fluentWait2(final WebElement by) {
		WebElement targetElem = null;
		try {
			@SuppressWarnings("deprecation")
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(60))
					.pollingEvery(Duration.ofSeconds(3)).ignoring(NoSuchElementException.class);
			targetElem = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement((By) by);
				}
			});
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return targetElem;
	}

	/**
	 * [EXTENT REPORT]____________________________________________________________
	 * [<not used captureScreenshot>]_____________________________________________
	 * [<used for extent report - getScreenshotPath>]_____________________________
	 */
//[USED FOR EXTENT_REPORT(SHETTY's WAY): ]
	public String getScreenshotPath(String TestCaseName, WebDriver driver) throws IOException {

		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String destPath = System.getProperty("user.dir") + "//reports//" + TestCaseName + ".png";
		File file = new File(destPath);
		FileUtils.copyFile(source, file);

		return destPath;
	}

// [USED FOR REPORTER NG: SAMPLE/MIGHT BE DELETED AND REPLACED BY THE ABOVE]
	public void getScreenshotPathRG() throws IOException {
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String timeStamp = getCurrentTime();
		screenshotName = timeStamp + "error.png";
		FileUtils.copyFile(srcFile,
				new File(System.getProperty("user.dir") + "//target//surefire-reports//html//" + screenshotName));

	}

	public String captureScreenshot(String screenshotFileName, String filePath, WebDriver driver) {
		String screenshotFilePath = filePath;
		String timeStamp = getCurrentTime();
		try {
			if (filePath.isEmpty()) {
				checkDirectory("target/reports/");
				screenshotFilePath = "target/reports/" + screenshotFileName + timeStamp + ".png";
			} else {
				checkDirectory(filePath);
				screenshotFilePath = filePath + screenshotFileName + timeStamp + ".png";
			}
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			Files.copy(srcFile, new File(screenshotFilePath));

		} catch (Exception e) {
			e.printStackTrace();

		}
		System.out.println("Screenshot captured: " + screenshotFilePath);
		return screenshotFilePath;
	}

	private void checkDirectory(String inputPath) {
		File file = new File(inputPath);
		String abPath = file.getAbsolutePath();
		File file2 = new File(abPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
				System.out.println("Directories are created...");
			} else {
				System.out.println("Directories are not created...");
			}
		}

	}

	public String getCurrentTime() {
		String currentTime = null;
		try {
			Date date = new Date();
			logger.debug("Date: " + date);
			String tempTime = new Timestamp(date.getTime()).toString();
			currentTime = tempTime.replace(" ", "").replace("-", "").replace(":", "").replace(".", "");
			logger.debug("tempTime: " + tempTime);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return currentTime;
	}

	/***
	 * Extent Report Locator and Conditional Report Logs
	 * @param locator
	 * @param value
	 * @throws IOException
	 */
	public void type(String locator, String value) throws IOException {

		FileInputStream fis = new FileInputStream("src/test/java/com/adt/ibp/ui_automation/Privacy/utilities/locators.properties");
		LOC.load(fis);
		if (locator.endsWith("_CSS")) {
			try{
				driver.findElement(By.cssSelector(LOC.getProperty(locator))).sendKeys(value);
				b.test.log(LogStatus.PASS, "@Locator/cssElements ==> " + locator);
			}catch (Exception e){
				b.test.log(LogStatus.FAIL, "@Locator/cssElements ==> " + locator);
				e.printStackTrace();
			}
		} else if (locator.endsWith("_XPATH")) {
			try{
				driver.findElement(By.xpath(LOC.getProperty(locator))).sendKeys(value);
				b.test.log(LogStatus.PASS, "@Locator/xpathElements ==> " + locator);
			}catch (Exception e){
				b.test.log(LogStatus.FAIL, "@Locator/xpathElements ==> " + locator);
				e.printStackTrace();
			}
		} else if (locator.endsWith("_ID")) {
			try{
				driver.findElement(By.id(LOC.getProperty(locator))).sendKeys(value);
				b.test.log(LogStatus.PASS, "@Locator/idElements ==> " + locator);
			}catch (Exception e){
				b.test.log(LogStatus.FAIL, "@Locator/idElements ==> " + locator);
				e.printStackTrace();
			}
		} else {
//			b.test.log(LogStatus.FAIL, "@LOCATOR FILE NOT FOUND..... Please Check if 'Privacy/utilities/locators.properties file' Exists");

		}
	}
	public void buttonClick(String locator) throws IOException {

		FileInputStream fis = new FileInputStream("src/test/java/com/adt/ibp/ui_automation/Privacy/utilities/locators.properties");
		LOC.load(fis);
		if (locator.endsWith("_CSS")) {
			try{

				waitUntilElementClickable(By.cssSelector(LOC.getProperty(locator))).click();
				b.test.log(LogStatus.PASS, "@Locator/cssElements ==> " + locator);
			}catch (Exception e){
				b.test.log(LogStatus.FAIL, "@Locator/cssElements ==> " + locator);
				e.printStackTrace();
			}
		} else if (locator.endsWith("_XPATH")) {
			try{
				waitUntilElementClickable(By.cssSelector(LOC.getProperty(locator))).click();
				b.test.log(LogStatus.PASS, "@Locator/xpathElements ==> " + locator);
			}catch (Exception e){
				b.test.log(LogStatus.FAIL, "@Locator/xpathElements ==> " + locator);
				e.printStackTrace();
			}
		} else if (locator.endsWith("_ID")) {
			try{
				waitUntilElementClickable(By.cssSelector(LOC.getProperty(locator))).click();
				b.test.log(LogStatus.PASS, "@Locator/idElements ==> " + locator);
			}catch (Exception e){
				b.test.log(LogStatus.FAIL, "@Locator/idElements ==> " + locator);
				e.printStackTrace();
			}
		} else {
//			b.test.log(LogStatus.FAIL, "@LOCATOR FILE NOT FOUND..... Please Check if 'Privacy/utilities/locators.properties file' Exists");

		}
	}


	public void checkCurrentPageLanding(String currentUrl, String expectedUrl){
//		String currentUrl = driver.getCurrentUrl();
			try{
//				if (currentUrl.equals(LOC.getProperty("ADMIN_LANDING")))
				customeWait(30);
				assertEquals(currentUrl,LOC.getProperty(expectedUrl));
				b.test.log(LogStatus.PASS, "@Admin Page Landing URL Result ==> " + currentUrl);

			}catch (Exception e){
				e.printStackTrace();
				String actualCurrentUrl = driver.getCurrentUrl();
				b.test.log(LogStatus.FAIL, "@Admin Page Landing URL Result ==> " + actualCurrentUrl);

				/**
				 * If not landed in Admin Page remove ibpConfig.json and cameraStatusList.json files to prevent false positive tests
				 */
//				DeleteFilesFromDir remove_cameraStatusList = new DeleteFilesFromDir("src/test/resources/DynamicValueFiles/cameraStatusList.json");
//				DeleteFilesFromDir remove_ibpConfig = new DeleteFilesFromDir("src/test/resources/DynamicValueFiles/ibpConfig.json");
			}
		}

	public String envPrinterForExtentTests(String ian){
		String env=null;
		if(driver.getCurrentUrl().contains(LOC.getProperty("ADMIN_LANDING_PROD"))){
			customeWait(30);
			env="ENV: Prod    -- IAN: " + ian;
		}else if(driver.getCurrentUrl().contains(LOC.getProperty("ADMIN_LANDING_PrePROD"))){
			customeWait(30);
//			System.out.println("ENV: PreProd -- IAN: " + ian);
			env="ENV: PreProd    -- IAN: " + ian;
		}else if(driver.getCurrentUrl().contains(LOC.getProperty("ADMIN_LANDING_GPC_INT"))){
			customeWait(30);
//			System.out.println("ENV: GCP_INT -- IAN: " + ian);
			env="ENV: GCP_INT    -- IAN: " + ian;
		}else{
			logger.info("Can't find the specified environment....");

		}
		return env;
	}

	public void moveToElement(String locator) {
		try {
			FileInputStream fis = new FileInputStream("src/test/java/com/adt/ibp/ui_automation/Privacy/utilities/locators.properties");
			LOC.load(fis);
			if (locator.endsWith("_CSS")) {
				new Actions(driver).moveToElement(driver.findElement(By.cssSelector(LOC.getProperty(locator)))).build()
						.perform();
//			new Actions(driver).moveToElement(fluentWait(By.cssSelector(LOC.getProperty(locator)))).build().perform();
//			new Actions(driver).moveToElement(waitUntilElementClickable(By.cssSelector(LOC.getProperty(locator))))
//					.build().perform();
			} else if (locator.endsWith("_XPATH")) {
				new Actions(driver).moveToElement(driver.findElement(By.xpath(LOC.getProperty(locator)))).build()
						.perform();
//			new Actions(driver).moveToElement(fluentWait(By.xpath(LOC.getProperty(locator)))).build().perform();
//			new Actions(driver).moveToElement(waitUntilElementClickable(By.xpath(LOC.getProperty(locator)))).build()
//					.perform();
			} else if (locator.endsWith("_ID")) {
				new Actions(driver).moveToElement(driver.findElement(By.id(LOC.getProperty(locator)))).build()
						.perform();
//			new Actions(driver).moveToElement(fluentWait(By.id(LOC.getProperty(locator)))).build().perform();
//			new Actions(driver).moveToElement(waitUntilElementClickable(By.id(LOC.getProperty(locator)))).build()
//					.perform();

			} else if (locator.endsWith("_PLT")) {
				new Actions(driver).moveToElement(driver.findElement(By.partialLinkText(LOC.getProperty(locator))))
						.build().perform();
//			new Actions(driver).moveToElement(fluentWait(By.partialLinkText(LOC.getProperty(locator)))).build()
//					.perform();
//			new Actions(driver).moveToElement(waitUntilElementClickable(By.partialLinkText(LOC.getProperty(locator))))
//					.build().perform();
			}
//			b.test.log(LogStatus.INFO, "Clicking on : " + locator);
		} catch (Exception e) {
			logger.error("CLick Failed and Recorded in Reports..", e);
		}
	}

	public void selectByText(String locator, String selectByVisibleText) {
//		try {
		if (locator.endsWith("_CSS")) {
			dropdown = driver.findElement(By.cssSelector(LOC.getProperty(locator)));
//			fluentWait(By.cssSelector(LOC.getProperty(locator)));
		} else if (locator.endsWith("_XPATH")) {
			dropdown = driver.findElement(By.xpath(LOC.getProperty(locator)));
		} else if (locator.endsWith("_ID")) {
			dropdown = driver.findElement(By.id(LOC.getProperty(locator)));
		} else if (locator.endsWith("_PLT")) {
			dropdown = driver.findElement(By.partialLinkText(LOC.getProperty(locator)));
		}

		Select select = new Select(dropdown);
		select.selectByVisibleText(selectByVisibleText);
//		b.test.log(LogStatus.INFO, "Clicking on : " + locator);

//		} catch (Exception e) {
//			logger.error("Select Failed and Recorded in Reports..", e);
//		}
	}

	public void selectByVaue(String locator, String selectByValue) {
		try {
			if (locator.endsWith("_CSS")) {
				dropdown = driver.findElement(By.cssSelector(LOC.getProperty(locator)));
			} else if (locator.endsWith("_XPATH")) {
				dropdown = driver.findElement(By.xpath(LOC.getProperty(locator)));
			} else if (locator.endsWith("_ID")) {
				dropdown = driver.findElement(By.id(LOC.getProperty(locator)));
			} else if (locator.endsWith("_PLT")) {
				dropdown = driver.findElement(By.partialLinkText(LOC.getProperty(locator)));
			}

			Select select = new Select(dropdown);
			select.selectByValue(selectByValue);
//			b.test.log(LogStatus.INFO, "Clicking on : " + locator);

		} catch (Exception e) {
			logger.error("Select Failed and Recorded in Reports..", e);
//			b.test.log(LogStatus.INFO, "Clicking on : " + locator);
		}
	}

	public void selectByIndex(String locator, int selectByIndex) {
		try {
			if (locator.endsWith("_CSS")) {
				dropdown = driver.findElement(By.cssSelector(LOC.getProperty(locator)));
			} else if (locator.endsWith("_XPATH")) {
				dropdown = driver.findElement(By.xpath(LOC.getProperty(locator)));
			} else if (locator.endsWith("_ID")) {
				dropdown = driver.findElement(By.id(LOC.getProperty(locator)));
			} else if (locator.endsWith("_PLT")) {
				dropdown = driver.findElement(By.partialLinkText(LOC.getProperty(locator)));
			}

			Select select = new Select(dropdown);
			select.selectByIndex(selectByIndex);
//			b.test.log(LogStatus.INFO, "Clicking on : " + locator);

		} catch (Exception e) {
			logger.error("Select Failed and Recorded in Reports..", e);
//			b.test.log(LogStatus.INFO, "Clicking on : " + locator);
		}
	}

	// Interchangeable with click method above [recorded in new Method List]
	public void vehicleCondition(String vehicleCond) {
		WebElement navBarParent = driver.findElement(By.cssSelector(".ddc-pill-nav.line-height-reset"));
		List<WebElement> navigatorTags = navBarParent.findElements(By.tagName("a"));

		for (WebElement aTags : navigatorTags) {
			String attrContents = aTags.getAttribute("data-value");
			// VEHICLE CONDITIONS TO CHOOSE FROM [new, used, certified]
			System.out.println("Vehicle Cond's:[" + attrContents + "]");
			if (attrContents.contains(vehicleCond)) {
				aTags.click();

			}

		}
//		b.test.log(LogStatus.INFO, "Clicking on : " + vehicleCond);
	}

	/***
	 * This scrolls browser view and make the element in the center
	 * 
	 * @param elem
	 */
	public void scrollToWebElement(WebElement elem) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView();", elem);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	public void scrollByOffsetVertical(String verticalPixel) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("scroll(0," + verticalPixel + ")"); // "scroll(0,200)"
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	public void scrollByOffsetHorizontal(String horizontalPixel) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("scroll(" + horizontalPixel + ",0)");
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	/***
	 * This handles check-boxes and buttons.
	 * 
	 * @author Frank M (07/27/2019)
	 * @param isUserWantsToCheckTheBox
	 * @param by
	 */
	public void handleCheckBoxRadioBtn(boolean isUserWantsToCheckTheBox, By by) {
		try {
			WebElement checkboxElem = driver.findElement(by);
			if (isUserWantsToCheckTheBox == true) {
				boolean checkBoxState = checkboxElem.isSelected();
				if (checkBoxState == true) {
					// do nothing
				} else {
					checkboxElem.click();
				}
			} else {
				boolean checkBoxState = checkboxElem.isSelected();
				if (checkBoxState == true) {
					checkboxElem.click();
				} else {
					// do nothing
				}
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}

	}
	public List<String> automaticallyAttachErrorImgToEmail() {
		List<String> fileNames = new ArrayList<>();
		JavaPropertiesManager propertyReader = new JavaPropertiesManager("src/test/resources/DynamicValueFiles/dynamicConfig.properties");
		String tempTimeStamp = propertyReader.readProperty("sessionTime");
		String numberTimeStamp = tempTimeStamp.replaceAll("_", "");
		long testStartTime = Long.parseLong(numberTimeStamp);

		// first check if error-screenshot folder has file
		File file = new File("target/screenshots");
		if (file.isDirectory()) {
			if (file.list().length > 0) {
				File[] screenshotFiles = file.listFiles();
				for (int i = 0; i < screenshotFiles.length; i++) {
					// checking if file is a file, not a folder
					if (screenshotFiles[i].isFile()) {
						String eachFileName = screenshotFiles[i].getName();
						logger.debug("Testing file names: " + eachFileName);
						int indexOf20 = searchSubstringInString("20", eachFileName);
						String timeStampFromScreenshotFile = eachFileName.substring(indexOf20,
								eachFileName.length() - 4);
						logger.debug("Testing file timestamp: " + timeStampFromScreenshotFile);
						String fileNumberStamp = timeStampFromScreenshotFile.replaceAll("_", "");
						long screenshotfileTime = Long.parseLong(fileNumberStamp);

						testStartTime = Long.parseLong(numberTimeStamp.substring(0, 14));
						screenshotfileTime = Long.parseLong(fileNumberStamp.substring(0, 14));
						if (screenshotfileTime > testStartTime) {
							fileNames.add("target/screenshots/" + eachFileName);
						}
					}
				}

			}
		}
		errorScreenshots = fileNames;
		return fileNames;
	}
	public int searchSubstringInString(String target, String message) {
		int targetIndex = 0;
		for (int i = -1; (i = message.indexOf(target, i + 1)) != -1;) {
			targetIndex = i;
			break;
		}
		return targetIndex;
	}

}
