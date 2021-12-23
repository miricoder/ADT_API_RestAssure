package com.adt.ibp.ui_automation.Privacy.pages;

import com.adt.ibp.Utils.CreateFileWriteToFile;
import com.adt.ibp.ui_automation.Privacy.utilities.Base;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;

import java.io.IOException;
import java.util.Properties;


public class SubscriberPage extends Base {

    final static Logger logger = Logger.getLogger(SubscriberPage.class);
    public static Properties LOC = new Properties();

    public SubscriberPage goToSubscriberAccount() throws IOException {
        BasicConfigurator.configure();
        /*Get WSS Response and evaluate */
        captureWebSocketResponse();
        return this;
    }

    public void captureWebSocketResponse() {

        library.customeWait(5);
        LogEntries logEntries = driver.manage().logs().get(LogType.PERFORMANCE);
        logEntries.forEach(entry -> {
            String wssLoginRes;
            try {
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
                if (method.equalsIgnoreCase("Network.webSocketFrameSent")) {
                    System.out.println("Message Sent: " + messageJSON.getJSONObject("message").getJSONObject("params").getJSONObject("response").getString("payloadData"));
                } else if (method.equalsIgnoreCase("Network.webSocketFrameReceived")) {
                    System.out.println("Message Received: " + messageJSON.getJSONObject("message").getJSONObject("params").getJSONObject("response").getString("payloadData"));
                    wssLoginRes = messageJSON.getJSONObject("message").getJSONObject("params").getJSONObject("response").getString("payloadData");

                    try {
                        if (wssLoginRes.contains("cameraStatusList")) {
                            CreateFileWriteToFile ibpConfig = new CreateFileWriteToFile("src/test/resources/DynamicValueFiles/", "cameraStatusList.json", wssLoginRes);
                        }
                        if (wssLoginRes.contains("ibpConfig")) {
                            /**
                             * cameraConfigList and ibpConfig are returned within the same object and stored in the same ibpConfig.json file
                             */
                            CreateFileWriteToFile ibpConfig = new CreateFileWriteToFile("src/test/resources/DynamicValueFiles/", "ibpConfig.json", wssLoginRes);
                        }
                    } catch (Exception e) {
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
