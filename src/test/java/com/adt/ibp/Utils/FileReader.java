package com.adt.ibp.Utils;

import org.testng.log4testng.Logger;

import java.io.*;

import static org.testng.Assert.assertTrue;

public class FileReader {
	final static Logger logger = Logger.getLogger(FileReader.class);
	private String fileName;

	public FileReader(String jwtTokensPath) {
		fileName = jwtTokensPath;
	}

	/***
	 * To Do
	 * Write a code block which will create the file as well
	 * @return
	 * @param
	 */
	public String readFile() {
		String finalText = null;
		String line = null;
		String newLine = System.lineSeparator();
		try {
			java.io.FileReader fileReader = new java.io.FileReader(fileName);
			BufferedReader bfr = new BufferedReader(fileReader);
			StringBuffer sb = new StringBuffer();
			while ((line = bfr.readLine()) != null) {
				sb.append(line + newLine);
			}
			finalText = sb.toString();
			bfr.close();
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return finalText;
	}
//	public static void main(String[] args) {
//		FileReader readValue = new FileReader("src/test/resources/DynamicValueFiles/JWTTokens.txt");
//		String dataResult = readValue.readFile();
//		logger.info("my data: " + dataResult);
//		System.out.println(dataResult);
//	}
}
