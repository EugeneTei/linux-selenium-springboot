package com.shoalter.apache5;

import com.shoalter.SslUtil;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

// Completed
public class HttpClientWithProxyAndIgnoreSsl {

    public static void main(String[] args) throws Exception {
        String url = "https://www.example.com/api";
//        url = "http://www.example.org";

        SslUtil.trustAll();

        HttpHost proxy = new HttpHost("http", "44.218.183.55", 80); // proxy host and port
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        PoolingHttpClientConnectionManager connectionManager = getPoolingHttpClientConnectionManager();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)  // 信任所有憑證（非必要）
                .setRoutePlanner(routePlanner)            // Use proxy
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofSeconds(10))
                        .setResponseTimeout(Timeout.ofSeconds(10))
                        .build())
                .build();

        try {
            String response = Request.get(url)
                    .viaProxy(proxy)
                    .connectTimeout(Timeout.ofSeconds(10))
                    .responseTimeout(Timeout.ofSeconds(10))
                    .execute()
                    .returnContent()
                    .asString();

            System.out.println("Response: \n" + response);
        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
        } finally {
            httpClient.close();
        }
    }

    private static PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(TrustAllStrategy.INSTANCE) // 信任所有憑證
                .build();
        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(
                        SSLConnectionSocketFactoryBuilder.create()
                                .setSslContext(sslContext) // 使用自定義 SSLContext
                                .build()
                )
                .build();
        return connectionManager;
    }
}
