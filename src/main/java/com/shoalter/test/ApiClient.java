package com.shoalter.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoalter.FakeSslUtil;
import com.shoalter.test.pojo.ProxyDo;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@Slf4j
public class ApiClient {

    private static final String URL = "https://www.facebook.com/api/graphql/";

    public static void main(String[] args) throws Exception {

        List<ProxyDo> proxyDoList = getProxyList();

        for (int i = 0; i < 20; i++) {
            log.info("Round: {}", i);
            disableSsl();
            ProxyDo proxyDo = proxyDoList.get(ThreadLocalRandom.current().nextInt(proxyDoList.size()));
            log.info("HOST: {}", proxyDo.getIp(), proxyDo.getPort());
            execute(proxyDo);
            System.out.println("");
        }
    }

    private static void execute(ProxyDo proxyDo) {
        try {
            RestTemplate restTemplate = createRestTemplateWithProxy(proxyDo);
            HttpEntity<MultiValueMap<String, String>> requestEntity = getHttpEntity();

            ResponseEntity<String> response = restTemplate.postForEntity(URL, requestEntity, String.class);

            log.info("Response: {}", response.getBody());
        } catch (HttpClientErrorException e) {
            log.warn("Client Failed: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("Error: {}", e.getMessage());
        }
    }

    private static HttpEntity<MultiValueMap<String, String>> getHttpEntity() {
        return new HttpEntity<>(getStringStringMultiValueMap(), getHttpHeaders());
    }

    private static MultiValueMap<String, String> getStringStringMultiValueMap() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("variables", "{\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}");
        body.add("doc_id", "8973253692695896");
        return body;
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private static List<ProxyDo> getProxyList() {

        try {
            String taskConfigContent = new String(Files.readAllBytes(new File("ProxyList.json").toPath()));
            return new Gson().fromJson(taskConfigContent, new TypeToken<List<ProxyDo>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static RestTemplate createRestTemplateWithProxy(ProxyDo proxyDo) {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();

        // completed
        simpleClientHttpRequestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyDo.getIp(), proxyDo.getPort())));

        restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
        return restTemplate;
    }

    public static void disableSsl() {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // Trust all client certificates
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // Trust all server certificates
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }
}
