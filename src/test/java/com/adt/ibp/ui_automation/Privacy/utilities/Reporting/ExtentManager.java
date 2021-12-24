package com.adt.ibp.ui_automation.Privacy.utilities.Reporting;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

import java.io.File;
import java.util.Calendar;

public class ExtentManager {

	private static ExtentReports extent;

	public static ExtentReports getInstance() {
		if (extent == null) {
			/*
			how to manage extent report.html files
			https://www.extentreports.com/docs/versions/2/java/index.html#append-to-report
			 */
			// Instantiate a Date object
//			Date date = new Date();
//			String reformatedDate=date.toString();
			/***
			Report Naming:
			 year_month_date (String fullDate = ""+year+"_"+months+"_"+dates;)
			 */
			Calendar calendar = Calendar.getInstance();
			String months = String.valueOf(calendar.get(Calendar.MONTH));
			String dates = String.valueOf(calendar.get(Calendar.DATE));
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			String fullDate = ""+year+"_"+months+"_"+dates;






			//creates the new file to be saved
			extent = new ExtentReports(System.getProperty("user.dir") + "/src/test/resources/reports/"+fullDate+".html",
//					extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/html/"+fuq!:
//					llDate+".html",
					false, DisplayOrder.OLDEST_FIRST);
			extent.loadConfig(new File(
					System.getProperty("user.dir") + "/src/test/java/com/adt/ibp/ui_automation/Privacy/utilities/Reporting/ReportsConfig.xml"));
		}
		return extent;
	}
}
