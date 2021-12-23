package com.adt.ibp.ui_automation.Privacy.utilities;


import com.adt.ibp.ui_automation.Privacy.utilities.Reporting.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.log4testng.Logger;

import java.io.IOException;
import java.util.Properties;

//chrome: grid location/node.json file: \tmp\webdriver\chromedriver\chromedriver_81.0_32bit.exe
public class Base {
	final static Logger logger = Logger.getLogger(Base.class);
	public static WebDriver driver;
	public static SeleniumGlobalLibraries library;
	public String browser;
	public String myRemote;
	public String paramBuild;
	public String myExtentReport;
	public boolean isCaptureScreenshot = true;
	public static Properties LOC = new Properties();
	protected RemoteWebDriver remoteDriver;

	// [Extent Report_v2]
	public ExtentReports reportE = ExtentManager.getInstance();
	public static ExtentTest test;
	// [Excel]
//	public static ExcelReader excel;
	
	

	@BeforeClass
	public void beforeAllTest() throws IOException {
		
		JavaPropertiesManager jpm = new JavaPropertiesManager(
				"src/test/resources/Configuration/test_conf_prop.properties");
//		excel = new ExcelReader("src/test/resources/com/MC/excel/testdata/testdata.xlsx");

//		ConfigurationReader confReader = new ConfigurationReader();

		// >>>[Code Level] test Execution
		browser = jpm.readProperty("browserV");
//		browser = confReader.getProperty("src/test/resources/Configuration/test_conf_prop.properties","browserV");
		myRemote = jpm.readProperty("isRemote");
//		myRemote = confReader.getProperty("src/test/resources/Configuration/test_conf_prop.properties","isRemote");
		paramBuild = jpm.readProperty("paramBuild");
//		paramBuild = confReader.getProperty("src/test/resources/Configuration/test_conf_prop.properties","paramBuild");
		library = new SeleniumGlobalLibraries(driver);


	}

	@AfterClass
	public void afterAllTest() {
		if (driver != null) {
			driver.quit();
		}
	}

//	@Parameters("grid3Browser")
	@BeforeMethod()
	public void beforeEachTest() throws IOException {//@Optional String grid3Browser
		// [INFO] Selenium GRID 3 Usage (Not The Grid Extra USE)
		if (paramBuild.contains("true") & myRemote.contains("gridX")) {
			driver = library.startRemoteBrowser("http://192.168.1.179:7755/wd/hub", browser);
//		} else if (paramBuild.contains("true") & myRemote.contains("grid3")) {
//			driver = library.gridBrowsers("http://127.0.0.1:4444/wd/hub", grid3Browser);
		} else if (paramBuild.contains("true") & myRemote.contains("false")) {
			driver = library.mvnParamterizedBrowserCommand();
		} else {
			driver = library.startLocalBrowsers(browser);
		}
	}

	@AfterMethod()
	public void afterEachTest(ITestResult result) throws IOException {// ITestResult result
		driver.close();

		
	}
}
