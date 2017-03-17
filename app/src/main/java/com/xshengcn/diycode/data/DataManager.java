package com.xshengcn.diycode.data;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xshengcn.diycode.BuildConfig;
import com.xshengcn.diycode.Constants;
import com.xshengcn.diycode.data.remote.DiyCodeService;
import com.xshengcn.diycode.model.ImageResult;
import com.xshengcn.diycode.model.Token;
import com.xshengcn.diycode.model.news.News;
import com.xshengcn.diycode.model.news.NewsReply;
import com.xshengcn.diycode.model.project.Project;
import com.xshengcn.diycode.model.site.Site;
import com.xshengcn.diycode.model.site.SiteCollection;
import com.xshengcn.diycode.model.site.SiteHeaderItem;
import com.xshengcn.diycode.model.site.SiteItem;
import com.xshengcn.diycode.model.site.SiteListItem;
import com.xshengcn.diycode.model.topic.Topic;
import com.xshengcn.diycode.model.topic.TopicContent;
import com.xshengcn.diycode.model.topic.TopicNode;
import com.xshengcn.diycode.model.topic.TopicNodeCategory;
import com.xshengcn.diycode.model.topic.TopicReply;
import com.xshengcn.diycode.model.user.Notification;
import com.xshengcn.diycode.model.user.NotificationUnread;
import com.xshengcn.diycode.model.user.UserDetail;
import com.xshengcn.diycode.model.user.UserReply;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataManager {

  public static final int PAGE_LIMIT = 30;
  public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS:SZ";

  private final DiyCodeService service;

  public DataManager(OkHttpClient client) {
    Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
    Retrofit retrofit = new Retrofit.Builder().client(client)
        .baseUrl(DiyCodeService.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
    service = retrofit.create(DiyCodeService.class);
  }

  public Observable<Token> login(String username, String password) {
    return service.getToken(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET,
        Constants.GRANT_TYPE_PASSWORD, username, password).subscribeOn(Schedulers.io());
  }

  public Observable<List<Topic>> getTopics(int offset) {
    return service.getTopics(null, null, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
  }

  public Observable<Topic> createTopic(int nodeId, String title, String body) {
    return service.createTopic(nodeId, title, body).subscribeOn(Schedulers.io());
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

  public Observable<List<UserReply>> getUserReplies(String userLogin, int offset) {
    return service.getUserReplies(userLogin, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
  }

  public Observable<UserDetail> getMe() {
    return service.getMe().subscribeOn(Schedulers.io());
  }

  public Observable<NotificationUnread> getNotificationsUnreadCount() {
    return service.getNotificationsUnreadCount().subscribeOn(Schedulers.io());
  }

  public Observable<List<Notification>> getNotifications(int offset) {
    return service.getNotifications(offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
  }

  public Observable<TopicContent> getTopicDetail(int id) {
    return service.getTopicDetail(id).subscribeOn(Schedulers.io());
  }

  public Observable<List<TopicReply>> getTopicReplies(int topicId, int offset) {
    return service.getTopicReplies(topicId, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
  }

  public Observable<TopicReply> sendReply(int id, String body) {
    return service.sendReply(id, body).subscribeOn(Schedulers.io());
  }

  public Observable<ImageResult> uploadPhoto(String filePath) {
    File file = new File(filePath);
    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
    return service.uploadPhoto(
        MultipartBody.Part.createFormData("file", file.getName(), requestFile))
        .subscribeOn(Schedulers.io());
  }

  public Observable<Map<TopicNodeCategory, List<TopicNode>>> getTopicNodes() {
    return service.getTopicNodes()
        .subscribeOn(Schedulers.io())
        .map(this::getTopicNodeCategoryListMap);
  }

  @NonNull private Map<TopicNodeCategory, List<TopicNode>> getTopicNodeCategoryListMap(
      @NonNull List<TopicNode> topicNodes) {
    LinkedHashMap<TopicNodeCategory, List<TopicNode>> map = new LinkedHashMap<>();
    topicNodes.sort((o1, o2) -> {
      int x = o1.sectionId;
      int y = o2.sectionId;
      return (x < y) ? -1 : ((x == y) ? 0 : 1);
    });
    for (TopicNode topicNode : topicNodes) {
      TopicNodeCategory category =
          new TopicNodeCategory(topicNode.sectionId, topicNode.sectionName);
      if (map.containsKey(category)) {
        map.get(category).add(topicNode);
      } else {
        List<TopicNode> nodes = new ArrayList<>();
        nodes.add(topicNode);
        map.put(category, nodes);
      }
    }
    for (TopicNodeCategory category : map.keySet()) {
      map.get(category).sort((o1, o2) -> {
        int x = o1.sort;
        int y = o2.sort;
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
      });
    }
    return map;
  }

  public Observable<List<Project>> getProjects(int offset) {
    return service.getProjects(null, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
  }

  public Observable<List<SiteListItem>> getSites() {
    return service.getSites().subscribeOn(Schedulers.io()).map(this::getSiteListItems);
  }

  private List<SiteListItem> getSiteListItems(List<SiteCollection> siteCollections) {
    if (siteCollections == null) {
      return null;
    }
    List<SiteListItem> items = new ArrayList<>();
    for (SiteCollection siteCollection : siteCollections) {
      items.add(new SiteHeaderItem(siteCollection.name, siteCollection.id));
      if (siteCollection.sites == null) {
        continue;
      }
      for (Site site : siteCollection.sites) {
        items.add(new SiteItem(site));
      }
    }
    return items;
  }
}
