package com.shoalter.apache5.asyncClient;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Future;

// Success to get data from google
@Slf4j
public class ProxyHttpAsyncClientExample {

    //    public static final String URL = "http://www.example.org";
//    public static final String URL = "http://neverssl.com";
//
//    public static final String URL = "https://www.example.com/api";
    public static final String URL = "https://www.google.com.tw/?hl=zh_TW";

    public static HttpHost PROXY = new HttpHost("http", "44.218.183.55", 80);

    public static void main(String[] args) throws Exception {

        try (CloseableHttpAsyncClient httpClient = getClient()) {
            httpClient.start();

            SimpleHttpRequest request = SimpleRequestBuilder
                    .get(URL)
                    .build();

            Future<SimpleHttpResponse> future = httpClient.execute(request, null);
            SimpleHttpResponse response = future.get();

            log.info("Response BodyText: {}", response.getBodyText());
        }
    }

    private static CloseableHttpAsyncClient getClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(PROXY);
        return HttpAsyncClients.custom()
                .setProxy(PROXY)
                .setConnectionManager(getClientConnectionManager())
                .setRoutePlanner(routePlanner)
                .build();
    }

    private static PoolingAsyncClientConnectionManager getClientConnectionManager() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        TrustStrategy acceptingTrustStrategy = (certificate, authType) -> true;

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        TlsStrategy tlsStrategy = ClientTlsStrategyBuilder.create()
                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSslContext(sslContext)
                .build();

        PoolingAsyncClientConnectionManager cm = PoolingAsyncClientConnectionManagerBuilder.create()
                .setTlsStrategy(tlsStrategy)
                .build();

        return cm;
    }
}
