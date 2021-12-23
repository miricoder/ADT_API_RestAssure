package com.adt.ibp.Utils;

import org.testng.log4testng.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateFileWriteToFile {
    final static org.testng.log4testng.Logger logger = Logger.getLogger(CreateFileWriteToFile.class);
    /**
     * Checks if file is created then it won't recreate & write JWT Token to it to be used in LoginWithJWT Token Step
     * Web Source: https://www.w3schools.com/java/java_files_create.asp
     * @param args
     * "src/test/resources/DynamicValueFiles/"
     */
    public String  jwtTokensPath;
    public static String file;
    public String text;
//
//public DynamicValues (String jwtTokensPath, String fileName, String text){
//    this.jwtTokensPath  =jwtTokensPath;
//    file = new File(fileName);
//    this.text = text;
//    createAfile(jwtTokensPath,fileName);
//    writeToaFile(text,fileName);
//}
    public CreateFileWriteToFile(String path, String file, String line){
//        this.jwtTokensPath = path;
//        this.file = file;
//        this.text=line;
        createAfile(path, file);
        writeToaFile(path,file,line);

    }
    public static void createAfile(String path, String fileName){
        try{
            File myObj = new File( path,fileName);
            if (myObj.createNewFile()){
                System.out.println("File created: " + myObj.getName());
            }else{
                System.out.println("File already exists....");
            }
        }catch (IOException e){
            System.out.println("An error occurred");
            e.printStackTrace();

        }

    }

    public static void writeToaFile(String path, String file, String text){
        try{
            FileWriter myWriter = new FileWriter(path+file);
            myWriter.write(text);
            myWriter.close();
            System.out.println("Successfully wrote to the file....");
        }catch (IOException e){
            System.out.println("An error occurred....");
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//        createAfile("src/test/resources/DynamicValueFiles/", "JWTTokens.txt");
//        writeToaFile("bluh2", "src/test/resources/DynamicValueFiles/"+"JWTTokens.txt");
//    }

}
