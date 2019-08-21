package xyz.ashu.testssl.network;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.ashu.demo11.R;
import xyz.ashu.testssl.MyApplication;

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
        SSLSocketFactory factory = createSSLSocketFactory();
        mBuilder.sslSocketFactory(factory)
                .hostnameVerifier(new TrustAllHostnameVerifier())
//                .certificatePinner(new CertificatePinner.Builder()
//                        .add("ashu.xyz", "sha256/ZXlgxEjyJdFPhcbbXQ3VOiAQK3uYMUUI24yj6+2oIbg=")
//                        .build())
        ;
        return mBuilder.build();
    }

    private SSLSocketFactory createSSLSocketFactory() {

        TrustManagerFactory trustManagerFactory;
        try {
            InputStream resourceStream = MyApplication.getContext().getResources().openRawResource(R.raw.cert);
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(resourceStream, null);

            String trustManagerAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            trustManagerFactory = TrustManagerFactory.getInstance(trustManagerAlgorithm);
            trustManagerFactory.init(keyStore);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
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
            Log.e("MyTrustManager verify:", hostname);
            return true;
        }
    }
}
