package com.adt.ibp.ui_automation.Privacy.utilities;


import com.adt.ibp.Utils.EmailManager;
import com.adt.ibp.ui_automation.Privacy.utilities.Reporting.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.log4testng.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
		logger.info("After all test completed...");
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
	@AfterSuite
	public void afterSuite(){
		Calendar calendar = Calendar.getInstance();
		String months = String.valueOf(calendar.get(Calendar.MONTH)+1);
		String dates = String.valueOf(calendar.get(Calendar.DATE));
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String fullDate = ""+year+"_"+months+"_"+dates;
		EmailManager sender = new EmailManager();
//		sender.toAddress = "musabaytechcorp@gmail.com;training@musabaytechnologies.com";
		sender.toAddress = "qa.group.notes@gmail.com;";
		sender.ccAddress = "qa.group.notes@gmail.com;";

		List<String> screenshots = new ArrayList<>();
//		screenshots.add("target/logs/log4j-selenium.log");
//		screenshots.add("target/logs/Selenium-Report.html");
//		screenshots.add("target/screenshots/buy_TheAgingBrainCoursTest20190824100902222.png");
//		screenshots.add("target/screenshots/buy_TheAgingBrainCoursTest20190824101303778.png");
		screenshots.add(System.getProperty("user.dir") + "/src/test/resources/reports/"+fullDate+".html");
		sender.sendEmail(screenshots);
//		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Maryland"));
//		ZonedDateTime nextRun = now.withHour(20).withMinute(05).withSecond(0);
//		if(now.compareTo(nextRun) > 0)
//			nextRun = nextRun.plusDays(1);
//
//		Duration duration = Duration.between(now, nextRun);
//		long initalDelay = duration.getSeconds();
//
//		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//		scheduler.scheduleAtFixedRate(new Runnable() {
//										  @Override
//										  public void run() {
//
//
//											  sender.sendEmail(screenshots);
//										  }
//									  },
//				initalDelay,
//				TimeUnit.DAYS.toSeconds(1),
//				TimeUnit.SECONDS);
	}

}
