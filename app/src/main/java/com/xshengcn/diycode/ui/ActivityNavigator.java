package com.xshengcn.diycode.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.xshengcn.diycode.data.PreferencesHelper;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.injection.PreActivity;
import com.xshengcn.diycode.ui.activity.LoginActivity;
import com.xshengcn.diycode.ui.activity.MarkdownPreviewActivity;
import com.xshengcn.diycode.ui.activity.NotificationActivity;
import com.xshengcn.diycode.ui.activity.SearchActivity;
import com.xshengcn.diycode.ui.activity.SiteActivity;
import com.xshengcn.diycode.ui.activity.TopicCreatorActivity;
import com.xshengcn.diycode.ui.activity.TopicDetailActivity;
import com.xshengcn.diycode.ui.activity.TopicReplyActivity;
import com.xshengcn.diycode.ui.activity.UserFavoriteActivity;
import com.xshengcn.diycode.ui.activity.UserReplyActivity;
import com.xshengcn.diycode.ui.activity.UserTopicActivity;
import com.xshengcn.diycode.util.BrowserUtil;

import javax.inject.Inject;

@PreActivity
public final class ActivityNavigator {

    private final PreferencesHelper mPreferencesHelper;
    private final AppCompatActivity mActivity;

    @Inject
    public ActivityNavigator(PreferencesHelper preferencesHelper, AppCompatActivity activity) {
        this.mPreferencesHelper = preferencesHelper;
        this.mActivity = activity;
    }

    public void showUserReplies() {
        if (mPreferencesHelper.getToken() == null) {
            showLogin();
            return;
        }
        UserReplyActivity.start(mActivity, mPreferencesHelper.getUser().login);
    }

    public void showUserFavorites() {
        if (mPreferencesHelper.getToken() == null) {
            showLogin();
            return;
        }
        UserFavoriteActivity.start(mActivity, mPreferencesHelper.getUser().login);
    }

    public void showUserTopics() {
        if (mPreferencesHelper.getToken() == null) {
            showLogin();
            return;
        }
        UserTopicActivity.start(mActivity, mPreferencesHelper.getUser().login);
    }

    public void showUser() {
        if (mPreferencesHelper.getToken() == null) {
            showLogin();
            return;
        }
        // TODO 完善个人页面
        // UserActivity.start(mActivity);
    }

    public void showLogin() {
        LoginActivity.start(mActivity);
    }

    public void showSearch() {
        SearchActivity.start(mActivity);
    }

    public void showNotification() {
        if (mPreferencesHelper.getToken() == null) {
            showLogin();
            return;
        }
        NotificationActivity.start(mActivity);
    }

    public void showReply(@NonNull String title, @NonNull int id) {
        if (mPreferencesHelper.getToken() == null) {
            showLogin();
            return;
        }
        TopicReplyActivity.start(mActivity, title, id);
    }

    public void showCreateTopic() {
        if (mPreferencesHelper.getToken() == null) {
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

    public void showPreView(@NonNull String markdown) {
        MarkdownPreviewActivity.start(mActivity, markdown);
    }
}
