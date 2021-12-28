package com.adt.ibp.ui_automation.Privacy.utilities;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import static org.testng.Assert.assertTrue;

public class TextFileReaderWriter {
	final static Logger logger = Logger.getLogger(TextFileReaderWriter.class);
	
	private String fileName;
	
	public TextFileReaderWriter(String filePath){
		fileName = filePath;
	}
	
	public String readFile(){
		String finalText = null;
		String line = null;
		String newLine = System.lineSeparator();
		try{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bfr = new BufferedReader(fileReader);
			StringBuffer sb = new StringBuffer();
			while((line = bfr.readLine()) != null){
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
	
	public void writeFile(String inputData){
		try{
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fileWriter);
			bw.write(inputData);
			bw.close();
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}
	
	public static void main(String[] args) {
		//TextFileReaderWriter myWriter = new TextFileReaderWriter("C:/Jenkins1/Cars.txt");
		//myWriter.writeFile("I like cars. I want to buy BMW.");
		
		TextFileReaderWriter myReader = new TextFileReaderWriter("C:/Jenkins1/Cars.txt");
		String dataResult = myReader.readFile();
		logger.info("my data: " + dataResult);
	}
	
	
}




























