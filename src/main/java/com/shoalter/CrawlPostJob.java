package com.shoalter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class CrawlPostJob implements CommonJobAction {

    @Autowired
    private WebDriver remoteWebDriver;

    @Override
    public void run(Map<String, String> args) {
        System.out.println("run https://bonigarcia.dev/selenium-webdriver-java/");
        try{

            remoteWebDriver.get("https://bonigarcia.dev/selenium-webdriver-java/");
            System.out.println("Page title is: " + remoteWebDriver.getTitle());
            remoteWebDriver.quit();
        }
        catch (Exception e){
            remoteWebDriver.quit();
            throw e;
        }
    }
}