package com.shoalter;

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

    @Override
    public RemoteWebDriver run(Map<String, String> args) {
        System.out.println("run https://bonigarcia.dev/selenium-webdriver-java/");
        try {
            remoteWebDriver.get("https://bonigarcia.dev/selenium-webdriver-java/");
            System.out.println("Page title is: " + remoteWebDriver.getTitle());
            remoteWebDriver.quit();
        } catch (Exception e) {
            remoteWebDriver.quit();
            throw e;
        }
        return null;
    }

//        private WebDriver create() {
//            try {
//                ChromeOptions options = new ChromeOptions();
////              options.addArguments("--headless");
//                options.addArguments("--disable-gpu");
//                options.setCapability("goog:loggingPrefs", "ALL");
//                options.addArguments("--verbose");
//                return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
//            } catch (MalformedURLException e) {
//                throw new RuntimeException("Invalid URL: " + e.getMessage(), e);
//            }
//        }
}