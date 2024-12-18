package com.shoalter.apache5;


import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.HttpHost;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

// Work when request http url
@Slf4j
public class ProxyExample {

//    public static final String URL = "http://www.example.org";
//    public static  final String URL = "http://neverssl.com";
    public static  final String URL = "https://www.example.com/api";

//    public static  final String URL = "https://www.facebook.com/api/graphql/";
//    public static final String URL = "https://www.google.com.tw/?hl=zh_TW";
    public static HttpHost proxyHost = new HttpHost("http", "148.66.6.214", 80);

    public static void main(String[] args) {

        try {
            CloseableHttpAsyncClient client = HttpAsyncClients.custom()
                    .setProxy(proxyHost)
                    .build();
            client.start();

            SimpleHttpRequest request = SimpleRequestBuilder.get(new URI(URL)).build();
            SimpleHttpResponse response = client.execute(request, null).get();
            log.info("Status Code: {}", response.getCode());
            log.info("Response body: \n{}", response.getBodyText());
            client.close();
        } catch (URISyntaxException e) {
            log.warn("URISyntaxException: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            log.warn("InterruptedException: " + e.getMessage());
            e.printStackTrace();
        } catch (ExecutionException e) {
            log.warn("ExecutionException: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.warn("IOException : " + e.getMessage());
            e.printStackTrace();
        }
    }
}


