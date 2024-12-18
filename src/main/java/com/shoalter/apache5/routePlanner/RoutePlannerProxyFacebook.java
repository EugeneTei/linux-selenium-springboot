package com.shoalter.apache5.routePlanner;

import com.shoalter.util.SslUtil;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RoutePlannerProxyFacebook {

    private static final String body = "variables={\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}&doc_id=8973253692695896";

    public static void main(String[] args) throws Exception {

        SslUtil.trustAll();
        String url = "https://www.google.com/search?q=google&oq=google&gs_lcrp=EgZjaHJvbWUqDAgAECMYJxiABBiKBTIMCAAQIxgnGIAEGIoFMgYIARBFGDwyEggCEC4YQxjHARjRAxiABBiKBTIGCAMQRRg8MgYIBBBFGDwyBggFEEUYPDIGCAYQRRhBMgYIBxBFGDzSAQc4OTFqMGo0qAIAsAIB&sourceid=chrome&ie=UTF-8";
        url = "https://www.facebook.com/api/graphql/";

        CloseableHttpClient httpclient = HttpClients
                .custom()
                .setConnectionManager(SslUtil.getPoolingHttpClientConnectionManager())
                .setRoutePlanner(getDefaultProxyRoutePlanner())
                .build();

        HttpPost httpPost = getHttpPost(url);

        CloseableHttpResponse response = httpclient.execute(httpPost);

        printResponse(response);
    }

    private static @NotNull DefaultProxyRoutePlanner getDefaultProxyRoutePlanner() {
        HttpHost proxy = new HttpHost("44.218.183.55", 80);
        return new DefaultProxyRoutePlanner(proxy);
    }

    private static @NotNull HttpPost getHttpPost(String url) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
        httpPost.setEntity(new StringEntity(body));
        return httpPost;
    }

    private static void printResponse(CloseableHttpResponse response) throws IOException {
        InputStream in = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        StringBuilder responseBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBody.append(line).append("\n");
        }
        System.out.println("Response Body: " + responseBody);
    }
}
