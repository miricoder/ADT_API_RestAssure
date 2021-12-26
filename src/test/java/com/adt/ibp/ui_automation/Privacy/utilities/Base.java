package com.adt.ibp.ui_automation.Privacy.utilities;


import com.adt.ibp.Utils.EmailManager;
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
import java.util.*;

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
	public String isAutoSendEmail;

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
		isAutoSendEmail = jpm.readProperty("autoEmail");


	}

	@AfterClass
	public void afterAllTest() {
		if (driver != null) {
			driver.quit();
		}



		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 13);
		c.set(Calendar.MINUTE, 05);
		c.set(Calendar.SECOND, 00);

		String months = String.valueOf(c.get(Calendar.MONTH));
		String dates = String.valueOf(c.get(Calendar.DATE));
		String year = String.valueOf(c.get(Calendar.YEAR));
		String fullDate = ""+year+"_"+months+"_"+dates;

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				//Call your method here
				// Sending all the reports, screenshots, and log files via email
				List<String> screenshots = new ArrayList<>();
				EmailManager emailSender = new EmailManager();
//				emailSender.attachmentFiles.add("target/logs/log4j-selenium.log");
				emailSender.attachmentFiles.add("ADT_API_RestAssure/src/test/resources/reports/"+fullDate+".html");
				screenshots = library.automaticallyAttachErrorImgToEmail();
				if(screenshots.size() != 0){
					for(String attachFile : screenshots){
						emailSender.attachmentFiles.add(attachFile);
					}
				}

				emailSender.toAddress = "qa.group.notes@gmail.com";
				emailSender.ccAddress = "mirzayev.mirali19@gmail.com";

				if(isAutoSendEmail.contains("true")){
					emailSender.sendEmail(emailSender.attachmentFiles);
				}
			}
		}, c.getTime(), 86400000);
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
