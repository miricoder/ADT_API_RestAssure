package com.adt.ibp.ISOLATED;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v96.network.Network;
import org.openqa.selenium.devtools.v96.network.model.Request;
import org.openqa.selenium.devtools.v96.network.model.Response;

import java.util.Optional;

public class NetMock_PLAT1509 {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/java/com/adt/ibp/Utils/ISOLATED/chromedriver");
        ChromeDriver driver = new ChromeDriver();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        /**
         * Listening to network Response
         * https://www.youtube.com/watch?v=h-1bon3dMac
         */
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        /*
        Request URL sent to the server
         */
        devTools.addListener(Network.requestWillBeSent(), request ->
        {
            Request req = request.getRequest();
            System.out.println(req.getUrl());


        });

        /*
        Request URL received by the server
         */
        //Event will get fired when you get response back
        devTools.addListener(Network.responseReceived(), response ->
        {
            Response res = response.getResponse();
            System.out.println(res.getUrl().contains("ap-config.json"));
            System.out.println(res.getStatus());
            System.out.println(res.getHeaders());


        });

        driver.get("https://admin.bluebyadt.com/#/login");
        driver.findElement(By.cssSelector("button[class='btn btn-default']")).click();
        driver.findElement(By.cssSelector("input[id='username']")).sendKeys("miralimirzayev@adt.com");
        driver.findElement(By.cssSelector("input[id='password']")).sendKeys("!look@book5");
        driver.findElement(By.cssSelector(".footer > .btn")).click();
        driver.findElement(By.cssSelector(".footer > .btn")).click();
//        driver.findElement(By.cssSelector("#searchCriteria")).click();
//        driver.findElement(By.cssSelector("input[name='searchCriteria']")).sendKeys("873093643");
//        driver.findElement(By.cssSelector(".account-search > .btn-primary")).click();
        Thread.sleep(5000);
        driver.quit();

    }
}
