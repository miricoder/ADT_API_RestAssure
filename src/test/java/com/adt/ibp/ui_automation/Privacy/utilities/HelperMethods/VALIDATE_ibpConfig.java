package com.adt.ibp.ui_automation.Privacy.utilities.HelperMethods;

import com.adt.ibp.Utils.FileReader;
import com.adt.ibp.ui_automation.Privacy.utilities.Base;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.path.json.JsonPath;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class VALIDATE_ibpConfig {

    private String cameraConfigList;
    private Object conf;
    Base b = new Base();
    ITestResult result;
    long timeStamp;
    JsonPath ibpConfObject;
    String ibpConfigArray;

    @Test(alwaysRun = false)
//    @AfterMethod()
    public void ibpConfig(String ian) {
        /*
        Java and Linux
        https://github.com/tools4j/unix4j/blob/master/unix4j-core/unix4j-command/src/test/java/org/unix4j/builder/CommandBuilderTest.java
         */
        BasicConfigurator.configure();
        /*
        Parsing ibpConfig.json file saved from the WSS Response
         */
        Calendar calendar = Calendar.getInstance();
        String months = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String dates = String.valueOf(calendar.get(Calendar.DATE));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String time = String.valueOf(calendar.get(Calendar.HOUR)+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND));
        String fullDate = ""+year+"_"+months+"_"+dates;
        String jsonFileForValidation=ian+"_ibpConfig_"+fullDate+".json";

        final Logger logger = Logger.getLogger(VALIDATE_ibpConfig.class);
        FileReader ibpConf = new FileReader("src/test/resources/DynamicValueFiles/"+jsonFileForValidation);
        ibpConfObject = new JsonPath(ibpConf.readFile());
        List<String> cameraCount = ibpConfObject.get("ibpConfig.cameraConfigList.id");
        timeStamp = ibpConfObject.get("ibpConfig.timestamp");
        Date date = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String myDate = format.format(date);
        ibpConfigArray = ibpConfObject.getString("ibpConfig");


        if (ibpConfigArray.isEmpty() || !ibpConfigArray.contains("cameraConfigList")) {
            logger.warn("WARNING -- [" + date + "]" + "--" + "ibpConfig is empty / check WSS response when signing into Admin Portal.......");
            b.test.log(LogStatus.WARNING, "@ibpConfigArray ==> " + "WARNING -- [" + date + "]" + "--" + "ibpConfig is empty / check WSS response when signing into Admin Portal.......");
        } else {
            for (int i = 0; i <= cameraCount.size() - 1; i++) {
                String enabledFieldPerCameraId = ibpConfObject.getString("ibpConfig.cameraConfigList[" + i + "].enabled");
                String cameraName = ibpConfObject.getString("ibpConfig.cameraConfigList[" + i + "].name"); //homeId
                String homeId = ibpConfObject.getString("ibpConfig.alertConfigList.homeId[0]");
                if (enabledFieldPerCameraId.matches("false")) {
                    logger.error("FAILED -- [" + date + "]" + "--" +"[IAN-"+homeId+"]"+"(" + cameraName + ")" + "cameraId: " + i + " --> " + enabledFieldPerCameraId);
                    b.test.log(LogStatus.FAIL, "[IAN-"+homeId+"]"+"@cameraConfigList/enable Field ==> " + "FAILED -- [" + date + "]" + "--" + "(" + cameraName + ")" + "cameraId: " + i + " --- " + enabledFieldPerCameraId);
                    //Use Below Sysout for DEBUG matters
                    //                System.err.println("["+date+"]" + "--" +"cameraId: " + i + " --> " + enabledFieldPerCameraId + "("+cameraName+")");
                    System.out.println("================================================================================================================================================================");

                    //                b.test.log(LogStatus.FAIL, "FAILED -- ["+date+"]" + "--" +"cameraId: " + i + " --> " + enabledFieldPerCameraId + "("+cameraName+")");
                    //                b.reportE.setTestRunnerOutput("FAILED -- ["+date+"]" + "--" +"cameraId: " + i + " --> " + enabledFieldPerCameraId + "("+cameraName+")");
                    //                CustomListeners_ReportNG rep = new CustomListeners_ReportNG();
                    //                rep.onTestFailure("FAILED -- ["+date+"]" + "--" +"cameraId: " + i + " --> " + enabledFieldPerCameraId + "("+cameraName+")");
//                    DeleteFilesFromDir remove_ibpConfig = new DeleteFilesFromDir("src/test/resources/DynamicValueFiles/ibpConfig.json");
                } else {
                    //            System.out.println("["+date+"]" + "--" +"cameraId: " + i + " --> " + enabledFieldPerCameraId + "("+cameraName+")");

                    b.test.log(LogStatus.PASS, "[IAN-"+homeId+"]"+"@cameraConfigList/enable Field ==> " + "PASSED -- [" + date + "]" + "--" + "(" + cameraName + ")" + "cameraId: " + i + " --- " + enabledFieldPerCameraId);
                    logger.debug("PASSED -- [" + date + "]" + "--" + "[IAN-"+homeId+"]"+"(" + cameraName + ")" + "cameraId: " + i + " --> " + enabledFieldPerCameraId);
                    System.out.println("================================================================================================================================================================");
//                    DeleteFilesFromDir remove_ibpConfig = new DeleteFilesFromDir("src/test/resources/DynamicValueFiles/ibpConfig.json");

                }
            }
        }
    }
