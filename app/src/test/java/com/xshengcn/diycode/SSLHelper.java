package com.xshengcn.diycode;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okio.Buffer;

public class SSLHelper {


  public X509TrustManager trustManagerForCertificates()
      throws GeneralSecurityException {
    InputStream in = trustedCertificatesInputStream();
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
    if (certificates.isEmpty()) {
      throw new IllegalArgumentException("expected non-empty set of trusted certificates");
    }

    // Put the certificates a key store.
    char[] password = "password".toCharArray(); // Any password will work.
    KeyStore keyStore = newEmptyKeyStore(password);
    int index = 0;
    for (Certificate certificate : certificates) {
      String certificateAlias = Integer.toString(index++);
      keyStore.setCertificateEntry(certificateAlias, certificate);
    }

    // Use it to build an X509 trust manager.
    KeyManagerFactory keyManagerFactory =
        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, password);
    TrustManagerFactory trustManagerFactory =
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(keyStore);
    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
    if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
      throw new IllegalStateException(
          "Unexpected default trust managers:" + Arrays.toString(trustManagers));
    }
    return (X509TrustManager) trustManagers[0];
  }


  private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
    try {
      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      InputStream in = null; // By convention, 'null' creates an empty key store.
      keyStore.load(in, password);
      return keyStore;
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  public InputStream trustedCertificatesInputStream() {
    // PEM files for root certificates of Comodo and Entrust. These two CAs are sufficient to view
    // https://publicobject.com (Comodo) and https://squareup.com (Entrust). But they aren't
    // sufficient to connect to most HTTPS sites including https://godaddy.com and https://visa.com.
    // Typically developers will need to get a PEM file from their organization's TLS administrator.

    String dstRootCertificateAuthority = ""
        + "-----BEGIN CERTIFICATE-----\n"
        + "MIIFCDCCA/CgAwIBAgISA3lDNAKHHOSktO9YjhjXlxcJMA0GCSqGSIb3DQEBCwUA\n"
        + "MEoxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1MZXQncyBFbmNyeXB0MSMwIQYDVQQD\n"
        + "ExpMZXQncyBFbmNyeXB0IEF1dGhvcml0eSBYMzAeFw0xNzAxMzExNTAwMDBaFw0x\n"
        + "NzA1MDExNTAwMDBaMBUxEzARBgNVBAMTCmRpeWNvZGUuY2MwggEiMA0GCSqGSIb3\n"
        + "DQEBAQUAA4IBDwAwggEKAoIBAQDvgt4sb6NfzBINS5+zBNKOydPxvc5ZhHRaBz2Y\n"
        + "Y4sJxnfG9g5dvYNZ0Xzy3lZDWErk1vwYSptlSTTJHfZS5dWrBB1PtC8/IlQXh/ep\n"
        + "fv6pqL9wxP0t/ZFua5PLUezAI3O/cco5K/YI3ydZPINq8tBmbWfJOIKtY7/5mCmj\n"
        + "fbFvGXyEZiTn8y9umCh9DH08jaqf3lrdVW6IUeA9ecFTycF3klpKxnNbegPZIezJ\n"
        + "rY2AQwP3iLOu6tmaslyNI5eUuC9i5c47ovX2JTTubtQpxwCJsQObuye6CMFOB4si\n"
        + "FXy/IuOMUeQdLS4Rrbzy/OdyxU9u7Iiy9SnnTlTKXtIDoKHtAgMBAAGjggIbMIIC\n"
        + "FzAOBgNVHQ8BAf8EBAMCBaAwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMC\n"
        + "MAwGA1UdEwEB/wQCMAAwHQYDVR0OBBYEFF9XnL/VkmiC5dPx6NFPZrcx1rYyMB8G\n"
        + "A1UdIwQYMBaAFKhKamMEfd265tE5t6ZFZe/zqOyhMHAGCCsGAQUFBwEBBGQwYjAv\n"
        + "BggrBgEFBQcwAYYjaHR0cDovL29jc3AuaW50LXgzLmxldHNlbmNyeXB0Lm9yZy8w\n"
        + "LwYIKwYBBQUHMAKGI2h0dHA6Ly9jZXJ0LmludC14My5sZXRzZW5jcnlwdC5vcmcv\n"
        + "MCUGA1UdEQQeMByCCmRpeWNvZGUuY2OCDnd3dy5kaXljb2RlLmNjMIH+BgNVHSAE\n"
        + "gfYwgfMwCAYGZ4EMAQIBMIHmBgsrBgEEAYLfEwEBATCB1jAmBggrBgEFBQcCARYa\n"
        + "aHR0cDovL2Nwcy5sZXRzZW5jcnlwdC5vcmcwgasGCCsGAQUFBwICMIGeDIGbVGhp\n"
        + "cyBDZXJ0aWZpY2F0ZSBtYXkgb25seSBiZSByZWxpZWQgdXBvbiBieSBSZWx5aW5n\n"
        + "IFBhcnRpZXMgYW5kIG9ubHkgaW4gYWNjb3JkYW5jZSB3aXRoIHRoZSBDZXJ0aWZp\n"
        + "Y2F0ZSBQb2xpY3kgZm91bmQgYXQgaHR0cHM6Ly9sZXRzZW5jcnlwdC5vcmcvcmVw\n"
        + "b3NpdG9yeS8wDQYJKoZIhvcNAQELBQADggEBAF0v4vN/Cx0wu61xRUN75+66EwH4\n"
        + "EuF2xLTG+pxF515k2XbaAvpJldrgGYu2+ayuEamivBMEFcQLcWl3d484MkzjSbWe\n"
        + "RLEb6l/1bb1BWw4FvZg9UxpvXqdLjpYjg9oAtdHwiT4xsZAgr52mgI871Vlx/o4M\n"
        + "0nSHkxhFWvycR8Dn40KTSYu6EZ5wjIk/egXRW1myd/SWncs77iT7emrBFF+NcDED\n"
        + "baXpv4StEfdXXK//O1FvedWf9G6I1aaze5HSXxjJJxaeYN2vOALF3jlAe/qJNVQf\n"
        + "aDLIU6y55Crt0ziHoFvEFMHFxOmRblLbg3eAiLThoszyNhd9mlcW8KHP5Y0=\n"
        + "-----END CERTIFICATE-----\n";
    return new Buffer().writeUtf8(dstRootCertificateAuthority).inputStream();
  }
}
