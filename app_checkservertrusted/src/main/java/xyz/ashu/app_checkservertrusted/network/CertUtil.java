package xyz.ashu.app_checkservertrusted.network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * <pre>
 * author : zhouyang
 * time   : 2019/09/05
 * desc   :
 * version:
 * </pre>
 */
public class CertUtil {
    public static String getSHA256FromSert(X509Certificate cert) {
        String hexString = null;
        try {
            //加密算法的类，这里的参数可以使MD4,MD5等加密算法
            MessageDigest md = MessageDigest.getInstance("SHA256");
            //获得公钥
            byte[] publicKey = md.digest(cert.getEncoded());
            //字节到十六进制的格式转换
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();
    }


    public static String getSHA256FromSert2(X509Certificate cert) {
        String result = "";

        try {
            byte[] encodedSPKI = cert.getPublicKey().getEncoded();
            MessageDigest md = MessageDigest.getInstance("SHA256");
            byte[] digest = md.digest(encodedSPKI);
            Base64.Encoder encoder = Base64.getEncoder();
            result = encoder.encodeToString(digest);
        } catch (Exception e) {

        }

        return result;
    }
}
