package com.xshengcn.diycode.ui;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.ui.activity.LoginActivity;
import com.xshengcn.diycode.ui.activity.NotificationActivity;
import com.xshengcn.diycode.ui.activity.ReplyActivity;
import com.xshengcn.diycode.ui.activity.SearchActivity;
import com.xshengcn.diycode.ui.activity.TopicCreatorActivity;
import com.xshengcn.diycode.ui.activity.UserActivity;
import com.xshengcn.diycode.ui.activity.UserFavoriteActivity;
import com.xshengcn.diycode.ui.activity.UserReplyActivity;
import com.xshengcn.diycode.ui.activity.UserTopicActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class ActivityNavigator {

    private final DiyCodePrefs prefs;

    @Inject
    public ActivityNavigator(DiyCodePrefs prefs) {
        this.prefs = prefs;
    }

    public void showUserReplies(@NonNull Activity activity) {
        if (prefs.getToken() == null) {
            showLogin(activity);
            return;
        }
        UserReplyActivity.start(activity, prefs.getUser().login);
    }

    public void showUserFavorites(@NonNull Activity activity) {
        if (prefs.getToken() == null) {
            showLogin(activity);
            return;
        }
        UserFavoriteActivity.start(activity, prefs.getUser().login);
    }

    public void showUserTopics(@NonNull Activity activity) {
        if (prefs.getToken() == null) {
            showLogin(activity);
            return;
        }
        UserTopicActivity.start(activity, prefs.getUser().login);
    }

    public void showUser(@NonNull Activity activity) {
        if (prefs.getToken() == null) {
            showLogin(activity);
            return;
        }
        UserActivity.start(activity);
    }

    public void showLogin(@NonNull Activity activity) {
        LoginActivity.start(activity);
    }

    public void showSearch(@NonNull Activity activity) {
        SearchActivity.start(activity);
    }

    public void showNotification(@NonNull Activity activity) {
        if (prefs.getToken() == null) {
            showLogin(activity);
            return;
        }
        NotificationActivity.start(activity);
    }

    public void showReply(@NonNull Activity activity, @NonNull String title, @NonNull int id) {
        ReplyActivity.start(activity, title, id);
    }

    public void showCreateTopic(@NonNull Activity activity) {
        TopicCreatorActivity.start(activity);
    }
}
