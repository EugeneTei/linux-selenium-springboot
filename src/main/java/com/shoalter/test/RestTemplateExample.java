package com.shoalter.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestTemplateExample {

    public static final String url = "https://www.facebook.com/api/graphql/";

    public static void main(String[] args) {

//        List<ProxyDo> proxyList = new ArrayList<>();
//
//        try {
//            String taskConfigContent = new String(Files.readAllBytes(new File("ProxyList.json").toPath()));
//            proxyList = new Gson().fromJson(taskConfigContent, new TypeToken<List<ProxyDo>>() {
//            }.getType());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        for (int i = 0; i < 20; i++) {
//            FakeSslUtil.addFakeSsl();
//            execute(proxyList.get(ThreadLocalRandom.current().nextInt(proxyList.size())));
//        }
    }

//    private static void execute(ProxyDo proxyDo) {
//        HttpHost proxy = new HttpHost(proxyDo.getIp(), proxyDo.getPort());
//
//        // 將所有 HTTP 請求透過 proxy 傳送。
//        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
//
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setRoutePlanner(routePlanner)
//                .build();
//
//        // 透過 HttpComponentsClientHttpRequestFactory 來設定自定義請求工廠。
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(10000);
//
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.set("accept-language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7");
//
//        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//        formData.add("variables", "{\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}");
//        formData.add("doc_id", "8973253692695896");
//
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
//
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//            log.info("Success: {}", response.getBody().toString());
//        } catch (Exception e) {
//            log.warn("Failed: {}", e.getMessage());
//        }
//    }
}
