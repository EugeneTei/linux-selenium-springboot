package com.shoalter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Slf4j
public class ProxyTest {

    public static void main(String[] args) {
        try {
            FakeSslUtil.addFakeSsl();
            RestTemplate restTemplate = createRestTemplateWithProxy();
            HttpEntity<MultiValueMap<String, String>> entity = createMultiValueMapHttpEntity();

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://www.facebook.com/api/graphql/",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            System.out.println("Response Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    private static RestTemplate createRestTemplateWithProxy() {
        HostnameVerifier PROMISCUOUS_VERIFIER = (s, sslSession) -> true;
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setHostnameVerifier(PROMISCUOUS_VERIFIER);
                }
                super.prepareConnection(connection, httpMethod);
            }
        };
        simpleClientHttpRequestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("148.66.6.214", 80)));
        restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
        return restTemplate;
    }

    private static HttpEntity<MultiValueMap<String, String>> createMultiValueMapHttpEntity() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("variables", "{\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}");
        params.add("doc_id", "8973253692695896");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        return new HttpEntity<>(params, headers);
    }
}
