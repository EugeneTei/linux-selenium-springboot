package com.shoalter.spring;

import com.shoalter.util.SslUtil;
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
public class RequestFactoryProxyTest {

    public static void main(String[] args) {
        try {

            String url = "https://www.facebook.com/api/graphql/";
//            url = "https://www.google.com/search?q=google&oq=google&gs_lcrp=EgZjaHJvbWUqDAgAECMYJxiABBiKBTIMCAAQIxgnGIAEGIoFMgYIARBFGDwyEggCEC4YQxjHARjRAxiABBiKBTIGCAMQRRg8MgYIBBBFGDwyBggFEEUYPDIGCAYQRRhBMgYIBxBFGDzSAQgxNzE0ajBqN6gCALACAA&sourceid=chrome&ie=UTF-8"; //can work

            SslUtil.trustAll();
            RestTemplate restTemplate = createRestTemplateWithProxy();
            HttpEntity<MultiValueMap<String, String>> entity = createMultiValueMapHttpEntity();

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
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
        simpleClientHttpRequestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("44.218.183.55", 80)));
        restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
        return restTemplate;
    }

    private static HttpEntity<MultiValueMap<String, String>> createMultiValueMapHttpEntity() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("variables", "{\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}");
        params.add("doc_id", "8973253692695896");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        return new HttpEntity<>(params, headers);
    }
}
