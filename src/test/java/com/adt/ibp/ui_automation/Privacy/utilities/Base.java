package com.adt.ibp.ui_automation.Privacy.utilities;


import com.adt.ibp.Utils.DeleteFilesFromDir;
import com.adt.ibp.Utils.EmailManager;
import com.adt.ibp.ui_automation.Privacy.utilities.Reporting.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.log4testng.Logger;

import java.io.File;
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
	public void afterSuite() throws IOException {
		Calendar calendar = Calendar.getInstance();
		String months = String.valueOf(calendar.get(Calendar.MONTH)+1);
		String dates = String.valueOf(calendar.get(Calendar.DATE));
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String fullDate = year+"_"+months+"_"+dates;
		EmailManager sender = new EmailManager();
//		sender.toAddress = "musabaytechcorp@gmail.com;training@musabaytechnologies.com";
		sender.toAddress = "qa.group.notes@gmail.com;";
		sender.ccAddress = "qa.group.notes@gmail.com;";

		List<String> screenshots = new ArrayList<>();

		screenshots.add(System.getProperty("user.dir") + "/src/test/resources/reports/"+fullDate+".html");


//		TextFileReaderWriter readFiles = new TextFileReaderWriter("src/test/resources/DynamicValueFiles/+ListOfIans.txt");
//		String listOfIANS=readFiles.readFile();
//
//		for(int i=0; i<=listOfIANS.length(); i++){
//
//			screenshots.add(System.getProperty("user.dir") + "/src/test/resources/DynamicValueFiles/"+i+"_cameraStatusList_"+fullDate+".json");
//			screenshots.add(System.getProperty("user.dir") + "/src/test/resources/DynamicValueFiles/"+i+"_ibpConfig_"+fullDate+".json");
//			sender.sendEmail(screenshots);
//		}
		String file = "src/test/resources/DynamicValueFiles/ListOfIans.txt";
		LineIterator it = FileUtils.lineIterator(new File(file), "UTF-8");
		String line=null;
		try {
			while (it.hasNext()) {
				 line = it.nextLine();
				// do something with line
//				System.out.println(line+"_cameraStatusList"+fullDate+".json");
//				System.out.println(line+"_ibpConfig"+fullDate+".json");
				screenshots.add(System.getProperty("user.dir") + "/src/test/resources/DynamicValueFiles/"+line+"_cameraStatusList"+"_"+fullDate+".json");
				screenshots.add(System.getProperty("user.dir") + "/src/test/resources/DynamicValueFiles/"+line+"_ibpConfig"+"_"+fullDate+".json");
				sender.sendEmail(screenshots);
			}
			DeleteFilesFromDir deleteCameraStatusListJsonFile = new DeleteFilesFromDir("/src/test/resources/DynamicValueFiles/"+line+"_cameraStatusList"+"_"+fullDate+".json");
			DeleteFilesFromDir deleteIbpConfigJsonFiles = new DeleteFilesFromDir("/src/test/resources/DynamicValueFiles/"+line+"_ibpConfig"+"_"+fullDate+".json");

		} finally {
			it.close();
		}

	}

}
