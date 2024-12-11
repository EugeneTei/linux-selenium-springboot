package com.shoalter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component("cross")
public class CrossContainerJob implements CommonJobAction {

    private final RemoteWebDriver remoteWebDriver;

    @Override
    public RemoteWebDriver run(Map<String, String> args) {
        try {
            remoteWebDriver.get("https://www.facebook.com/vamossports1/");
            log.info("Page title: {}", remoteWebDriver.getTitle());

            Thread.sleep(10000);

            String pageSource = remoteWebDriver.getPageSource();

            log.info("pageSource: \n{}", pageSource.substring(0, 1000));

        } catch (InterruptedException e) {
            log.error("InterruptedException: {}", e.getMessage());
        }  finally {
            if (remoteWebDriver != null) {
                remoteWebDriver.quit();
            }
        }

        return null;
    }
}
