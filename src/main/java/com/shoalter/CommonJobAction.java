package com.shoalter;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Map;

public interface CommonJobAction {
    RemoteWebDriver run(Map<String, String> args) throws Exception;
}
