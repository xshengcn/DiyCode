package com.xshengcn.diycode.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.xshengcn.diycode.data.PreferencesHelper;
import com.xshengcn.diycode.injection.PreActivity;
import com.xshengcn.diycode.ui.activity.LicensesActivity;
import com.xshengcn.diycode.ui.activity.LoginActivity;
import com.xshengcn.diycode.ui.activity.MarkdownPreviewActivity;
import com.xshengcn.diycode.ui.activity.MarkdownSyntaxReferenceActivity;
import com.xshengcn.diycode.ui.activity.NotificationActivity;
import com.xshengcn.diycode.ui.activity.SearchActivity;
import com.xshengcn.diycode.ui.activity.SettingsActivity;
import com.xshengcn.diycode.ui.activity.SiteActivity;
import com.xshengcn.diycode.ui.activity.TopicCreatorActivity;
import com.xshengcn.diycode.ui.activity.TopicDetailActivity;
import com.xshengcn.diycode.ui.activity.TopicReplyActivity;
import com.xshengcn.diycode.ui.activity.UserActivity;
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

    public void showUserReplies(boolean me) {
        if (me && mPreferencesHelper.getToken() == null) {
            showLogin();
            return;
        }
        UserReplyActivity.start(mActivity, me);
    }

    public void showUserReplies(@NonNull String userLogin) {
        UserReplyActivity.start(mActivity, userLogin);
    }

    public void showUserFavorites(boolean me) {
        if (me && mPreferencesHelper.getToken() == null) {
            showLogin();
            return;
        }
        UserFavoriteActivity.start(mActivity, me);
    }

    public void showUserFavorites(String userLogin) {
        UserFavoriteActivity.start(mActivity, userLogin);
    }

    public void showUserTopics(boolean me) {
        if (me && mPreferencesHelper.getToken() == null) {
            showLogin();
            return;
        }
        UserTopicActivity.start(mActivity, me);
    }

    public void showUserTopics(String userLogin) {
        UserTopicActivity.start(mActivity, userLogin);
    }

    public void showUser(boolean me) {
        if (me && mPreferencesHelper.getToken() == null) {
            showLogin();
            return;
        }
        UserActivity.start(mActivity, me);
    }

    public void showUser(String user) {
        UserActivity.start(mActivity, user);
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

    public void showReply(@NonNull String title, int id) {
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

    public void showTopicDetail(int topicId, @NonNull String topicTitle) {
        TopicDetailActivity.start(mActivity, topicId, topicTitle);
    }

    public void showWeb(@NonNull String url) {
        BrowserUtil.openUrl(mActivity, url);
    }

    public void showPreView(@NonNull String markdown) {
        MarkdownPreviewActivity.start(mActivity, markdown);
    }

    public void showMarkdownHelper() {
        MarkdownSyntaxReferenceActivity.start(mActivity);
    }

    public void showSettings() {
        SettingsActivity.start(mActivity);
    }

    public void showLicenses() {
        LicensesActivity.start(mActivity);
    }
}
