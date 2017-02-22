package com.xshengcn.diycode.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xshengcn.diycode.BuildConfig;
import com.xshengcn.diycode.Constants;
import com.xshengcn.diycode.entity.Token;
import com.xshengcn.diycode.entity.news.News;
import com.xshengcn.diycode.entity.news.NewsReply;
import com.xshengcn.diycode.entity.topic.Topic;
import com.xshengcn.diycode.entity.topic.TopicContent;
import com.xshengcn.diycode.entity.topic.TopicReply;
import com.xshengcn.diycode.entity.user.NotificationUnread;
import com.xshengcn.diycode.entity.user.UserDetail;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DiyCodeClient {

  public static final int PAGE_LIMIT = 30;
  public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS:SZ";

  private final DiyCodeService service;

  public DiyCodeClient(OkHttpClient client) {
    Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
    Retrofit retrofit = new Retrofit.Builder().client(client)
        .baseUrl(DiyCodeService.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
    service = retrofit.create(DiyCodeService.class);
  }

  public Observable<Token> login(String username, String password) {
    return service.login(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET,
        Constants.GRANT_TYPE_PASSWORD, username, password).subscribeOn(Schedulers.io());
  }

  public Observable<List<Topic>> getTopics(int offset) {
    return service.getTopics(null, null, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
  }

  public Observable<List<News>> getAllNewses(Integer offset) {
    return getNewses(null, offset);
  }

  public Observable<List<News>> getNewses(String nodeId, Integer offset) {
    return service.getNewses(nodeId, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
  }

  public Observable<List<NewsReply>> getNewsReplies(int newsId, int offset) {
    return service.getNewsReplies(newsId, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
  }

  public Observable<List<Topic>> getUserTopics(String userLogin, int offset) {
    return service.getUserTopics(userLogin, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
  }

  public Observable<List<Topic>> getUserFavorites(String userLogin, int offset) {
    return service.getUserFavorites(userLogin, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
  }

  public Observable<Object> uploadPhoto() {
    File file = new File("/sdcard/DCIM/Camera/IMG_20170210_225912_01_01_01.jpg");
    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
    return service.uploadPhoto(
        MultipartBody.Part.createFormData("file", file.getName(), requestFile))
        .subscribeOn(Schedulers.io());
  }

  public Observable<UserDetail> getMe() {
    return service.getMe().subscribeOn(Schedulers.io());
  }

  public Observable<NotificationUnread> getNotificationsUnreadCount() {
    return service.getNotificationsUnreadCount().subscribeOn(Schedulers.io());
  }

  public Observable<Object> getNotifications(int offset) {
    return service.getNotifications(offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
  }

  public Observable<TopicContent> getTopicDetail(int id) {
    return service.getTopicDetail(id).subscribeOn(Schedulers.io());
  }

  public Observable<List<TopicReply>> getTopicReplies(int topicId, int offset) {
    return service.getTopicReplies(topicId, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
  }
}
