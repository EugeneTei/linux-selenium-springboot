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
import java.util.Arrays;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component("cross")
public class CrossContainerJob implements CommonJobAction {

    @Override
    public void run(Map<String, String> args) {

        ChromeOptions options = new ChromeOptions();

        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(
                    new URI("http://localhost:4444/wd/hub").toURL(),
                    options
            );

            driver.get("https://www.facebook.com/vamossports1/");
            log.info("Page title: {}", driver.getTitle());

            Thread.sleep(10000);

            String pageSource = driver.getPageSource();

            log.info("pageSource: \n{}", pageSource.substring(0, 1000));

        } catch (MalformedURLException e) {
            log.error("Invalid URL for Remote WebDriver: {}", e.getMessage());
        } catch (InterruptedException e) {
            log.error("InterruptedException: {}", e.getMessage());
        } catch (URISyntaxException e) {
            log.error("URISyntaxException: {}", e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }

    }
}
