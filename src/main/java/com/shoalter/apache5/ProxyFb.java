package com.shoalter.apache5;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ProxyFb {

    private static final String URL = "https://www.facebook.com/api/graphql/"; // 目標 API URL
    private static final String PROXY_HOST = "148.66.6.214"; // 代理主機
    private static final int PROXY_PORT = 80; // 代理

    public static void main(String[] args) {
        RestTemplate restTemplate = createRestTemplateWithProxy(PROXY_HOST, PROXY_PORT);

        HttpEntity<MultiValueMap<String, String>> requestEntity = getHttpEntity();

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(URL, requestEntity, String.class);
            System.out.println("Response Body:\n" + response.getBody());
        } catch (Exception e) {
            if (response != null) {
                System.out.println("Response Body:\n" + response.getBody());
            }
            System.err.println("Request Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 創建 RestTemplate 並設置 Proxy
     */
    private static RestTemplate createRestTemplateWithProxy(String proxyHost, int proxyPort) {
        // 設置代理配置
        HttpHost proxy = new HttpHost("http", proxyHost, proxyPort);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        // 創建 HttpClient，並設置 Route Planner
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(10)) // 連線超時 10 秒
                .setResponseTimeout(Timeout.ofSeconds(10)) // 回應超時 10 秒
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setRoutePlanner(routePlanner)
                .build();

        // 將 HttpClient 設置到 RestTemplate 中
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }

    /**
     * 構建 HttpEntity，包含 Headers 和 Body
     */
    private static HttpEntity<MultiValueMap<String, String>> getHttpEntity() {
        // 設置 Body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("variables", "{\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}");
        body.add("doc_id", "8973253692695896");

        // 設置 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(body, headers);
    }
}
