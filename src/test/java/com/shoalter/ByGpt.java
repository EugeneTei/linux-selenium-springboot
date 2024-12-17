package com.shoalter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.cert.X509Certificate;

public class ByGpt {

    private static final String proxyHost = "148.66.6.214";
    private static final int proxyPort = 80;

    public static void main(String[] args) {

        String url = "https://www.facebook.com/api/graphql/";

        RestTemplate restTemplate = createRestTemplateWithProxy(proxyHost, proxyPort);
        addFakeSsl();

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

    private static void addFakeSsl() {
        SSLContext ctx;
        TrustManager[] trustAllCerts = new X509TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, trustAllCerts, null);
            SSLContext.setDefault(ctx);
        } catch (Exception e) {
        }
    }
}
