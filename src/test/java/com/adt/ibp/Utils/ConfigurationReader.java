package com.adt.ibp.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {
    private static Properties properties;

    static{

    }
public static String getProperty(String fileName,  String key){
    try{
        String path = "src/test/resources/Configuration/" + fileName;
        FileInputStream stream = new FileInputStream(path);
        properties = new Properties();
        properties.load(stream);
        stream.close();
    }catch (IOException e){
        e.printStackTrace();
    }
    return properties.getProperty(key);
}

}