@Test(alwaysRun = false)
//@AfterMethod()
    public void camConfig_camStatus(String ian) {
        final Logger logger = Logger.getLogger(VALIDATE_ibpConfig.class);
        BasicConfigurator.configure();
        Calendar calendar = Calendar.getInstance();
       String months = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String dates = String.valueOf(calendar.get(Calendar.DATE));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String time = String.valueOf(calendar.get(Calendar.HOUR)+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND));
         String fullDate = ""+year+"_"+months+"_"+dates;
        String jsonFileForValidation=ian+"_cameraStatusList_"+fullDate+".json";
                 /*
        Parsing cameraStatusList.json file saved from the WSS Response
         */
        FileReader cameraStatus = new FileReader("src/test/resources/DynamicValueFiles/"+jsonFileForValidation);
        JsonPath cameraStatusObject = new JsonPath(cameraStatus.readFile());
        List<String> cameraStatusCamCount = cameraStatusObject.get("cameraStatusList");
        String cameraStatusArray = cameraStatusObject.getString("cameraStatusList.");
//        String id = cameraStatusObject.getString("cameraStatusList.id");
        List<String> cameraSatusListCamCount = ibpConfObject.get("ibpConfig.cameraConfigList.id");
        Date date = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String myDate = format.format(date);
        String homeId = ibpConfObject.getString("ibpConfig.alertConfigList.homeId[0]");

        if (ibpConfigArray.contains("cameraConfigList") && cameraStatusArray.isEmpty() ||cameraStatusArray.contains("[]") ) {
            logger.warn("WARNING -- [" + date + "]" + "--" + "[IAN-"+homeId+"]"+ "ibpConfig contains cameraConfigList and cameraStatusList is not empty.....");
            b.test.log(LogStatus.WARNING, "[IAN-"+homeId+"]"+"@cameraConfig/List           ==>  " + "WARNING -- [" + date + "]--"+" ibpConfig contains cameraConfigList but cameraStatusList is empty or malformed.....");
//            DeleteFilesFromDir remove_cameraStatusList = new DeleteFilesFromDir("src/test/resources/DynamicValueFiles/cameraStatusList.json");
//            String cameraConfigList = ibpConfObject.get("ibpConfig.cameraConfigList");
//            String buildJson = "-----IAN: " + homeId + date + "(taken from ibpConfig -> Epoch TimeStamp from Json Object and converted)" +
//                    cameraConfigList;
//            CreateFileWriteToFile verificationJsonObject = new CreateFileWriteToFile("src/test/resources/DynamicValueFiles/", "RES_"+fullDate+".json", cameraConfigList);
        }else{
            logger.debug("PASSED -- [" + date + "]" + "--" + "[IAN-"+homeId+"]"+"ibpConfig contains cameraConfigList and cameraStatusList is not empty.....");
            b.test.log(LogStatus.PASS, "[IAN-"+homeId+"]"+"@cameraConfig/List              ==>  " + "PASSED -- [" + date + "]--"+" ibpConfig contains cameraConfigList and cameraStatusList is not empty or malformed.....");
//            String buildJson = "-----IAN: " + homeId + date + "(taken from ibpConfig -> Epoch TimeStamp from Json Object and converted)" +
//                    cameraConfigList;
//            System.out.println("DEBUG ::::::: " + buildJson + " :::::::::::");
//            Object ibConfig = ibpConfObject.get("ibpConfig.timestamp");
//            CreateFileWriteToFile verificationJsonObject = new CreateFileWriteToFile("src/test/resources/DynamicValueFiles/", "RES_"+fullDate+".json", timeStamp);
        }

        /**
         * Print cameraSttausList into the report (perhaps a table)
         */
//            for (int c = 0; c <= cameraSatusListCamCount.size() - 1; c++){
//               String id = cameraStatusObject.getString("cameraStatusList."+c);
//                String model = cameraStatusObject.getString("cameraStatusList."+c+".model");
//                String state = cameraStatusObject.getString("cameraStatusList."+c+".state");
//                String macAdd = cameraStatusObject.getString("cameraStatusList."+c+".macAddress");
//                 String wakeServer = cameraStatusObject.getString("cameraStatusList."+c+".wakeServer");
//                String bridgeId = cameraStatusObject.getString("cameraStatusList."+c+".bridgeId");
//                b.test.log(LogStatus.PASS, "@cameraStatusList/ verify cameraStatusList has not returned empty ==> " + "PASSED -- [" + date + "]" + "--" + "(" + "id: "+c + "model: "+model+"macAddress: "+macAdd+"wakeServer: "+wakeServer+"bridgeId: "+bridgeId+"state: " + state + ")");
//               }
    }
    @Test(alwaysRun = false)
    public void readIanLists() throws IOException {
        Calendar calendar = Calendar.getInstance();
        String months = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String dates = String.valueOf(calendar.get(Calendar.DATE));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String fullDate = "_"+year+"_"+months+"_"+dates;
//        TextFileReaderWriter readFiles = new TextFileReaderWriter("src/test/resources/DynamicValueFiles/ListOfIans.txt");
//        String listOfIANS = readFiles.readFile();
//        System.out.println(listOfIANS);

//        StringBuffer sb = new StringBuffer();
//        sb.charAt()
        String file = "src/test/resources/DynamicValueFiles/ListOfIans.txt";
        LineIterator it = FileUtils.lineIterator(new File(file), "UTF-8");
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                // do something with line
                System.out.println(line+"_cameraStatusList"+fullDate+".json");
                System.out.println(line+"_ibpConfig"+fullDate+".json");


            }
        } finally {
            it.close();
        }
    }
}



