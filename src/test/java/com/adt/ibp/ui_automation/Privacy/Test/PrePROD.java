package com.adt.ibp.ui_automation.Privacy.Test;

import com.adt.ibp.Utils.ExcelManager;
import com.adt.ibp.ui_automation.Privacy.pages.AdminPage;
import com.adt.ibp.ui_automation.Privacy.pages.SubscriberPage;
import com.adt.ibp.ui_automation.Privacy.utilities.Base;
import com.adt.ibp.ui_automation.Privacy.utilities.HelperMethods.VALIDATE_ibpConfig;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;


public class PrePROD extends Base {
    final static Logger logger = Logger.getLogger(PrePROD.class);
    AdminPage adminPage = new AdminPage();
    SubscriberPage goToSubscriberPage = new SubscriberPage();

    @DataProvider(name = "UI_AUTOMATION_TEST_DATA_PreProd")
    private Object[][] UI_AUTOMATION_TEST_DATA() {
        ExcelManager excelReader = new ExcelManager("src/test/resources/TestData/UI_TD_PRIVACY.xls");
        Object[][] data;
        data = excelReader.getExcelData("preprod");
        return data;
    }

    @Test(enabled = true,dataProvider = "UI_AUTOMATION_TEST_DATA_PreProd")
    public void PREProd_Privacy_Test(String env, String expected, String username, String password, String ian) throws IOException, InterruptedException {
        BasicConfigurator.configure();
/*
 *GIVEN: User Signs into Admin Page
 */
//        adminPage.logintoAdminportal("https://admin.bluebyadt.com/#/login", "miralimirzayev@adt.com","!look@book5");
        adminPage.logintoAdminportal(env, username,password);
        if (env.contains("https://admin.bluebyadt.com/#/login")){
                library.checkCurrentPageLanding(expected,"ADMIN_LANDING_PROD");//https://admin.bluebyadt.com/#/global"
        }else if(env.contains("https://admin.preprod.bluebyadt.com/#/login")){
                library.checkCurrentPageLanding(expected,"ADMIN_LANDING_PrePROD");//https://admin.bluebyadt.com/#/global
        }else{
                library.checkCurrentPageLanding(expected,"ADMIN_LANDING_GPC_INT");//https://admin.int.bluebyadt.com/#/global
        }



/*
 *WHEN: Searches and finds Subscriber account
 */
//        goToSubscriberPage=adminPage.goToSubscriberAccount("873093643");
        goToSubscriberPage=adminPage.goToSubscriberAccount(ian);

/*
 *THEN: Captures WSS response
 */
        goToSubscriberPage.captureWebSocketResponse();

        /**
         *THEN: Verify below points
         * [1] ibpConfig arrary is not empty
         * [2] ibpConfig contains and cameraConfig list and cameraStatusList is also returned as part of general WSS Response
         * [3] Verify enable field is not false if it is logged it as a failure in extent report and continue evaluating other fields
         * [4] If the Admin Portal Sign in Fails remove all exisiting ibpConfig and cameraSTatusList.json files to avoid false positive tests
         */
        VALIDATE_ibpConfig ibpConfigValidation = new VALIDATE_ibpConfig();
        ibpConfigValidation.ibpConfig();
        ibpConfigValidation.camConfig_camStatus();

    }

//    @Test(invocationCount = 1, dependsOnMethods = "Sign_In_Get_WSS_Response")//dependsOnMethods = "Sign_In_Get_WSS_Response"
//    @AfterMethod()
////    @Test(dependsOnMethods = "Sign_In_Get_WSS_Response")
//    public void ibpConfig_VALIDATE(){
//        VALIDATE_ibpConfig ibpConfigValidation = new VALIDATE_ibpConfig();
//        ibpConfigValidation.ibpConfig();
//        ibpConfigValidation.camConfig_camStatus();
//
//
//    }

}
