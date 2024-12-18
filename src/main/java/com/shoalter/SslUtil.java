package com.shoalter;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class SslUtil {

    public static  void trustAll() {
        SSLContext ctx;

        // 信任所有憑證
        TrustManager[] trustAllCerts = new X509TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null; // 不需要任何特定的受信任憑證。
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // 空實作檢查 Client 端憑證，接受所有 Client 憑證
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // 空實作檢查 Server 端憑證，接受所有 Server端憑證
            }
        }};

        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, trustAllCerts, null);  //不需要 KeyManager，設定為自定義的 TrustManager，不需要 SecureRandom
            SSLContext.setDefault(ctx); // 將自定義的 SSLContext 設定為全域預設的 SSL 設定。
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
