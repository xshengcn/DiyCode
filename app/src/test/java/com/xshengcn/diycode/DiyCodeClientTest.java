package com.xshengcn.diycode;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.entity.Token;
import com.xshengcn.diycode.entity.topic.Topic;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import java.security.GeneralSecurityException;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;

public class DiyCodeClientTest {

  private DiyCodeClient client;

  @Before public void setup() throws Exception {
    X509TrustManager trustManager;
    SSLSocketFactory sslSocketFactory;
    SSLHelper sslHelper = new SSLHelper();
    try {
      trustManager = sslHelper.trustManagerForCertificates();
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, new TrustManager[] { trustManager }, null);
      sslSocketFactory = sslContext.getSocketFactory();
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }

    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    OkHttpClient okHttpClient = builder.sslSocketFactory(sslSocketFactory, trustManager).build();
    client = new DiyCodeClient(okHttpClient);
  }

  @Test public void testRxJava2() throws Exception {
    TestObserver<String> testObserver = Observable.just("1").test();
    testObserver.assertNoErrors();
    testObserver.assertComplete();
  }

  @Test public void testLogin() throws Exception {
    TestObserver<Token> testObserver =
        client.login("abcd", "2222").subscribeOn(Schedulers.single()).test().await();
    testObserver.assertError(HttpException.class);
    testObserver.assertNotComplete();
  }

  @Test public void testGetTopics() throws Exception {
    Observable<List<Topic>> observable = client.getTopics(1);
    TestObserver<List<Topic>> testObserver =
        observable.subscribeOn(Schedulers.single()).test().await();
    testObserver.assertNoErrors();
    testObserver.assertComplete();
  }
}