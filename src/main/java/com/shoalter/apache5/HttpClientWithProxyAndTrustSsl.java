package com.shoalter.apache5;

import com.shoalter.SslUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class HttpClientWithProxyAndTrustSsl {

    public static void main(String[] args) throws Exception {
        String url = "https://www.example.com/api";
//        url = "https://www.hktvmall.com/";
//        url = "https://www.google.com/search?q=apple&sca_esv=0c2c2c7fe2c65901&sxsrf=ADLYWIJCnXYg_QoQgv8KL8Rw6ISOgMx8mQ%3A1734495380189&ei=lExiZ5iAC7Gcvr0PlKiIqAc&ved=0ahUKEwiYmoDourCKAxUxjq8BHRQUAnUQ4dUDCBA&uact=5&oq=apple&gs_lp=Egxnd3Mtd2l6LXNlcnAiBWFwcGxlMgoQIxiABBgnGIoFMgoQIxiABBgnGIoFMgQQIxgnMhYQLhiABBixAxjRAxhDGMcBGMkDGIoFMg0QABiABBixAxhDGIoFMhMQLhiABBixAxjRAxhDGMcBGIoFMgoQABiABBhDGIoFMg0QABiABBixAxhDGIoFMhAQLhiABBjRAxhDGMcBGIoFMgsQABiABBiSAxiKBTIlEC4YgAQYsQMY0QMYQxjHARjJAxiKBRiXBRjcBBjeBBjgBNgBAUjVCVC6A1inCXABeAGQAQCYATmgAdABqgEBNLgBA8gBAPgBAZgCBaAC5wHCAgcQIxiwAxgnwgIKEAAYsAMY1gQYR8ICDhAuGIAEGLEDGNEDGMcBwgIIEAAYgAQYsQPCAhEQLhiABBixAxjRAxiDARjHAcICBRAAGIAEwgIdEC4YgAQYsQMY0QMYxwEYlwUY3AQY3gQY4ATYAQHCAiIQLhiABBixAxjRAxhDGMcBGIoFGJcFGNwEGN4EGOAE2AEBmAMAiAYBkAYKugYGCAEQARgUkgcBNaAHvTA&sclient=gws-wiz-serp";
//        url = "https://tw.yahoo.com/";

        SslUtil.trustAll();

        HttpHost proxy = new HttpHost("http", "44.218.183.55", 80); // proxy host and port
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(getPoolingHttpClientConnectionManager())  // 信任所有憑證（非必要）
                .setRoutePlanner(routePlanner)            // Route traffic to proxy
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

            log.info("Response: \n{}", response);
        } catch (Exception e) {
            log.warn("Request failed: {}", e.getMessage());
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
