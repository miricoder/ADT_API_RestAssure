package com.adt.ibp.Utils;


import org.apache.log4j.Logger;

import java.io.File;

public class DeleteFilesFromDir {
    final static Logger logger = Logger.getLogger(DeleteFilesFromDir.class);
    String filePath;
    public DeleteFilesFromDir(String filePath){
        this.filePath=filePath;
        File cameraStatusObj = new File(filePath); //"src/test/resources/DynamicValueFiles/cameraStatusList.json"

        if (cameraStatusObj.delete()) {
            System.out.println("Deleted the file: " + cameraStatusObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }
}
