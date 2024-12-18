package com.shoalter.spring;

public class ProxyPostRequestApplication {

    public static void main(String[] args) {
        System.out.println("Hello World!");
//        // 1. 設定代理伺服器 (148.66.6.214:80)
//        HttpHost proxy = new HttpHost("148.66.6.214", 80);
//        RequestConfig config = RequestConfig.custom()
//                .setProxy(proxy)
//                .build();
//
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setDefaultRequestConfig(config)
//                .build();
//
//        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
//
//        // 4. 設定目標 URL
//        String url = "https://www.facebook.com/api/graphql/";
//
//        // 5. 設定請求 Header
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
//
//        // 6. 設定請求 Payload (表單參數)
//        Map<String, String> formData = new LinkedHashMap<>();
//        formData.put("variables", "{\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}");
//        formData.put("doc_id", "8973253692695896");
//
//        // 7. 將表單參數和 Header 包裝到 HttpEntity 中
//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(formData, headers);
//
//        try {
//            // 8. 發送 POST 請求
//            ResponseEntity<String> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.POST,
//                    requestEntity,
//                    String.class
//            );
//
//            // 9. 輸出結果
//            System.out.println("Response Code: " + response.getStatusCodeValue());
//            System.out.println("Response Body: " + response.getBody());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("Error occurred during the POST request.");
//        }
    }
}

