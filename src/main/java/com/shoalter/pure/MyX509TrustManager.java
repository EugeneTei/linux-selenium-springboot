package com.shoalter.pure;

import com.shoalter.SslUtil;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.X509Certificate;

public class MyX509TrustManager { //implements X509TrustManager

    private static final String PROXY_HOST = "44.218.183.55";
    private static final int PROXY_PORT = 80;

    private static final String REQUEST_BODY = "variables={\"cursor\": \"\", \"id\": \"100044641110094\", \"count\": 3}&doc_id=8973253692695896";

//    @Override
//    public void checkClientTrusted(X509Certificate[] chain, String authType) {
//    }
//
//    @Override
//    public void checkServerTrusted(X509Certificate[] chain, String authType) {
//    }
//
//    @Override
//    public X509Certificate[] getAcceptedIssuers() {
//        return null;
//    }

    public static void main(String[] args) throws Exception {

        SslUtil.trustAll();

        String urlStr = "https://www.example.org/";
        urlStr = "https://www.hktvmall.com/";
        urlStr = "https://www.facebook.com/api/graphql/";
        URL url = new URI(urlStr).toURL();

        HttpsURLConnection connection =
                (HttpsURLConnection) url.openConnection(getProxy());
//        HostnameVerifier ignoreHostnameVerifier = (s, sslsession) -> {
//            System.out.println("WARNING: Hostname is not matched for cert.");
//            return true;
//        };

//        connection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
//        connection.setDefaultSSLSocketFactory(getSslSocketFactory());
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String result = getReturn(connection);
        System.out.println("Result: " + result);
    }

//    private static SSLSocketFactory getSslSocketFactory() throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
//        SSLContext sc = SSLContext.getInstance("TLSv1.2", "SunJSSE");
//
//        sc.init(null,
//                new TrustManager[]{new MyX509TrustManager()},
//                new java.security.SecureRandom());
//
//        SSLSocketFactory ssf = sc.getSocketFactory();
//        return ssf;
//    }

    private static java.net.Proxy getProxy() {
        java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
        return proxy;
    }

    private static String getReturn(HttpURLConnection connection)
            throws Exception {
        try (OutputStream os = connection.getOutputStream()) {
            os.write(REQUEST_BODY.getBytes());
            os.flush();
        }

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
