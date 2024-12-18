package com.shoalter.apache5.requestConfig;

import com.shoalter.util.SslUtil;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// Request google-search url through a proxy and it can work !
public class RequestConfigProxyExample {

    private static final HttpHost PROXY = new HttpHost("44.218.183.55", 80);

    public static void main(String[] args) throws Exception {

        SslUtil.trustAll();

        String url = "https://www.google.com/search?q=google&oq=google&gs_lcrp=EgZjaHJvbWUqDAgAECMYJxiABBiKBTIMCAAQIxgnGIAEGIoFMgYIARBFGDwyEggCEC4YQxjHARjRAxiABBiKBTIGCAMQRRg8MgYIBBBFGDwyBggFEEUYPDIGCAYQRRhBMgYIBxBFGDzSAQc4OTFqMGo0qAIAsAIB&sourceid=chrome&ie=UTF-8";

        CloseableHttpClient httpclient = HttpClients
                .custom()
                .setConnectionManager(SslUtil.getPoolingHttpClientConnectionManager())
                .build();
        HttpGet request = new HttpGet(url);

        request.setConfig(
                RequestConfig.custom()
                        .setProxy(PROXY)
                        .build()
        );

        CloseableHttpResponse response = httpclient.execute(request);

        printResponse(response);
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
