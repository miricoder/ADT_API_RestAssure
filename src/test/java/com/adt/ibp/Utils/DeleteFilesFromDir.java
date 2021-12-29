package com.adt.ibp.Utils;


import com.adt.ibp.ui_automation.Privacy.utilities.Base;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;

import java.io.*;

public class DeleteFilesFromDir extends Base {
    final static Logger logger = Logger.getLogger(DeleteFilesFromDir.class);
//    private static final String FILE_DIR = "src/test/resources/DynamicValueFiles/";
//    private static final String FILE_JSON_EXT = ".json";
    private final String fileDirectory;
//    private String fileExtension;


//    public static void main(String args[]) {
//        new DeleteFilesFromDir().deleteFile(FILE_DIR,FILE_JSON_EXT);
//    }

public DeleteFilesFromDir(String FILE_DIR, String FILE_JSON_EXT){
    this.fileDirectory=FILE_DIR;
    deleteFile(FILE_DIR, FILE_JSON_EXT);
}
    public void deleteFile(String folder, String ext){

        GenericExtFilter filter = new GenericExtFilter(ext);
        File dir = new File(folder);

        //list out all the file name with .txt extension
        String[] list = dir.list(filter);

        if (list.length == 0) return;

        File fileDelete;

        for (String file : list){
            String temp = new StringBuffer(fileDirectory)
                    .append(File.separator)
                    .append(file).toString();
            fileDelete = new File(temp);
            boolean isdeleted = fileDelete.delete();
            System.out.println("file : " + temp + " is deleted : " + isdeleted);
            logger.warn("file : " + temp + " is deleted : " + isdeleted);
            test.log(LogStatus.INFO, "file : " + temp + " is deleted : " + isdeleted);
        }
    }


    //inner class, generic extension filter
    public class GenericExtFilter implements FilenameFilter {

        private String ext;

        public GenericExtFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(File dir, String name) {
            return (name.endsWith(ext));
        }
    }


}
