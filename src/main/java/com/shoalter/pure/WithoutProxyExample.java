package com.shoalter.pure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// without using Proxy
// can work
public class WithoutProxyExample {

    public static void main(String[] args) throws Exception {
        URL serverUrl = new URL("http://www.example.org");
        HttpURLConnection conn =
                (HttpURLConnection) serverUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        String result = getReturn(conn);

        System.out.println("Result: " + result);
    }

    private static String getReturn(HttpURLConnection connection)
            throws Exception {
        StringBuffer buffer = new StringBuffer();

        try (InputStream inputStream = connection.getInputStream();
             InputStreamReader inputStreamReader =
                     new InputStreamReader(inputStream, "UTF-8");
             BufferedReader bufferedReader =
                     new BufferedReader(inputStreamReader);) {
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str + "\n");
            }
            return buffer.toString();
        }
    }
}
