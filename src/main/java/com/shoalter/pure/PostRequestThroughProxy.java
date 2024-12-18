package com.shoalter.pure;

import com.shoalter.util.SslUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class PostRequestThroughProxy {

    private static final String PROXY_HOST = "44.218.183.55";
    private static final int PROXY_PORT = 80;
    private static final Proxy PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
    private static final String POST_DATA = "variables={\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}&doc_id=8973253692695896";

    public static void main(String[] args) throws Exception {

        String urlStr = "http://www.example.org";
//        urlStr = "https://www.facebook.com/api/graphql/";

        SslUtil.trustAll();

        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(PROXY);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set headers
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(POST_DATA.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        try (InputStream is = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            printBody(reader);
        } catch (IOException e) {
            handleErrorMessage(connection);
        }
    }

    private static void printBody(BufferedReader reader) throws IOException {
        String line;
        StringBuilder responseBody = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            responseBody.append(line + "\n");
        }
        System.out.println("Response Body: " + responseBody);
    }

    private static void handleErrorMessage(HttpURLConnection connection) throws IOException {
        try (InputStream es = connection.getErrorStream();
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(es))) {
            String line;
            StringBuilder errorBody = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorBody.append(line);
            }
            System.err.println("Error Body: " + errorBody);
        }
    }
}

