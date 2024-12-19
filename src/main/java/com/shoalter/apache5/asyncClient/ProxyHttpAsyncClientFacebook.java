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
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Future;

@Slf4j
public class ProxyHttpAsyncClientFacebook {


    public static HttpHost PROXY = new HttpHost("http", "87.248.129.26", 80);

    public static void main(String[] args) throws Exception {

        String url = "https://www.facebook.com/api/graphql/";
//        url = "https://www.google.com.tw/search?q=apple&sca_esv=35aa2c76c27153e3&hl=zh_TW&sxsrf=ADLYWIIXwec4TsQEaf3cjbAneIRlP7TtMA%3A1734494349840&source=hp&ei=jUhiZ4fyMOOhvr0PkJ3i-As&iflsig=AL9hbdgAAAAAZ2JWnc0YtyT1DG387a5c0UAFWV59wwTu&ved=0ahUKEwjH7Nb8trCKAxXjkK8BHZCOGL8Q4dUDCBk&uact=5&oq=apple&gs_lp=Egdnd3Mtd2l6IgVhcHBsZTIKECMYgAQYJxiKBTINEAAYgAQYsQMYQxiKBTILEC4YgAQY0QMYxwEyDRAAGIAEGLEDGEMYigUyDhAuGIAEGLEDGNEDGMcBMgsQABiABBixAxiDATIIEAAYgAQYsQMyCxAAGIAEGLEDGIMBMggQABiABBixAzIKEAAYgAQYQxiKBUifCFCFAljbB3ABeACQAQCYATSgAeYBqgEBNbgBA8gBAPgBAZgCBqAC9wGoAgrCAgcQIxgnGOoCwgIEECMYJ8ICERAuGIAEGLEDGNEDGIMBGMcBwgIFEAAYgATCAhMQLhiABBixAxjRAxhDGMcBGIoFwgIWEC4YgAQYsQMY0QMYQxjHARjJAxiKBcICEBAuGIAEGNEDGEMYxwEYigXCAgsQABiABBiSAxiKBZgDBvEFe2ZWDPQC-RKSBwE2oAfuNQ&sclient=gws-wiz";

        try (CloseableHttpAsyncClient httpClient = getClient()) {

            httpClient.start();

            String bodyStr = "variables={\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}&doc_id=8973253692695896";

            SimpleHttpRequest request = SimpleRequestBuilder
                    .post(url)
                    .setBody(bodyStr, ContentType.APPLICATION_FORM_URLENCODED)
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            Future<SimpleHttpResponse> future = httpClient.execute(request, null);
            SimpleHttpResponse response = future.get();

            log.info("Response BodyText: {}", response.getBodyText());
        } catch (Exception e) {
            e.printStackTrace();
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
