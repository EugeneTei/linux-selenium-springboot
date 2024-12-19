package com.shoalter.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;
import java.util.Map;

// 在 container 內部呼叫 selenium
@Slf4j
@RequiredArgsConstructor
@Component("internal")
public class InternalContainerJob implements CommonJobAction {

    private final RemoteWebDriver remoteWebDriver;

    private static final String FAN_PAGE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";

    @Override
    public void run(Map<String, String> args) {
        log.info("Crawling: {}", FAN_PAGE_URL);
        try {
            remoteWebDriver.get(FAN_PAGE_URL);
            log.info("Page title is: {}", remoteWebDriver.getTitle());

            String pageSource = remoteWebDriver.getPageSource();
            if(pageSource != null) {
                log.info("pageSource: \n{}", pageSource.substring(0, 1000));
            }
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        } finally {
            remoteWebDriver.quit();
        }
    }
}