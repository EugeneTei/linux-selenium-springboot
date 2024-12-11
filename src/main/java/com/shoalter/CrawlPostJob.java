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
    private DriverConfig driverConfig;

    @Override
    public void run(Map<String, String> args) {
        WebDriver driver = create();
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        System.out.println("Page title is: " + driver.getTitle());
        driver.quit();
    }

    private WebDriver create() {
        try {
            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL: " + e.getMessage(), e);
        }
    }
}