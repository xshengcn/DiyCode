package com.xshengcn.diycode.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.xshengcn.diycode.data.DiyCodePrefs;
import com.xshengcn.diycode.data.model.topic.Topic;
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
import com.xshengcn.diycode.util.BrowserUtil;

import javax.inject.Inject;

@PreActivity
public final class ActivityNavigator {

    private final DiyCodePrefs mPrefs;
    private final AppCompatActivity mActivity;

    @Inject
    public ActivityNavigator(DiyCodePrefs prefs, AppCompatActivity activity) {
        this.mPrefs = prefs;
        this.mActivity = activity;
    }

    public void showUserReplies() {
        if (mPrefs.getToken() == null) {
            showLogin();
            return;
        }
        UserReplyActivity.start(mActivity, mPrefs.getUser().login);
    }

    public void showUserFavorites() {
        if (mPrefs.getToken() == null) {
            showLogin();
            return;
        }
        UserFavoriteActivity.start(mActivity, mPrefs.getUser().login);
    }

    public void showUserTopics() {
        if (mPrefs.getToken() == null) {
            showLogin();
            return;
        }
        UserTopicActivity.start(mActivity, mPrefs.getUser().login);
    }

    public void showUser() {
        if (mPrefs.getToken() == null) {
            showLogin();
            return;
        }
        UserActivity.start(mActivity);
    }

    public void showLogin() {
        LoginActivity.start(mActivity);
    }

    public void showSearch() {
        SearchActivity.start(mActivity);
    }

    public void showNotification() {
        if (mPrefs.getToken() == null) {
            showLogin();
            return;
        }
        NotificationActivity.start(mActivity);
    }

    public void showReply(@NonNull String title, @NonNull int id) {
        if (mPrefs.getToken() == null) {
            showLogin();
            return;
        }
        ReplyActivity.start(mActivity, title, id);
    }

    public void showCreateTopic() {
        if (mPrefs.getToken() == null) {
            showLogin();
            return;
        }
        TopicCreatorActivity.start(mActivity);
    }

    public void showSite() {
        SiteActivity.start(mActivity);
    }

    public void showTopicDetail(@NonNull Topic topic) {
        TopicDetailActivity.start(mActivity, topic);
    }

    public void showWeb(@NonNull String url) {
        BrowserUtil.openUrl(mActivity, url);
    }
}
