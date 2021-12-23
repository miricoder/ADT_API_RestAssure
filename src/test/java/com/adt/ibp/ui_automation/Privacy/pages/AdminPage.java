package com.adt.ibp.ui_automation.Privacy.pages;


import com.adt.ibp.ui_automation.Privacy.utilities.Base;
import org.apache.log4j.BasicConfigurator;
import org.testng.log4testng.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class AdminPage extends Base {
    final static Logger logger = Logger.getLogger(AdminPage.class);
    public static Properties LOC = new Properties();
    public AdminPage logintoAdminportal(String url, String username, String password) throws IOException {
        BasicConfigurator.configure();
        FileInputStream fis = new FileInputStream("src/test/java/com/adt/ibp/ui_automation/Privacy/utilities/locators.properties");
        LOC.load(fis);
        driver.get(url);
//        driver.findElement(By.cssSelector(LOC.getProperty("LEGACY_LOGIN_CSS"))).click();
        library.buttonClick("CHOOSE_LEGACY_LOGIN_OPTION_CSS");
//        driver.findElement(By.cssSelector(LOC.getProperty("ADMIN_USERNAME_CSS"))).sendKeys(username);
        library.type("ENTER_ADMIN_USERNAME_CSS", username);
//        driver.findElement(By.cssSelector(LOC.getProperty("ADMIN_PWD_CSS"))).sendKeys(password);
        library.type("ENTER_ADMIN_PASSWORD_CSS", password);
//        driver.findElement(By.cssSelector(LOC.getProperty("ADMIN_CREDT_SUBMIT_CSS"))).click();
        library.buttonClick("CLICK_LEGACY_SIGNIN_BUTTON_CSS");

//        String currentUrl = driver.getCurrentUrl();




    return this;
    }

    public SubscriberPage goToSubscriberAccount(String IAN) throws IOException {
        BasicConfigurator.configure();
        FileInputStream fis  = new FileInputStream("src/test/java/com/adt/ibp/ui_automation/Privacy/utilities/locators.properties");
        LOC.load(fis);

//        driver.findElement(By.cssSelector(LOC.getProperty("ENTER_IAN_BOX"))).sendKeys(IAN);
//        library.customeWait(3);
        library.type("SEARCH_FOR_SUB_IAN_CSS",IAN);
//        driver.findElement(By.cssSelector(LOC.getProperty("GO_SEARCH_IAN"))).click();
        library.buttonClick("GO_SEARCH_IAN_CSS");
        /*
         * Added on Friday December 17 7PM
         * BasicConfigurator is used to solve log4j appenders not found issue
         */

        return new SubscriberPage();
    }

}
