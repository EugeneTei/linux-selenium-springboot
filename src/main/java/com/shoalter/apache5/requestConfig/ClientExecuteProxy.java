package com.shoalter.apache5.requestConfig;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoalter.pojo.ProxyDo;
import com.shoalter.util.SslUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class ClientExecuteProxy {

    public static void main(String[] args) throws Exception {

        SslUtil.trustAll();

        List<ProxyDo> proxyDoList = getProxyList();

        for (int i = 0; i < 10; i++) {
            log.info("Round: {}", i);

            ProxyDo proxyDo = proxyDoList.get(ThreadLocalRandom.current().nextInt(proxyDoList.size()));

            log.info("HOST: {}, PORT: {}", proxyDo.getIp(), proxyDo.getPort());

            try {
                doThe(proxyDo);
            } catch (Exception e) {
                log.warn("Error: {}", e.getMessage());
            }

            System.out.println(" ");
        }
    }

    private static void doThe(ProxyDo proxyDo) throws IOException, ParseException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(SslUtil.getPoolingHttpClientConnectionManager()).build();
        try {
            HttpHost target = new HttpHost("https", "google.com");
            HttpHost proxy = new HttpHost("http", proxyDo.getIp(), proxyDo.getPort());

            RequestConfig config = RequestConfig.custom()
                    .setProxy(proxy)
                    .setConnectionRequestTimeout(Timeout.ofSeconds(5))
                    .setResponseTimeout(Timeout.ofSeconds(5))
                    .build();
            HttpGet request = new HttpGet("/search?q=apple");
            request.setConfig(config);

            System.out.println("Executing request " + request.getRequestUri() + " to " + target + " via " + proxy);

            CloseableHttpResponse response = httpclient.execute(target, request);
            try {
                System.out.println("----------------------------------------");

                String body = EntityUtils.toString(response.getEntity());
                if (body.length() > 50) {
                    body = body.substring(0, 50);
                }
                log.info("Response: {}", body);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    private static List<ProxyDo> getProxyList() {

        try {
            String taskConfigContent = new String(Files.readAllBytes(new File("ProxyList.json").toPath()));
            return new Gson().fromJson(taskConfigContent, new TypeToken<List<ProxyDo>>() {
            }.getType());
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return List.of();
        }
    }
}
