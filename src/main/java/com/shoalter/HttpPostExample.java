package com.shoalter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpPostExample {

    public static void main(String[] args) {

        SslUtil.trustAll();

        String proxyHost = "103.152.112.120";
        int proxyPort = 80;

        String url = "https://www.facebook.com/api/graphql/";

        String payload = "variables={\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}"
                + "&doc_id=8973253692695896";

        try {
            HttpClient httpClient = HttpClient.newBuilder()
                    .proxy(java.net.ProxySelector.of(new InetSocketAddress(proxyHost, proxyPort)))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("e.getMessage(): " + e.getMessage());
        }
    }
}

