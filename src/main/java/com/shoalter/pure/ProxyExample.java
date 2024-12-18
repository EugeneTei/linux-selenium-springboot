package com.shoalter.pure;

import com.shoalter.SslUtil;

import java.io.*;
import java.net.*;

public class ProxyExample {

    private static final String PROXY_HOST = "44.218.183.55";
    private static final int PROXY_PORT = 80;

    private static final String requestBody = "variables={\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}&doc_id=8973253692695896";

    public static void main(String[] args) {
        SslUtil.trustAll();
        // API URL and Proxy configuration
        String apiUrl = "http://www.example.com/api";
        apiUrl = "https://www.google.com/search?q=apple&sca_esv=664a1b17173617b9&sxsrf=ADLYWIKj73u1_p1zrgZtmhECK5IUmA2ljA%3A1734503660492&ei=7GxiZ7HTHeyZvr0PmoCqqAE&ved=0ahUKEwix-a3U2bCKAxXsjK8BHRqAChUQ4dUDCBA&uact=5&oq=apple&gs_lp=Egxnd3Mtd2l6LXNlcnAiBWFwcGxlMgoQIxiABBgnGIoFMgoQIxiABBgnGIoFMgQQIxgnMhYQLhiABBixAxjRAxhDGMcBGMkDGIoFMg0QABiABBixAxhDGIoFMhMQLhiABBixAxjRAxhDGMcBGIoFMgoQABiABBhDGIoFMg0QABiABBixAxhDGIoFMhAQLhiABBjRAxhDGMcBGIoFMgsQABiABBiSAxiKBTIlEC4YgAQYsQMY0QMYQxjHARjJAxiKBRiXBRjcBBjeBBjgBNgBAUj_B1DqA1jvB3ABeACQAQCYAXCgAbYCqgEDNC4xuAEDyAEA-AEBmAIFoALeAcICBxAjGLADGCfCAgoQABiwAxjWBBhHwgIOEC4YgAQYsQMY0QMYxwHCAggQABiABBixA8ICERAuGIAEGLEDGNEDGIMBGMcBwgIFEAAYgATCAh0QLhiABBixAxjRAxjHARiXBRjcBBjeBBjgBNgBAcICIhAuGIAEGLEDGNEDGEMYxwEYigUYlwUY3AQY3gQY4ATYAQGYAwCIBgGQBgq6BgYIARABGBSSBwE1oAfXOA&sclient=gws-wiz-serp";
        apiUrl = "https://www.facebook.com/api/graphql/";



        try {
            java.net.Proxy proxy = getProxy();

            // Step 2: Open a connection to the target URL through the proxy
            URL url = new URL(apiUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Step 3: Configure the connection (as POST, for example)
            setConnection(connection);

            // Step 4: Write request body (if applicable)
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.getBytes());
                os.flush();
            }

            // Step 5: Read the response
            int statusCode = connection.getResponseCode();
            System.out.println("Response Code: " + statusCode);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                System.out.println("Response Body:");
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // Step 6: Disconnect
            connection.disconnect();
        } catch (IOException e) {
            System.err.println("Error occurred while making the request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void setConnection(HttpURLConnection connection) throws ProtocolException {
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(requestBody.length()));
    }

    private static java.net.Proxy getProxy() {
        java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
        return proxy;
    }

}
