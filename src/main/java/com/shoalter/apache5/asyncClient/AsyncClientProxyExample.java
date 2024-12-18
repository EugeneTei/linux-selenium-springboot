package com.shoalter.apache5.asyncClient;


import com.shoalter.SslUtil;
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
public class AsyncClientProxyExample {

        public static final String URL = "http://www.example.org";
//    public static final String URL = "http://neverssl.com";

    //    public static final String URL = "https://www.example.com/api";
    //    public static  final String URL = "https://www.facebook.com/api/graphql/";
//    public static final String URL = "https://www.google.com.tw/?hl=zh_TW";
    public static HttpHost PROXY = new HttpHost("http", "44.218.183.55", 80);

    public static void main(String[] args) throws IOException, URISyntaxException, ExecutionException, InterruptedException {

        SslUtil.trustAll();
        CloseableHttpAsyncClient client = HttpAsyncClients.custom()
                .setProxy(PROXY)
                .build();
        client.start();

        SimpleHttpRequest request = SimpleRequestBuilder.get(new URI(URL)).build();
        SimpleHttpResponse response = client.execute(request, null).get();
        log.info("Status Code: {}", response.getCode());
        log.info("Response body: \n{}", response.getBodyText());
        client.close();
    }
}


