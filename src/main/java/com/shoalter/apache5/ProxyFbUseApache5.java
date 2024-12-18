package com.shoalter.apache5;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProxyFbUseApache5 {

    public static void main(String[] args) {
        String url = "https://www.facebook.com/api/graphql/";

        HttpHost proxy = new HttpHost("http", "148.66.6.214", 80);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(10)) // 連線超時 10 秒
                .setResponseTimeout(Timeout.ofSeconds(10)) // 回應超時 10 秒
                .build();

        // 創建 HttpClient，包含代理設定
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setRoutePlanner(routePlanner)
                .build()) {

            // 創建 POST 請求
            HttpPost httpPost = new HttpPost(url);

            // 設定 Content-Type 標頭
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

            // 設定請求 Body 參數
            String body = "variables={\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}"
                    + "&doc_id=8973253692695896";
            httpPost.setEntity(new StringEntity(body));

            // 執行請求並取得回應
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                System.out.println("HTTP Status: " + response.getCode());

                // 取得回應內容
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
