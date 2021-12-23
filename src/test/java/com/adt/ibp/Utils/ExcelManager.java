package com.adt.ibp.Utils;


import jxl.Sheet;
import jxl.Workbook;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelManager {

	final static Logger logger = Logger.getLogger(ExcelManager.class);
	
	private static String file;
	private static HSSFSheet excelSheet;
	private static HSSFWorkbook excelWBook;
	private static HSSFCell cell;
	private static HSSFRow row;
	
	public ExcelManager(String excelFileName){
		BasicConfigurator.configure();
		File myFile = new File(excelFileName);
		file = myFile.getAbsolutePath();
	}
	
	public String[][] getExcelData(String sheetName) {
		BasicConfigurator.configure();
		String[][] arrayExcelData = null;
		try {
			FileInputStream fs = new FileInputStream(file);
			Workbook wb = Workbook.getWorkbook(fs);
			Sheet sh = wb.getSheet(sheetName);

			int totalCols = sh.getColumns();
			int totalRows = sh.getRows();

			arrayExcelData = new String[totalRows - 1][totalCols];
			for (int i = 1; i < totalRows; i++) {
				// reading rows
				for (int j = 0; j < totalCols; j++) {
					// reading columns
					arrayExcelData[i - 1][j] = sh.getCell(j, i).getContents();
				}
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		return arrayExcelData;
	}
	
	public static void setCellData(String inputData, int rowNum, int colNum) {
		BasicConfigurator.configure();
		FileOutputStream fileOut = null;
		try {
			row = excelSheet.createRow(rowNum);
			cell = row.getCell(colNum);
			if (cell == null) {
				cell = row.createCell(colNum);
				cell.setCellValue(inputData);
				fileOut = new FileOutputStream(file);
				excelWBook.write(fileOut);
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
		} finally {
			try {
				fileOut.flush();
				fileOut.close();
			} catch (Exception e) {
				logger.error("Error: ", e);
			}
		}
	}
	
	public static String getCellData(int rowNum, int colNum) {
		BasicConfigurator.configure();
		String cellData = null;
		try {
			cell = excelSheet.getRow(rowNum).getCell(colNum);
			cellData = cell.getStringCellValue();
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		return cellData;
	}

	public static void setExcelFile(String sheetName) {
		BasicConfigurator.configure();
		try {
			//FileOutputStream excelfile = new FileOutputStream(file);			
			excelWBook = new HSSFWorkbook();
			excelSheet = excelWBook.createSheet(sheetName);
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
	}
	
	
	// Create or write excel file
//	public static void main(String[] args) {
//		ExcelManager excelWrite = new ExcelManager("src/test/resources/TestData/UI_TD_PROD.xls");
//		setExcelFile("mySheet1");
//		excelWrite.setCellData("I love Java programming!", 1, 1);
//		logger.info("excel file is created.");
//		logger.info("File location: " + "src/test/resources/TestData/UI_TD_PROD.xls");
//	}

//
	public static void main(String[] args) {
		BasicConfigurator.configure();
		ExcelManager excelRead = new ExcelManager("src/test/resources/TestData/UI_TestData.xls");
		//setExcelFile("Sheet1");
		String[][] cellData = excelRead.getExcelData("privacy_monitor");
		logger.info("data: " + cellData);
	}
	
}



















