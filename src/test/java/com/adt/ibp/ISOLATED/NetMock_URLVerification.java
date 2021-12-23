package com.adt.ibp.ISOLATED;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v96.fetch.Fetch;

import java.util.Optional;

public class NetMock_URLVerification {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");
        ChromeDriver driver = new ChromeDriver();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        /*Enable so you can start listening to the network
        https://chromedevtools.github.io/devtools-protocol/tot/Fetch/#method-enable
         */
        devTools.send(Fetch.enable(Optional.empty(), Optional.empty()));

        /*
        Manipulate the request
        https://chromedevtools.github.io/devtools-protocol/tot/Fetch/#event-requestPaused
         */
        devTools.addListener(Fetch.requestPaused(), request->{
            if (request.getRequest().getUrl().contains("ap-config.json")){
                String mockedUrl=request.getRequest().getUrl().replace("wp-config.json", "wp-config.json");
                System.out.println(mockedUrl);

                devTools.send(
                        Fetch.continueRequest(request.getRequestId(),
                                Optional.of(mockedUrl),
                                Optional.of(request.getRequest().getMethod()),
                                Optional.empty(),
                                Optional.empty(),
                                Optional.empty()));
            }else{
                //If URL matches don't mock anything
                devTools.send(
                        Fetch.continueRequest(request.getRequestId(),
                                Optional.of(request.getRequest().getUrl()),
                                Optional.of(request.getRequest().getMethod()),
                                Optional.empty(),
                                Optional.empty(),
                                Optional.empty()));
            }
        });
        driver.get("https://admin.bluebyadt.com/#/login");
        driver.findElement(By.cssSelector("button[class='btn btn-default']")).click();
////        driver.findElement(By.cssSelector("input[id='username']")).sendKeys("miralimirzayev@adt.com");
////        driver.findElement(By.cssSelector("input[id='password']")).sendKeys("!look@book5");
////        driver.findElement(By.cssSelector(".footer > .btn")).click();
////        driver.findElement(By.cssSelector(".footer > .btn")).click();
////        driver.findElement(By.cssSelector("#searchCriteria")).click();
////        driver.findElement(By.cssSelector("input[name='searchCriteria']")).sendKeys("873093643");
////        driver.findElement(By.cssSelector(".account-search > .btn-primary")).click();
        Thread.sleep(5000);
        driver.quit();

    }
}
