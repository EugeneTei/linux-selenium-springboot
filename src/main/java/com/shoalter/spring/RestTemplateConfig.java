package com.shoalter.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;

@RequiredArgsConstructor
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    public RestTemplate proxy() {
//        RestTemplate restTemplate = new RestTemplate();
//        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
//        simpleClientHttpRequestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("47.254.36.213", 8081)));
//        restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
//        return restTemplate;
//    }

    @Bean
    public RestTemplate proxyDisableSsl() {
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
}
