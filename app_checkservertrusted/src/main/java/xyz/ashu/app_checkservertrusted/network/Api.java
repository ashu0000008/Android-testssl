package xyz.ashu.app_checkservertrusted.network;

import android.util.Log;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 * author : zhouyang
 * time   : 2019/08/20
 * desc   :
 * version:
 * </pre>
 */
public class Api {
    private MyTrustManager mMyTrustManager;

    public void getCryptoPercent(final OnResponseListener listener) {
        String url = "https://ashu.xyz/percent/btc";
        OkHttpClient okHttpClient = getTrustAllClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != listener) {
                    listener.onResponse(response.body().string());
                }
            }
        });
    }


    public interface OnResponseListener {
        void onResponse(String response);
    }


    private OkHttpClient getTrustAllClient() {
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        mBuilder.sslSocketFactory(createSSLSocketFactory(), mMyTrustManager)
                .hostnameVerifier(new TrustAllHostnameVerifier())
        ;
        return mBuilder.build();
    }

    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            mMyTrustManager = new MyTrustManager();
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{mMyTrustManager}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return ssfFactory;
    }

    //实现X509TrustManager接口
    public class MyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            X509Certificate cert = chain[0];
            //检测证书是否合法，比如过期
            cert.checkValidity();

            //检测证书是不是目标证书
            String sha256 = CertUtil.getSHA256FromSert2(cert);
            Log.e("checkServerTrusted", "authType:" + authType + "----sha256:" + sha256);
            if (!"ZXlgxEjyJdFPhcbbXQ3VOiAQK3uYMUUI24yj6+2oIbg=".equals(sha256)) {
                throw new CertificateException("cert can not trusted!");
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    //实现HostnameVerifier接口
    private class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
