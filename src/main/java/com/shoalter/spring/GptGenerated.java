package com.shoalter.spring;

import com.shoalter.util.SslUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class GptGenerated {

    private static final String proxyHost = "148.66.6.214";
    private static final int proxyPort = 80;

    public static void main(String[] args) {

        String url = "https://www.facebook.com/api/graphql/";

        RestTemplate restTemplate = createRestTemplateWithProxy(proxyHost, proxyPort);
        SslUtil.trustAll();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("variables", "{\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}");
        body.add("doc_id", "8973253692695896");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            System.out.println("Response Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static RestTemplate createRestTemplateWithProxy(String proxyHost, int proxyPort) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(java.net.HttpURLConnection connection, String httpMethod) {
                try {
                    if (connection instanceof HttpsURLConnection) {
                        ((HttpsURLConnection) connection).setHostnameVerifier((hostname, session) -> true);
                    }
                    super.prepareConnection(connection, httpMethod);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to prepare connection", e);
                }
            }
        };

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        factory.setProxy(proxy);

        return new RestTemplate(factory);
    }
}
