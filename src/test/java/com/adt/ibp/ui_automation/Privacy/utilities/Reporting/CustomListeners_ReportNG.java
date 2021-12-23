package com.adt.ibp.ui_automation.Privacy.utilities.Reporting;

import com.adt.ibp.ui_automation.Privacy.utilities.Base;
import com.adt.ibp.ui_automation.Privacy.utilities.HelperMethods.VALIDATE_ibpConfig;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.testng.*;

import java.io.IOException;

public class CustomListeners_ReportNG extends Base implements ITestListener, ISuiteListener {
	final static org.apache.log4j.Logger logger = Logger.getLogger(CustomListeners_ReportNG.class);

	@Override
	public void onTestStart(ITestResult result) {
		BasicConfigurator.configure();
//		test = reportE.startTest(result.getName().toUpperCase());
//		String env = library.envPrinterForExtentTests("873093643");
		test = reportE.startTest(result.getName().toUpperCase());

//		if(driver.getCurrentUrl().equals(LOC.getProperty("ADMIN_LANDING_PROD2"))){
//			library.customeWait(30);
//			test = reportE.startTest("ENV: " + LOC.getProperty("ADMIN_LANDING_PROD2") + result.getName().toUpperCase());
//		}else if(driver.getCurrentUrl().equals(LOC.getProperty("ADMIN_LANDING_PrePROD2"))){
//			library.customeWait(30);
////			System.out.println("ENV: PreProd -- IAN: " + ian);
//			test = reportE.startTest("ENV: " +LOC.getProperty("ADMIN_LANDING_PrePROD2") + result.getName().toUpperCase());
//		}else if(driver.getCurrentUrl().equals(LOC.getProperty("ADMIN_LANDING_GPC_INT2"))){
//			library.customeWait(30);
////			System.out.println("ENV: GCP_INT -- IAN: " + ian);
//			test = reportE.startTest("ENV: " + LOC.getProperty("ADMIN_LANDING_GPC_INT2")+ result.getName().toUpperCase());
//		}else{
//			logger.info("Can't find the specified environment....");
//
//
//		}
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		/**
		 * Extent Report Setup
		 */
//		test.getStartedTime();

//		test.log(LogStatus.PASS, "FULL TEST STATUS:  ==> " + result.getName().toUpperCase());
//		test.log(LogStatus.PASS, library.envPrinterForExtentTests(ian),result.getName().toUpperCase());
//		test.log(LogStatus.PASS," --- --- --- --- ---  --- --- --- --- ------ --- --- --- ---  --- --- --- --- ------ --- --- --- ---  --- --- --- --- --- --- ------ --- --- --- ---  --- --- --- --- ---  --- --- --- --- --- ");
		reportE.endTest(test);
		reportE.flush();

	}

	@Override
	public void onTestFailure(ITestResult result) {
		// [In order for Screenshot to be taken correct path needs to passed]
		VALIDATE_ibpConfig ibpConfigTestResult = new VALIDATE_ibpConfig();
		System.setProperty("org.uncommons.reportng.escape-output", "false");

//				Reporter.log("This is a log for ReportNG: For Passing Test Cases Customized Message");
		try {
			library.getScreenshotPathRG();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * Extent Report Setup
		 */
//		test.log(LogStatus.FAIL,
//				result.getName().toLowerCase() + "Failed Test Case with Exception : " + result.getThrowable());
		test.log(LogStatus.FAIL, "@FULL TEST STATUS:  ==> "+ result.getThrowable());
		test.log(LogStatus.FAIL, test.addScreenCapture(library.screenshotName));
		

		/**
		 * ReporterNG SETUP
		 */

		Reporter.log("Click to see Screenshot");
		Reporter.log("<a target=\"_blank\" href=" + library.screenshotName + ">Screenshot</a>");
		Reporter.log("<br>");
		Reporter.log("<br>");
		Reporter.log("<a target=\"_blank\" href=" + library.screenshotName + "><img src=" + library.screenshotName
				+ " height=200 width=200></img></a>");
		reportE.endTest(test);
		reportE.flush();

	}

	@Override
	public void onTestSkipped(ITestResult result) {

		test.log(LogStatus.SKIP, "@FULL TEST STATUS: ==> " + result.getName().toUpperCase());
		reportE.endTest(test);
		reportE.flush();
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestFailedWithTimeout(result);
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		ITestListener.super.onStart(context);
	}

	@Override
	public void onFinish(ITestContext context) {

	}

}
