package com.adt.ibp.ISOLATED;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v96.log.Log;
import org.openqa.selenium.devtools.v96.network.Network;

import java.util.Optional;

public class NetMock_AllUrlsInXHR {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/java/com/adt/ibp/Utils/ISOLATED/chromedriver");
        ChromeDriver driver = new ChromeDriver();
        Thread.sleep(5000);
        DevTools devTools = driver.getDevTools();
        devTools.createSession();

        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(),
                entry -> {
                    System.out.println("Request URI : " + entry.getRequest().getUrl()+"\n"
                            + " With method : "+entry.getRequest().getMethod() + "\n");
                    entry.getRequest().getMethod();
                });

        devTools.send(Log.enable());
        devTools.addListener(Log.entryAdded(),
                logEntry -> {
                    System.out.println("log: "+logEntry.getText());
                    System.out.println("level: "+logEntry.getLevel());
                });


//        driver.get("https://www.google.com");
        driver.get("https://admin.bluebyadt.com/#/login");
        devTools.send(Network.disable());

        driver.get("https://admin.bluebyadt.com");
//        driver.findElement(By.cssSelector("button[class='btn btn-default']")).click();
//        driver.findElement(By.cssSelector("input[id='username']")).sendKeys("miralimirzayev@adt.com");
//        driver.findElement(By.cssSelector("input[id='password']")).sendKeys("!look@book5");
//        driver.findElement(By.cssSelector(".footer > .btn")).click();
//        driver.findElement(By.cssSelector(".footer > .btn")).click();
//        driver.findElement(By.cssSelector("#searchCriteria")).click();
//        driver.findElement(By.cssSelector("input[name='searchCriteria']")).sendKeys("873093643");
//        driver.findElement(By.cssSelector(".account-search > .btn-primary")).click();
// Check the terminal output for the browser console messages.
        driver.quit();
    }
}
