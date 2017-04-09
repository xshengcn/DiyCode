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
import com.xshengcn.diycode.data.model.topic.TopicDetail;
import com.xshengcn.diycode.data.model.topic.TopicNode;
import com.xshengcn.diycode.data.model.topic.TopicReply;
import com.xshengcn.diycode.data.model.user.Notification;
import com.xshengcn.diycode.data.model.user.NotificationUnread;
import com.xshengcn.diycode.data.model.user.UserDetail;
import com.xshengcn.diycode.data.model.user.UserReply;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
        String PLATFORM = "platform";
        String TOKEN = "token";

        String NODE_ID = "node_id";
        String OFFSET = "offset";
        String LIMIT = "limit";
        String ID = "id";
        String TYPE = "type";

        String TITLE = "title";
        String BODY = "body";

        String LOGIN = "login";
        String AUTHORIZATION = "AUTHORIZATION";
    }

    /**
     * 获取token
     */
    @POST(AUTH_URL)
    @FormUrlEncoded
    Single<Token> getToken(@Field(Params.CLIENT_ID) String clientId,
            @Field(Params.CLIENT_SECRET) String clientSecret,
            @Field(Params.GRANT_TYPE) String grantType,
            @Field(Params.USERNAME) String username,
            @Field(Params.PASSWORD) String password);

    /**
     * 刷新token
     */
    @POST(AUTH_URL)
    @FormUrlEncoded
    Single<Token> refreshToken(@Field(Params.CLIENT_ID) String clientId,
            @Field(Params.CLIENT_SECRET) String clientSecret,
            @Field(Params.GRANT_TYPE) String grantType,
            @Field(Params.REFRESH_TOKEN) String refreshToken);

    @POST("devices.json")
    Single<Object> pushDeviceInfo(@Query(Params.PLATFORM) String platform,
            @Query(Params.TOKEN) String token);

    @DELETE("devices.json")
    Single<Object> deleteDeviceInfo(@Query(Params.PLATFORM) String platform,
            @Query(Params.TOKEN) String token);

    @GET("news/nodes.json")
    Single<List<Node>> getNewsNodes();

    @GET("news.json")
    Single<List<News>> getNewses(@Header(Params.AUTHORIZATION) String header,
            @Query(Params.NODE_ID) String nodeId,
            @Query(Params.OFFSET) Integer offset,
            @Query(Params.LIMIT) Integer limit);

    @GET("news/{id}/replies.json")
    Single<List<NewsReply>> getNewsReplies(@Header(Params.AUTHORIZATION) String header,
            @Path(Params.ID) Integer newsId,
            @Query(Params.OFFSET) Integer offset,
            @Query(Params.LIMIT) Integer limit);

    @GET("topics.json")
    Single<List<Topic>> getTopics(@Header(Params.AUTHORIZATION) String header,
            @Query(Params.TYPE) String type,
            @Query(Params.NODE_ID) Integer nodeId,
            @Query(Params.OFFSET) Integer offset,
            @Query(Params.LIMIT) Integer limit);

    @POST("topics.json")
    @FormUrlEncoded
    Single<TopicDetail> createTopic(@Header(Params.AUTHORIZATION) String header,
            @Field(Params.NODE_ID) Integer nodeId,
            @Field(Params.TITLE) String title,
            @Field(Params.BODY) String body);

    @GET("topics/{id}.json")
    Single<TopicDetail> getTopicDetail(@Header(Params.AUTHORIZATION) String header,
            @Path(Params.ID) Integer id);

    @GET("topics/{id}/replies.json")
    Single<List<TopicReply>> getTopicReplies(@Header(Params.AUTHORIZATION) String header,
            @Path(Params.ID) Integer id,
            @Query(Params.OFFSET) Integer offset,
            @Query(Params.LIMIT) Integer limit);

    @POST("topics/{id}/replies.json")
    @FormUrlEncoded
    Single<TopicReply> publishComment(@Header(Params.AUTHORIZATION) String header,
            @Path(Params.ID) Integer id,
            @Field(Params.BODY) String body);

    @GET("users/me.json")
    Single<UserDetail> getMe(@Header(Params.AUTHORIZATION) String header);

    /**
     * 获取项目列表
     */
    @GET("projects.json")
    Single<List<Project>> getProjects(@Query(Params.NODE_ID) Integer nodeId,
            @Query(Params.OFFSET) Integer offset,
            @Query(Params.LIMIT) Integer limit);

    /**
     * 获得未读通知数量
     */
    @GET("notifications/unread_count.json")
    Single<NotificationUnread> getNotificationsUnreadCount(
            @Header(Params.AUTHORIZATION) String header);

    /**
     * 当前用户的某个通知
     */
    @GET("notifications.json")
    Single<List<Notification>> getNotifications(@Header(Params.AUTHORIZATION) String header,
            @Query(Params.OFFSET) Integer offset,
            @Query(Params.LIMIT) Integer limit);

    @GET("users/{login}.json")
    Single<UserDetail> getUserDetail(@Path(Params.LOGIN) String userLogin);

    /**
     * 用户收藏的话题列表
     */
    @GET("users/{login}/favorites.json")
    Single<List<Topic>> getUserFavorites(@Header(Params.AUTHORIZATION) String header,
            @Path(Params.LOGIN) String userLogin,
            @Query(Params.OFFSET) Integer offset,
            @Query(Params.LIMIT) Integer limit);

    /**
     * 获取用户创建的话题列表
     */
    @GET("users/{login}/topics.json")
    Single<List<Topic>> getUserTopics(@Header(Params.AUTHORIZATION) String header,
            @Path(Params.LOGIN) String userLogin,
            @Query(Params.OFFSET) Integer offset,
            @Query(Params.LIMIT) Integer limit);

    /**
     * 用户的关注者列表
     */
    @GET("users/{login}/followers.json")
    Single<List<User>> getUserFollowers(@Header(Params.AUTHORIZATION) String header,
            @Path(Params.LOGIN) String userLogin,
            @Query(Params.OFFSET) Integer offset,
            @Query(Params.LIMIT) Integer limit);

    /**
     * 用户正在关注的人
     */
    @GET("users/{login}/followers.json")
    Single<List<User>> getUserFollowing(@Header(Params.AUTHORIZATION) String header,
            @Path(Params.LOGIN) String userLogin,
            @Query(Params.OFFSET) Integer offset,
            @Query(Params.LIMIT) Integer limit);

    @GET("users/{login}/replies.json")
    Single<List<UserReply>> getUserReplies(@Header(Params.AUTHORIZATION) String header,
            @Path(Params.LOGIN) String userLogin,
            @Query(Params.OFFSET) Integer offset,
            @Query(Params.LIMIT) Integer limit);

    @Multipart
    @POST("photos.json")
    Single<ImageResult> uploadPhoto(@Header(Params.AUTHORIZATION) String header,
            @Part MultipartBody.Part file);

    /**
     * 获取话题node列表
     */
    @GET("nodes.json")
    Single<List<TopicNode>> getTopicNodes();

    /**
     * 获取酷站
     */
    @GET("sites.json")
    Single<List<SiteCollection>> getSites();
}
