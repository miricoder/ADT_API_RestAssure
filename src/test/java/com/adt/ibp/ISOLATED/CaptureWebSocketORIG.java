package com.adt.ibp.ISOLATED;

import com.adt.ibp.Utils.CreateFileWriteToFile;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.log4testng.Logger;

import java.util.logging.Level;

public class CaptureWebSocketORIG {
    final static Logger logger = Logger.getLogger(CaptureWebSocketORIG.class);
    /**
     * https://chromedevtools.github.io/devtools-protocol/tot/Network/#type-WebSocketResponse
     */
    public static void main(String[] args) throws InterruptedException, JSONException {
        BasicConfigurator.configure();
        LoggingPreferences loggingPreferences = new LoggingPreferences();
        loggingPreferences.enable(LogType.PERFORMANCE, Level.ALL);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability( "goog:loggingPrefs", loggingPreferences );

        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");

        /***
         * TO DO
         * Add extent report
         * Log: when test starts
         * Log: if ian is found if not log failure
         * IANs: Can be driven via excel and dataProvider or in Cucumber fashion
         */

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get("https://admin.bluebyadt.com/#/login");
        driver.findElement(By.cssSelector("button[class='btn btn-default']")).click();
        driver.findElement(By.cssSelector("input[id='username']")).sendKeys("miralimirzayev@adt.com");
        driver.findElement(By.cssSelector("input[id='password']")).sendKeys("!look@book5");
        driver.findElement(By.cssSelector(".footer > .btn")).click();
        Thread.sleep(5000);
//        driver.findElement(By.cssSelector("input[id='searchCriteria']")).click();
        driver.findElement(By.cssSelector("input[name='searchCriteria']")).sendKeys("873093643");
        driver.findElement(By.cssSelector(".account-search > .btn-primary")).click();
        Thread.sleep(5000);
        LogEntries logEntries = driver.manage().logs().get(LogType.PERFORMANCE);
        driver.close();
        driver.quit();

        logEntries.forEach(entry->{
            String wssLoginRes;
            try{
                JSONObject messageJSON = null;

                messageJSON = new JSONObject(entry.getMessage());
                /*
                 * Basic Inforation Extraction from JSON Response Body
                 */
                /***
                 * TO DO
                 * Log: if wss response returns zero
                 */

                String method = messageJSON.getJSONObject("message").getString("method");
                if(method.equalsIgnoreCase("Network.webSocketFrameSent")){
                    System.out.println("Message Sent: " + messageJSON.getJSONObject("message").getJSONObject("params").getJSONObject("response").getString("payloadData"));
                }else if(method.equalsIgnoreCase("Network.webSocketFrameReceived")){
                    System.out.println("Message Received: " + messageJSON.getJSONObject("message").getJSONObject("params").getJSONObject("response").getString("payloadData"));
                    wssLoginRes=messageJSON.getJSONObject("message").getJSONObject("params").getJSONObject("response").getString("payloadData");

                    try{
                        if (wssLoginRes.contains("cameraStatusList")){
                            CreateFileWriteToFile ibpConfig = new CreateFileWriteToFile("src/test/resources/DynamicValueFiles/","cameraStatusList.json",wssLoginRes);
                        }
                        if (wssLoginRes.contains("ibpConfig")){
                            /**
                             * cameraConfigList and ibpConfig are returned within the same object and stored in the same ibpConfig.json file
                             */
                            CreateFileWriteToFile ibpConfig = new CreateFileWriteToFile("src/test/resources/DynamicValueFiles/","ibpConfig.json",wssLoginRes);
                        }
                    }catch (Exception e){
                        logger.error("Can't find the Json Objects: ");
                        e.printStackTrace();

                    }
                }
            } catch (JSONException e) {
                logger.error("No Websocket Response FOUND: ");
                e.printStackTrace();
            }


        });



    }
}


