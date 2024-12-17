package com.shoalter;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

// 不能用
public class ApiService {

    public static void main(String[] args) {
        RestTemplate restTemplate = createRestTemplateWithProxy();

        String apiUrl = "https://www.facebook.com/api/graphql/";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String variables = "{\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}";
        String docId = "8973253692695896";

        Map<String, String> formData = new HashMap<>();
        formData.put("variables", variables);
        formData.put("doc_id", docId);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(formData, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        System.out.println("Response: " + response.getBody());
    }

    private static RestTemplate createRestTemplateWithProxy() {
        String proxyHost = "203.95.196.90";
        int proxyPort = 8080;

        HttpClientBuilder clientBuilder = HttpClients.custom()
                .setProxy(new HttpHost(proxyHost, proxyPort));

        // Set up the connection manager to manage connections
        PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
        clientBuilder.setConnectionManager(poolingConnManager);


        // Create the HttpClient with the configuration
        HttpClient httpClient = clientBuilder.build();

        // Create the RequestFactory with the custom HttpClient
        ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        // Return a RestTemplate with the custom RequestFactory
        return new RestTemplate(factory);
    }
}

