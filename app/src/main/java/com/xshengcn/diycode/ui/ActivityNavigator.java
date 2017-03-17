package com.xshengcn.diycode.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.model.topic.Topic;
import com.xshengcn.diycode.injection.PreActivity;
import com.xshengcn.diycode.ui.activity.LoginActivity;
import com.xshengcn.diycode.ui.activity.NotificationActivity;
import com.xshengcn.diycode.ui.activity.ReplyActivity;
import com.xshengcn.diycode.ui.activity.SearchActivity;
import com.xshengcn.diycode.ui.activity.SiteActivity;
import com.xshengcn.diycode.ui.activity.TopicCreatorActivity;
import com.xshengcn.diycode.ui.activity.TopicDetailActivity;
import com.xshengcn.diycode.ui.activity.UserActivity;
import com.xshengcn.diycode.ui.activity.UserFavoriteActivity;
import com.xshengcn.diycode.ui.activity.UserReplyActivity;
import com.xshengcn.diycode.ui.activity.UserTopicActivity;
import javax.inject.Inject;

@PreActivity public final class ActivityNavigator {

  private final DiyCodePrefs prefs;
  private final AppCompatActivity activity;

  @Inject public ActivityNavigator(DiyCodePrefs prefs, AppCompatActivity activity) {
    this.prefs = prefs;
    this.activity = activity;
  }

  public void showUserReplies() {
    if (prefs.getToken() == null) {
      showLogin();
      return;
    }
    UserReplyActivity.start(activity, prefs.getUser().login);
  }

  public void showUserFavorites() {
    if (prefs.getToken() == null) {
      showLogin();
      return;
    }
    UserFavoriteActivity.start(activity, prefs.getUser().login);
  }

  public void showUserTopics() {
    if (prefs.getToken() == null) {
      showLogin();
      return;
    }
    UserTopicActivity.start(activity, prefs.getUser().login);
  }

  public void showUser() {
    if (prefs.getToken() == null) {
      showLogin();
      return;
    }
    UserActivity.start(activity);
  }

  public void showLogin() {
    LoginActivity.start(activity);
  }

  public void showSearch() {
    SearchActivity.start(activity);
  }

  public void showNotification() {
    if (prefs.getToken() == null) {
      showLogin();
      return;
    }
    NotificationActivity.start(activity);
  }

  public void showReply(@NonNull String title, @NonNull int id) {
    if (prefs.getToken() == null) {
      showLogin();
      return;
    }
    ReplyActivity.start(activity, title, id);
  }

  public void showCreateTopic() {
    if (prefs.getToken() == null) {
      showLogin();
      return;
    }
    TopicCreatorActivity.start(activity);
  }

  public void showSite() {
    SiteActivity.start(activity);
  }

  public void showTopicDetail(@NonNull Topic topic) {
    TopicDetailActivity.start(activity, topic);
  }
}
