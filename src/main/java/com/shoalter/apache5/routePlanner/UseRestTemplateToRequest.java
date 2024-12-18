package com.shoalter.apache5.routePlanner;

import com.shoalter.util.SslUtil;
import lombok.extern.slf4j.Slf4j;
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

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class UseRestTemplateToRequest {

    private static final String URL = "https://www.facebook.com/api/graphql/";
    //    private static final String PROXY_HOST = "3.90.100.12"; //https
//    private static final String PROXY_HOST = "54.152.3.36";
    private static final String PROXY_HOST = "44.219.175.186";
    private static final int PROXY_PORT = 80;

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SslUtil.trustAll();
        RestTemplate restTemplate = createRestTemplateWithProxy(PROXY_HOST, PROXY_PORT);
        HttpEntity<MultiValueMap<String, String>> requestEntity = getHttpEntity();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(URL, requestEntity, String.class);
            log.info("Response Code: {}", response.getStatusCode());
            log.info("Response Body: {}", response.getBody());
        } catch (Exception e) {
            log.warn("Request Failed: {}", e.getMessage());
        }
    }

    private static RestTemplate createRestTemplateWithProxy(String proxyHost, int proxyPort) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpHost proxy = new HttpHost("http", proxyHost, proxyPort);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(10)) // 連線超時 10 秒
                .setResponseTimeout(Timeout.ofSeconds(10)) // 回應超時 10 秒
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(SslUtil.getPoolingHttpClientConnectionManager())
                .setDefaultRequestConfig(requestConfig)
                .setRoutePlanner(routePlanner)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }

    private static HttpEntity<MultiValueMap<String, String>> getHttpEntity() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("variables", "{\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}");
        body.add("doc_id", "8973253692695896");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(body, headers);
    }
}
