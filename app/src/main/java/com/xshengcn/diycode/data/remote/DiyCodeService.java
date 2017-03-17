package com.xshengcn.diycode.data.remote;

import android.support.annotation.StringDef;
import com.xshengcn.diycode.model.ImageResult;
import com.xshengcn.diycode.model.Token;
import com.xshengcn.diycode.model.common.User;
import com.xshengcn.diycode.model.news.News;
import com.xshengcn.diycode.model.news.NewsReply;
import com.xshengcn.diycode.model.news.Node;
import com.xshengcn.diycode.model.project.Project;
import com.xshengcn.diycode.model.site.SiteCollection;
import com.xshengcn.diycode.model.topic.Topic;
import com.xshengcn.diycode.model.topic.TopicContent;
import com.xshengcn.diycode.model.topic.TopicNode;
import com.xshengcn.diycode.model.topic.TopicReply;
import com.xshengcn.diycode.model.user.Notification;
import com.xshengcn.diycode.model.user.NotificationUnread;
import com.xshengcn.diycode.model.user.UserDetail;
import com.xshengcn.diycode.model.user.UserReply;
import io.reactivex.Observable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
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

  String TOPIC_LAST_ACTIVED = "last_actived";
  String TOPIC_RECENT = "recent";
  String TOPIC_NO_REPLY = "no_reply";
  String TOPIC_POPULAR = "popular";
  String TOPIC_EXCELLENT = "excellent";

  String AUTH_URL = "https://www.diycode.cc/oauth/token";
  String BASE_URL = "https://www.diycode.cc/api/v3/";

  /**
   * 获取token
   */
  @POST(AUTH_URL) @FormUrlEncoded Observable<Token> getToken(@Field("client_id") String client_id,
      @Field("client_secret") String client_secret, @Field("grant_type") String grant_type,
      @Field("username") String username, @Field("password") String password);

  /**
   * 刷新token
   */
  @POST(AUTH_URL) @FormUrlEncoded Observable<Token> refreshToken(
      @Field("client_id") String client_id, @Field("client_secret") String client_secret,
      @Field("grant_type") String grant_type, @Field("refresh_token") String refreshToken);

  @GET("news/nodes.json") Observable<List<Node>> getNewsNodes();

  @GET("news.json") Observable<List<News>> getNewses(@Query("node_id") String nodeId,
      @Query("offset") Integer offset, @Query("limit") Integer limit);

  @GET("news/{id}/replies.json") Observable<List<NewsReply>> getNewsReplies(
      @Path("id") Integer newsId, @Query("offset") Integer offset, @Query("limit") Integer limit);

  @GET("topics.json") Observable<List<Topic>> getTopics(@TopicType @Query("type") String type,
      @Query("node_id") Integer nodeId, @Query("offset") Integer offset,
      @Query("limit") Integer limit);

  @POST("topics.json") @FormUrlEncoded Observable<Topic> createTopic(
      @Field("node_id") Integer nodeId, @Field("title") String title, @Field("body") String body);

  @GET("topics/{id}.json") Observable<TopicContent> getTopicDetail(@Path("id") Integer id);

  @GET("topics/{id}/replies.json") Observable<List<TopicReply>> getTopicReplies(
      @Path("id") Integer id, @Query("offset") Integer offset, @Query("limit") Integer limit);

  @POST("topics/{id}/replies.json") @FormUrlEncoded Observable<TopicReply> sendReply(
      @Path("id") Integer id, @Field("body") String body);

  @GET("users/me.json") Observable<UserDetail> getMe();

  /**
   * 获取项目列表
   */
  @GET("projects.json") Observable<List<Project>> getProjects(@Query("node_id") Integer nodeId,
      @Query("offset") Integer offset, @Query("limit") Integer limit);

  /**
   * 获得未读通知数量
   */
  @GET("notifications/unread_count.json")
  Observable<NotificationUnread> getNotificationsUnreadCount();

  /**
   * 当前用户的某个通知
   */
  @GET("notifications.json") Observable<List<Notification>> getNotifications(
      @Query("offset") Integer offset, @Query("limit") Integer limit);

  @GET("users/{login}.json") Observable<UserDetail> getUserDetail(@Path("login") String userLogin);

  /**
   * 用户收藏的话题列表
   */
  @GET("users/{login}/favorites.json") Observable<List<Topic>> getUserFavorites(
      @Path("login") String userLogin, @Query("offset") Integer offset,
      @Query("limit") Integer limit);

  /**
   * 获取用户创建的话题列表
   */
  @GET("users/{login}/topics.json") Observable<List<Topic>> getUserTopics(
      @Path("login") String userLogin, @Query("offset") Integer offset,
      @Query("limit") Integer limit);

  /**
   * 用户的关注者列表
   */
  @GET("users/{login}/followers.json") Observable<List<User>> getUserFollowers(
      @Path("login") String userLogin, @Query("offset") Integer offset,
      @Query("limit") Integer limit);

  /**
   * 用户正在关注的人
   */
  @GET("users/{login}/followers.json") Observable<List<User>> getUserFollowing(
      @Path("login") String userLogin, @Query("offset") Integer offset,
      @Query("limit") Integer limit);

  @GET("users/{login}/replies.json") Observable<List<UserReply>> getUserReplies(
      @Path("login") String userLogin, @Query("offset") Integer offset,
      @Query("limit") Integer limit);

  @Multipart @POST("photos.json") Observable<ImageResult> uploadPhoto(
      @Part MultipartBody.Part file);

  /**
   * 获取话题node列表
   */
  @GET("nodes.json") Observable<List<TopicNode>> getTopicNodes();

  /**
   * 获取酷站
   */
  @GET("sites.json") Observable<List<SiteCollection>> getSites();

  @Retention(RetentionPolicy.SOURCE)
  @StringDef({ TOPIC_LAST_ACTIVED, TOPIC_RECENT, TOPIC_NO_REPLY, TOPIC_POPULAR, TOPIC_EXCELLENT })
  @interface TopicType {
  }
}
