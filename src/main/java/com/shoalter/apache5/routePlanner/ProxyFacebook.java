package com.shoalter.apache5.routePlanner;

import com.shoalter.util.SslUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.Timeout;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProxyFacebook {

    public static final HttpHost proxy = new HttpHost("3.127.121.101", 3128);
//    public static final HttpHost proxy = new HttpHost("3.90.100.12", 80);

    public static void main(String[] args) throws Exception {

        String url = "https://www.facebook.com/api/graphql/";
//        url = "https://www.example.org/";

        SslUtil.trustAll();

        CloseableHttpClient httpClient = getCloseableHttpClient();

        try {
            HttpPost postRequest = getHttpPost(url);

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {

                int statusCode = response.getCode();
                log.info("Status Code: {}", statusCode);

                HttpEntity entity = response.getEntity();
                printResponse(entity);
            }
        } finally {
            httpClient.close();
        }
    }

    private static void printResponse(HttpEntity entity) throws IOException, ParseException {
        if (entity != null) {
            long length = entity.getContentLength();
            if (length != -1 && length < 2048) {
                String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                log.info("less than 2048: \n{}", responseBody);
            } else {
                // 資料過長，使用 Stream 讀取
                InputStream in = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                StringBuilder responseBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBody.append(line).append("\n");
                }
                log.info("Greater than 2048: \n{}", responseBody);
            }
        }
    }


    private static CloseableHttpClient getCloseableHttpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(SslUtil.getPoolingHttpClientConnectionManager())
                .setRoutePlanner(routePlanner)            // Route traffic to proxy
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofSeconds(10))
                        .setResponseTimeout(Timeout.ofSeconds(10))
                        .build())
                .build();
        return httpClient;
    }

    private static @NotNull HttpPost getHttpPost(String url) {
        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
        postRequest.setHeader(new BasicHeader("Accept-encoding", "gzip, deflate, br, zstd"));
        postRequest.setHeader(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"));

        List<BasicNameValuePair> formParams = getBasicNameValuePairs();
        postRequest.setEntity(new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8));
        return postRequest;
    }

    private static List<BasicNameValuePair> getBasicNameValuePairs() {
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("variables", "{\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}"));
        formParams.add(new BasicNameValuePair("doc_id", "8973253692695896"));
        return formParams;
    }
}
