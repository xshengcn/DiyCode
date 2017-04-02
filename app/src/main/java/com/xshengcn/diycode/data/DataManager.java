package com.xshengcn.diycode.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.xshengcn.diycode.BuildConfig;
import com.xshengcn.diycode.data.model.ImageResult;
import com.xshengcn.diycode.data.model.Token;
import com.xshengcn.diycode.data.model.news.News;
import com.xshengcn.diycode.data.model.news.NewsReply;
import com.xshengcn.diycode.data.model.project.Project;
import com.xshengcn.diycode.data.model.site.SiteCollection;
import com.xshengcn.diycode.data.model.site.SiteHeaderItem;
import com.xshengcn.diycode.data.model.site.SiteItem;
import com.xshengcn.diycode.data.model.site.SiteListItem;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.data.model.topic.TopicAndReplies;
import com.xshengcn.diycode.data.model.topic.TopicDetail;
import com.xshengcn.diycode.data.model.topic.TopicNode;
import com.xshengcn.diycode.data.model.topic.TopicNodeCategory;
import com.xshengcn.diycode.data.model.topic.TopicReply;
import com.xshengcn.diycode.data.model.user.Notification;
import com.xshengcn.diycode.data.model.user.NotificationUnread;
import com.xshengcn.diycode.data.model.user.UserDetail;
import com.xshengcn.diycode.data.model.user.UserReply;
import com.xshengcn.diycode.data.remote.DiyCodeService;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataManager {
    private static final String GRANT_TYPE_PASSWORD = "password";

    public static final int PAGE_LIMIT = 30;
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS:SZ";

    private final DiyCodeService mService;
    private final PreferencesHelper mPreferencesHelper;

    public DataManager(@NonNull OkHttpClient client, @NonNull PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
        Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(DiyCodeService.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mService = retrofit.create(DiyCodeService.class);
    }

    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public String buildAuthorization() {
        Token token = mPreferencesHelper.getToken();
        if (token != null) {
            return MessageFormat.format("{0} {1}", token.tokenType, token.accessToken);
        }
        return null;
    }

    public Observable<Token> login(String username, String password) {
        return mService.getToken(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET,
                GRANT_TYPE_PASSWORD, username, password).compose(applySchedulers());
    }

    public Observable<List<Topic>> getTopics(int offset) {
        return mService.getTopics(buildAuthorization(), null, null, offset, PAGE_LIMIT)
                .compose(applySchedulers());
    }

    public Observable<TopicDetail> createTopic(int nodeId, String title, String body) {
        return mService.createTopic(buildAuthorization(), nodeId, title, body)
                .compose(applySchedulers());
    }

    public Observable<List<News>> getAllNewses(Integer offset) {
        return getNewses(null, offset);
    }

    public Observable<List<News>> getNewses(String nodeId, Integer offset) {
        return mService.getNewses(buildAuthorization(), nodeId, offset, PAGE_LIMIT)
                .compose(applySchedulers());
    }

    public Observable<List<NewsReply>> getNewsReplies(int newsId, int offset) {
        return mService.getNewsReplies(buildAuthorization(), newsId, offset, PAGE_LIMIT)
                .compose(applySchedulers());
    }

    public Observable<List<Topic>> getUserTopics(String userLogin, int offset) {
        return mService.getUserTopics(buildAuthorization(), userLogin, offset, PAGE_LIMIT)
                .compose(applySchedulers());
    }

    public Observable<List<Topic>> getUserFavorites(String userLogin, int offset) {
        return mService.getUserFavorites(buildAuthorization(), userLogin, offset, PAGE_LIMIT)
                .compose(applySchedulers());
    }

    public Observable<List<UserReply>> getUserReplies(String userLogin, int offset) {
        return mService.getUserReplies(buildAuthorization(), userLogin, offset, PAGE_LIMIT)
                .compose(applySchedulers());
    }

    public Observable<UserDetail> getMe() {
        return mService.getMe(buildAuthorization()).compose(applySchedulers());
    }

    public Observable<NotificationUnread> getNotificationsUnreadCount() {
        return mService.getNotificationsUnreadCount(buildAuthorization())
                .compose(applySchedulers());
    }

    public Observable<List<Notification>> getNotifications(int offset) {
        return mService.getNotifications(buildAuthorization(), offset, PAGE_LIMIT)
                .compose(applySchedulers());
    }

    public Observable<TopicAndReplies> getTopicAndComments(int topicId) {
        Observable<TopicDetail> topicObservable = getTopicDetail(topicId);
        Observable<List<TopicReply>> reliesObservable = getTopicReplies(topicId, 0);
        return Observable.zip(topicObservable, reliesObservable, TopicAndReplies::new);
    }

    public Observable<TopicDetail> getTopicDetail(int id) {
        return mService.getTopicDetail(buildAuthorization(), id).compose(applySchedulers());
    }

    public Observable<List<TopicReply>> getTopicReplies(int topicId, int offset) {
        return mService.getTopicReplies(buildAuthorization(), topicId, offset, PAGE_LIMIT)
                .compose(applySchedulers());
    }

    public Observable<TopicReply> publishComment(int id, String body) {
        return mService.publishComment(buildAuthorization(), id, body).compose(applySchedulers());
    }

    public Observable<ImageResult> uploadPhoto(String filePath) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return mService.uploadPhoto(buildAuthorization(),
                MultipartBody.Part.createFormData("file", file.getName(), requestFile))
                .compose(applySchedulers());
    }

    public Observable<Map<TopicNodeCategory, List<TopicNode>>> getTopicNodes() {
        return mService.getTopicNodes()
                .compose(applySchedulers())
                .map(this::getTopicNodeCategoryListMap);
    }

    @NonNull
    private Map<TopicNodeCategory, List<TopicNode>> getTopicNodeCategoryListMap(
            @NonNull List<TopicNode> topicNodes) {
        ArrayMap<TopicNodeCategory, List<TopicNode>> map = new ArrayMap<>();
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
        return mService.getProjects(null, offset, PAGE_LIMIT).compose(applySchedulers());
    }

    public Observable<List<SiteListItem>> getSites() {
        return mService.getSites().compose(applySchedulers()).map(this::getSiteListItems);
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
            items.addAll(Stream.of(siteCollection.sites)
                    .map(SiteItem::new)
                    .collect(Collectors.toList()));
        }
        return items;
    }
}
