package com.xshengcn.diycode.data;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xshengcn.diycode.BuildConfig;
import com.xshengcn.diycode.Constants;
import com.xshengcn.diycode.data.model.ImageResult;
import com.xshengcn.diycode.data.model.Token;
import com.xshengcn.diycode.data.model.news.News;
import com.xshengcn.diycode.data.model.news.NewsReply;
import com.xshengcn.diycode.data.model.project.Project;
import com.xshengcn.diycode.data.model.site.Site;
import com.xshengcn.diycode.data.model.site.SiteCollection;
import com.xshengcn.diycode.data.model.site.SiteHeaderItem;
import com.xshengcn.diycode.data.model.site.SiteItem;
import com.xshengcn.diycode.data.model.site.SiteListItem;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.data.model.topic.TopicContent;
import com.xshengcn.diycode.data.model.topic.TopicNode;
import com.xshengcn.diycode.data.model.topic.TopicNodeCategory;
import com.xshengcn.diycode.data.model.topic.TopicReply;
import com.xshengcn.diycode.data.model.user.Notification;
import com.xshengcn.diycode.data.model.user.NotificationUnread;
import com.xshengcn.diycode.data.model.user.UserDetail;
import com.xshengcn.diycode.data.model.user.UserReply;
import com.xshengcn.diycode.data.remote.DiyCodeService;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
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

    private final DiyCodeService mService;

    public DataManager(OkHttpClient client) {
        Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(DiyCodeService.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mService = retrofit.create(DiyCodeService.class);
    }

    public Observable<Token> login(String username, String password) {
        return mService.getToken(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET,
                Constants.GRANT_TYPE_PASSWORD, username, password).subscribeOn(Schedulers.io());
    }

    public Observable<List<Topic>> getTopics(int offset) {
        return mService.getTopics(null, null, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
    }

    public Observable<Topic> createTopic(int nodeId, String title, String body) {
        return mService.createTopic(nodeId, title, body).subscribeOn(Schedulers.io());
    }

    public Observable<List<News>> getAllNewses(Integer offset) {
        return getNewses(null, offset);
    }

    public Observable<List<News>> getNewses(String nodeId, Integer offset) {
        return mService.getNewses(nodeId, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
    }

    public Observable<List<NewsReply>> getNewsReplies(int newsId, int offset) {
        return mService.getNewsReplies(newsId, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
    }

    public Observable<List<Topic>> getUserTopics(String userLogin, int offset) {
        return mService.getUserTopics(userLogin, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
    }

    public Observable<List<Topic>> getUserFavorites(String userLogin, int offset) {
        return mService.getUserFavorites(userLogin, offset, PAGE_LIMIT)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<UserReply>> getUserReplies(String userLogin, int offset) {
        return mService.getUserReplies(userLogin, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
    }

    public Observable<UserDetail> getMe() {
        return mService.getMe().subscribeOn(Schedulers.io());
    }

    public Observable<NotificationUnread> getNotificationsUnreadCount() {
        return mService.getNotificationsUnreadCount().subscribeOn(Schedulers.io());
    }

    public Observable<List<Notification>> getNotifications(int offset) {
        return mService.getNotifications(offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
    }

    public Observable<TopicContent> getTopicDetail(int id) {
        return mService.getTopicDetail(id).subscribeOn(Schedulers.io());
    }

    public Observable<List<TopicReply>> getTopicReplies(int topicId, int offset) {
        return mService.getTopicReplies(topicId, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
    }

    public Observable<TopicReply> sendReply(int id, String body) {
        return mService.sendReply(id, body).subscribeOn(Schedulers.io());
    }

    public Observable<ImageResult> uploadPhoto(String filePath) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return mService.uploadPhoto(
                MultipartBody.Part.createFormData("file", file.getName(), requestFile))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Map<TopicNodeCategory, List<TopicNode>>> getTopicNodes() {
        return mService.getTopicNodes()
                .subscribeOn(Schedulers.io())
                .map(this::getTopicNodeCategoryListMap);
    }

    @NonNull
    private Map<TopicNodeCategory, List<TopicNode>> getTopicNodeCategoryListMap(
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

            Collections.sort(map.get(category), (o1, o2) -> {
                int x = o1.sort;
                int y = o2.sort;
                return (x < y) ? -1 : ((x == y) ? 0 : 1);
            });
        }
        return map;
    }

    public Observable<List<Project>> getProjects(int offset) {
        return mService.getProjects(null, offset, PAGE_LIMIT).subscribeOn(Schedulers.io());
    }

    public Observable<List<SiteListItem>> getSites() {
        return mService.getSites().subscribeOn(Schedulers.io()).map(this::getSiteListItems);
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
