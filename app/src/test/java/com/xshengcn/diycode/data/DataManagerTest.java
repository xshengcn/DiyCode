package com.xshengcn.diycode.data;

import com.xshengcn.diycode.SSLHelper;
import com.xshengcn.diycode.data.model.Token;
import com.xshengcn.diycode.data.model.news.News;
import com.xshengcn.diycode.data.model.news.NewsReply;
import com.xshengcn.diycode.data.model.project.Project;
import com.xshengcn.diycode.data.model.site.SiteListItem;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.data.model.topic.TopicReply;
import com.xshengcn.diycode.data.model.topic.TopicDetail;
import com.xshengcn.diycode.data.model.topic.TopicNode;
import com.xshengcn.diycode.data.model.topic.TopicNodeCategory;
import com.xshengcn.diycode.data.model.user.Notification;
import com.xshengcn.diycode.data.model.user.NotificationUnread;
import com.xshengcn.diycode.data.model.user.UserDetail;
import com.xshengcn.diycode.data.model.user.UserReply;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    private DataManager mDataManager;
    @Mock
    PreferencesHelper mPreferencesHelper;

    @BeforeClass
    public static void onlyOnce() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                schedulerCallable -> Schedulers.trampoline());
    }

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;
        SSLHelper sslHelper = new SSLHelper();
        try {
            trustManager = sslHelper.trustManagerForCertificates();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient =
                builder.sslSocketFactory(sslSocketFactory, trustManager).build();
        mDataManager = new DataManager(okHttpClient, mPreferencesHelper);
    }


    @Test
    public void login() throws Exception {
        // 测试登录失败
        TestObserver<Token> testObserver = mDataManager.login("abcd", "2222").test().await();
        testObserver.assertError(HttpException.class);
        testObserver.assertNotComplete();
    }

    @Test
    public void getTopics() throws Exception {
        TestObserver<List<Topic>> testObserver = mDataManager.getTopics(0).test().await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void createTopic() throws Exception {
        // 发帖失败 需要token
        TestObserver<TopicDetail> testObserver = mDataManager.createTopic(0, "title", "body")
                .test()
                .await();
        testObserver.assertError(HttpException.class);
        testObserver.assertNotComplete();
    }

    @Test
    public void getAllNewses() throws Exception {
        TestObserver<List<News>> testObserver = mDataManager.getAllNewses(0).test().await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void getNewses() throws Exception {
        TestObserver<List<News>> testObserver = mDataManager.getNewses(null, 0).test().await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void getNewsReplies() throws Exception {
        TestObserver<List<NewsReply>> testObserver = mDataManager.getNewsReplies(1, 0).test()
                .await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void getUserTopics() throws Exception {
        TestObserver<List<Topic>> testObserver = mDataManager.getUserTopics("xshengcn", 0).test()
                .await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void getUserFavorites() throws Exception {
        TestObserver<List<Topic>> testObserver = mDataManager.getUserFavorites("xshengcn", 0).test()
                .await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void getUserReplies() throws Exception {
        TestObserver<List<UserReply>> testObserver = mDataManager.getUserReplies("xshengcn", 0)
                .test().await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void getMe() throws Exception {
        // 需要token
        TestObserver<UserDetail> testObserver = mDataManager.getMe().test().await();
        testObserver.assertError(HttpException.class);
        testObserver.assertNotComplete();
    }

    @Test
    public void getNotificationsUnreadCount() throws Exception {
        TestObserver<NotificationUnread> testObserver = mDataManager.getNotificationsUnreadCount()
                .test().await();
        testObserver.assertError(HttpException.class);
        testObserver.assertNotComplete();
    }

    @Test
    public void getNotifications() throws Exception {
        TestObserver<List<Notification>> testObserver = mDataManager.getNotifications(0)
                .test().await();
        testObserver.assertError(HttpException.class);
        testObserver.assertNotComplete();
    }

    @Test
    public void getTopicDetail() throws Exception {
        TestObserver<TopicDetail> testObserver = mDataManager.getTopicDetail(4).test().await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void getTopicReplies() throws Exception {
        TestObserver<List<TopicReply>> testObserver = mDataManager.getTopicReplies(4, 0).test()
                .await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void sendReply() throws Exception {
        TestObserver<TopicReply> testObserver = mDataManager.publishComment(4, "body").test()
                .await();
        testObserver.assertError(HttpException.class);
        testObserver.assertNotComplete();
    }

    @Test
    public void getTopicNodes() throws Exception {
        TestObserver<Map<TopicNodeCategory, List<TopicNode>>> testObserver = mDataManager
                .getTopicNodes().test().await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void getProjects() throws Exception {
        TestObserver<List<Project>> testObserver = mDataManager.getProjects(0).test().await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    @Test
    public void getSites() throws Exception {
        TestObserver<List<SiteListItem>> testObserver = mDataManager.getSites().test().await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

}