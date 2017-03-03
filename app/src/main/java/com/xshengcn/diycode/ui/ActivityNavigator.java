package com.xshengcn.diycode.ui;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.xshengcn.diycode.ui.login.LoginActivity;
import com.xshengcn.diycode.ui.notification.NotificationActivity;
import com.xshengcn.diycode.ui.reply.ReplyActivity;
import com.xshengcn.diycode.ui.search.SearchActivity;
import com.xshengcn.diycode.ui.user.UserActivity;
import com.xshengcn.diycode.ui.userfavorite.UserFavoriteActivity;
import com.xshengcn.diycode.ui.userreply.UserReplyActivity;
import com.xshengcn.diycode.ui.usertopic.UserTopicActivity;

public final class ActivityNavigator {

  public void showUserReplies(@NonNull Activity activity, @NonNull String user) {
    UserReplyActivity.start(activity, user);
  }

  public void showUserFavorites(@NonNull Activity activity, @NonNull String user) {
    UserFavoriteActivity.start(activity, user);
  }

  public void showUserTopics(@NonNull Activity activity, @NonNull String user) {
    UserTopicActivity.start(activity, user);
  }

  public void showUser(@NonNull Activity activity) {
    UserActivity.start(activity);
  }

  public void showLogin(@NonNull Activity activity) {
    LoginActivity.start(activity);
  }

  public void showSearch(@NonNull Activity activity) {
    SearchActivity.start(activity);
  }

  public void showNotification(@NonNull Activity activity) {
    NotificationActivity.start(activity);
  }

  public void showReply(@NonNull Activity activity, @NonNull String title, @NonNull int id) {
    ReplyActivity.start(activity, title, id);
  }
}
