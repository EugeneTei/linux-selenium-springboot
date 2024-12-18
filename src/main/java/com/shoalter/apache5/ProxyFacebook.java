package com.shoalter.apache5;

import com.shoalter.SslUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProxyFacebook {

    public static final String URL = "https://www.facebook.com/api/graphql/";
    public static final HttpHost proxy = new HttpHost("44.218.183.55", 80);
//    public static final HttpHost proxy = new HttpHost("3.90.100.12", 80);

    public static void main(String[] args) throws Exception {

        SslUtil.trustAll();
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CloseableHttpClient httpClient = getCloseableHttpClient(routePlanner);

        try {
            HttpPost postRequest = new HttpPost(URL);
            postRequest.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
            postRequest.setHeader(new BasicHeader("Accept-encoding", "gzip, deflate, br, zstd"));
            postRequest.setHeader(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"));

            List<BasicNameValuePair> formParams = new ArrayList<>();
            formParams.add(new BasicNameValuePair("variables", "{\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}"));
            formParams.add(new BasicNameValuePair("doc_id", "8973253692695896"));
            postRequest.setEntity(new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                int statusCode = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                System.out.println("Status Code: " + statusCode);
                System.out.println("Response Body: \n" + responseBody);
            }
        } finally {
            httpClient.close();
        }
    }

    private static CloseableHttpClient getCloseableHttpClient(DefaultProxyRoutePlanner routePlanner) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(getPoolingHttpClientConnectionManager())
                .setRoutePlanner(routePlanner)            // Route traffic to proxy
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofSeconds(10))
                        .setResponseTimeout(Timeout.ofSeconds(10))
                        .build())
                .build();
        return httpClient;
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
