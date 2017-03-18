package com.xshengcn.diycode.data.remote;

import com.xshengcn.diycode.data.model.ImageResult;
import com.xshengcn.diycode.data.model.Token;
import com.xshengcn.diycode.data.model.common.User;
import com.xshengcn.diycode.data.model.news.News;
import com.xshengcn.diycode.data.model.news.NewsReply;
import com.xshengcn.diycode.data.model.news.Node;
import com.xshengcn.diycode.data.model.project.Project;
import com.xshengcn.diycode.data.model.site.SiteCollection;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.data.model.topic.TopicContent;
import com.xshengcn.diycode.data.model.topic.TopicNode;
import com.xshengcn.diycode.data.model.topic.TopicReply;
import com.xshengcn.diycode.data.model.user.Notification;
import com.xshengcn.diycode.data.model.user.NotificationUnread;
import com.xshengcn.diycode.data.model.user.UserDetail;
import com.xshengcn.diycode.data.model.user.UserReply;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DiyCodeService {

    String AUTH_URL = "https://www.diycode.cc/oauth/token";
    String BASE_URL = "https://www.diycode.cc/api/v3/";

    interface Params {
        String CLIENT_ID = "client_id";
        String CLIENT_SECRET = "client_secret";
        String GRANT_TYPE = "grant_type";
        String USERNAME = "username";
        String PASSWORD = "password";
        String REFRESH_TOKEN = "refreshToken";

        String NODE_ID = "node_id";
        String OFFSET = "offset";
        String LIMIT = "limit";
        String ID = "id";
        String TYPE = "type";

        String TITLE = "title";
        String BODY = "body";

        String LOGIN = "login";
    }

    /**
     * 获取token
     */
    @POST(AUTH_URL)
    @FormUrlEncoded
    Observable<Token> getToken(@Field(Params.CLIENT_ID) String clientId,
                               @Field(Params.CLIENT_SECRET) String clientSecret,
                               @Field(Params.GRANT_TYPE) String grantType,
                               @Field(Params.USERNAME) String username,
                               @Field(Params.PASSWORD) String password);

    /**
     * 刷新token
     */
    @POST(AUTH_URL)
    @FormUrlEncoded
    Observable<Token> refreshToken(@Field(Params.CLIENT_ID) String clientId,
                                   @Field(Params.CLIENT_SECRET) String clientSecret,
                                   @Field(Params.GRANT_TYPE) String grantType,
                                   @Field(Params.REFRESH_TOKEN) String refreshToken);

    @GET("news/nodes.json")
    Observable<List<Node>> getNewsNodes();

    @GET("news.json")
    Observable<List<News>> getNewses(@Query(Params.NODE_ID) String nodeId,
                                     @Query(Params.OFFSET) Integer offset,
                                     @Query(Params.LIMIT) Integer limit);

    @GET("news/{id}/replies.json")
    Observable<List<NewsReply>> getNewsReplies(@Path(Params.ID) Integer newsId,
                                               @Query(Params.OFFSET) Integer offset,
                                               @Query(Params.LIMIT) Integer limit);

    @GET("topics.json")
    Observable<List<Topic>> getTopics(@Query(Params.TYPE) String type,
                                      @Query(Params.NODE_ID) Integer nodeId,
                                      @Query(Params.OFFSET) Integer offset,
                                      @Query(Params.LIMIT) Integer limit);

    @POST("topics.json")
    @FormUrlEncoded
    Observable<Topic> createTopic(@Field(Params.NODE_ID) Integer nodeId,
                                  @Field(Params.TITLE) String title,
                                  @Field(Params.BODY) String body);

    @GET("topics/{id}.json")
    Observable<TopicContent> getTopicDetail(@Path(Params.ID) Integer id);

    @GET("topics/{id}/replies.json")
    Observable<List<TopicReply>> getTopicReplies(@Path(Params.ID) Integer id,
                                                 @Query(Params.OFFSET) Integer offset,
                                                 @Query(Params.LIMIT) Integer limit);

    @POST("topics/{id}/replies.json")
    @FormUrlEncoded
    Observable<TopicReply> sendReply(@Path(Params.ID) Integer id,
                                     @Field(Params.BODY) String body);

    @GET("users/me.json")
    Observable<UserDetail> getMe();

    /**
     * 获取项目列表
     */
    @GET("projects.json")
    Observable<List<Project>> getProjects(@Query(Params.NODE_ID) Integer nodeId,
                                          @Query(Params.OFFSET) Integer offset,
                                          @Query(Params.LIMIT) Integer limit);

    /**
     * 获得未读通知数量
     */
    @GET("notifications/unread_count.json")
    Observable<NotificationUnread> getNotificationsUnreadCount();

    /**
     * 当前用户的某个通知
     */
    @GET("notifications.json")
    Observable<List<Notification>> getNotifications(@Query(Params.OFFSET) Integer offset,
                                                    @Query(Params.LIMIT) Integer limit);

    @GET("users/{login}.json")
    Observable<UserDetail> getUserDetail(@Path(Params.LOGIN) String userLogin);

    /**
     * 用户收藏的话题列表
     */
    @GET("users/{login}/favorites.json")
    Observable<List<Topic>> getUserFavorites(@Path(Params.LOGIN) String userLogin,
                                             @Query(Params.OFFSET) Integer offset,
                                             @Query(Params.LIMIT) Integer limit);

    /**
     * 获取用户创建的话题列表
     */
    @GET("users/{login}/topics.json")
    Observable<List<Topic>> getUserTopics(@Path(Params.LOGIN) String userLogin,
                                          @Query(Params.OFFSET) Integer offset,
                                          @Query(Params.LIMIT) Integer limit);

    /**
     * 用户的关注者列表
     */
    @GET("users/{login}/followers.json")
    Observable<List<User>> getUserFollowers(@Path(Params.LOGIN) String userLogin,
                                            @Query(Params.OFFSET) Integer offset,
                                            @Query(Params.LIMIT) Integer limit);

    /**
     * 用户正在关注的人
     */
    @GET("users/{login}/followers.json")
    Observable<List<User>> getUserFollowing(@Path(Params.LOGIN) String userLogin,
                                            @Query(Params.OFFSET) Integer offset,
                                            @Query(Params.LIMIT) Integer limit);

    @GET("users/{login}/replies.json")
    Observable<List<UserReply>> getUserReplies(@Path(Params.LOGIN) String userLogin,
                                               @Query(Params.OFFSET) Integer offset,
                                               @Query(Params.LIMIT) Integer limit);

    @Multipart
    @POST("photos.json")
    Observable<ImageResult> uploadPhoto(@Part MultipartBody.Part file);

    /**
     * 获取话题node列表
     */
    @GET("nodes.json")
    Observable<List<TopicNode>> getTopicNodes();

    /**
     * 获取酷站
     */
    @GET("sites.json")
    Observable<List<SiteCollection>> getSites();
}
