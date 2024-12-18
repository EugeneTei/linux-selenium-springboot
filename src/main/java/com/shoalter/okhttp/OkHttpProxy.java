package com.shoalter.okhttp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

public class OkHttpProxy {

    public static String PROXY_HOST = "44.218.183.55";
    public static int PROXY_PORT = 80;

    public static void main(String[] args) throws Exception {

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[0];
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));

        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(proxy)
                .readTimeout(30, TimeUnit.SECONDS)
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .build();

        String url = "https://www.example.org/";
        url = "https://httpbin.org/ip";
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        if(response.body() != null) {
            System.out.println("Response body: " + response.body().string());
        }
    }
}