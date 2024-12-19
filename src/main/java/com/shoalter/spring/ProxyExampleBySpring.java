package com.shoalter.spring;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoalter.pojo.ProxyDo;
import com.shoalter.util.SslUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class ProxyExampleBySpring {

//    private static final String URL = "https://www.facebook.com/api/graphql/";
    //    private static final String URL = "https://www.google.com/search?q=apple&sca_esv=0c2c2c7fe2c65901&sxsrf=ADLYWIJCnXYg_QoQgv8KL8Rw6ISOgMx8mQ%3A1734495380189&ei=lExiZ5iAC7Gcvr0PlKiIqAc&ved=0ahUKEwiYmoDourCKAxUxjq8BHRQUAnUQ4dUDCBA&uact=5&oq=apple&gs_lp=Egxnd3Mtd2l6LXNlcnAiBWFwcGxlMgoQIxiABBgnGIoFMgoQIxiABBgnGIoFMgQQIxgnMhYQLhiABBixAxjRAxhDGMcBGMkDGIoFMg0QABiABBixAxhDGIoFMhMQLhiABBixAxjRAxhDGMcBGIoFMgoQABiABBhDGIoFMg0QABiABBixAxhDGIoFMhAQLhiABBjRAxhDGMcBGIoFMgsQABiABBiSAxiKBTIlEC4YgAQYsQMY0QMYQxjHARjJAxiKBRiXBRjcBBjeBBjgBNgBAUjVCVC6A1inCXABeAGQAQCYATmgAdABqgEBNLgBA8gBAPgBAZgCBaAC5wHCAgcQIxiwAxgnwgIKEAAYsAMY1gQYR8ICDhAuGIAEGLEDGNEDGMcBwgIIEAAYgAQYsQPCAhEQLhiABBixAxjRAxiDARjHAcICBRAAGIAEwgIdEC4YgAQYsQMY0QMYxwEYlwUY3AQY3gQY4ATYAQHCAiIQLhiABBixAxjRAxhDGMcBGIoFGJcFGNwEGN4EGOAE2AEBmAMAiAYBkAYKugYGCAEQARgUkgcBNaAHvTA&sclient=gws-wiz-serp";
//    private static final String URL = "https://www.hktvmall.com/";
    private static final String URL = "http://www.example.org/";

    public static void main(String[] args) throws Exception {

        List<ProxyDo> proxyDoList = getProxyList();

        SslUtil.trustAll();

        for (int i = 0; i < 20; i++) {
            log.info("Round: {}", i);

            ProxyDo proxyDo = proxyDoList.get(ThreadLocalRandom.current().nextInt(proxyDoList.size()));

            log.info("HOST: {}, PORT: {}", proxyDo.getIp(), proxyDo.getPort());

            execute(proxyDo);

            System.out.println(" ");
        }
    }

    private static void execute(ProxyDo proxyDo) {
        try {
            RestTemplate restTemplate = createRestTemplateWithProxy(proxyDo);
            HttpEntity<MultiValueMap<String, String>> requestEntity = getHttpEntity();

//            ResponseEntity<String> response = restTemplate.postForEntity(URL, requestEntity, String.class);
            ResponseEntity<String> response = restTemplate.getForEntity(URL, String.class);

            String responseBody = response.getBody();

            if (responseBody != null) {
                if (responseBody.length() > 1000) {
                    responseBody = responseBody.substring(0, 1000);
                }

                log.info("Response Body: {}", responseBody);
            }
        } catch (HttpClientErrorException e) {
            log.warn("Client Failed: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("Error: {}", e.getMessage());
        }
    }

    private static HttpEntity<MultiValueMap<String, String>> getHttpEntity() {
        return new HttpEntity<>(getStringStringMultiValueMap(), getHttpHeaders());
    }

    private static MultiValueMap<String, String> getStringStringMultiValueMap() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("variables", "{\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}");
        body.add("doc_id", "8973253692695896");
        return body;
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        headers.set("accept-encoding", "gzip, deflate, br, zstd");
        headers.set("accept-language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        return headers;
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

    private static RestTemplate createRestTemplateWithProxy(ProxyDo proxyDo) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(5000); // milliseconds
        simpleClientHttpRequestFactory.setReadTimeout(5000); // milliseconds

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyDo.getIp(), proxyDo.getPort()));
        simpleClientHttpRequestFactory.setProxy(proxy);

        restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
        return restTemplate;
    }

}
