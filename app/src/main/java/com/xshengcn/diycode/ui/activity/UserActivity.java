package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.user.UserDetail;
import com.xshengcn.diycode.ui.ActivityNavigator;
import com.xshengcn.diycode.ui.iview.IUserView;
import com.xshengcn.diycode.ui.presenter.UserPresenter;
import com.xshengcn.diycode.ui.widget.TitleDescriptionView;
import com.xshengcn.diycode.util.glide.CircleTransform;

import java.text.MessageFormat;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserActivity extends BaseActivity implements IUserView {

    private static final String EXTRA_USER_LOGIN = "UserActivity.userLogin";
    private static final String EXTRA_ME = "UserActivity.me";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.user_avatar)
    ImageView mUserAvatar;
    @BindView(R.id.username)
    TextView mUsername;
    @BindView(R.id.user_id)
    TextView mUserId;
    @BindView(R.id.user_join)
    TextView mUserJoin;
    @BindView(R.id.recent_reply)
    TextView mRecentReply;
    @BindView(R.id.topic_count)
    TextView mTopicCount;
    @BindView(R.id.topic_layout)
    LinearLayout mTopicLayout;
    @BindView(R.id.favorite_count)
    TextView mFavoriteCount;
    @BindView(R.id.favorite_layout)
    LinearLayout mFavoriteLayout;
    @BindView(R.id.share_count)
    TextView mShareCount;
    @BindView(R.id.share_layout)
    LinearLayout mShareLayout;
    @BindView(R.id.user_github)
    TextView mUserGithub;
    @BindView(R.id.user_twitter)
    TextView mUserTwitter;
    @BindView(R.id.user_website)
    TextView mUserWebsite;
    @BindView(R.id.user_follower)
    TitleDescriptionView mUserFollower;
    @BindView(R.id.user_following)
    TitleDescriptionView mUserFollowing;

    @BindString(R.string.user_id_description)
    String mIdDesc;
    @BindString(R.string.github_user_url)
    String mGithubUser;
    @BindString(R.string.twitter_user_url)
    String mTwitterUser;

    @Inject
    UserPresenter mPresenter;
    @Inject
    ActivityNavigator mNavigator;

    private String mUserLogin;
    private boolean mMe;

    public static void start(Activity activity, boolean me) {
        Intent intent = new Intent(activity, UserActivity.class);
        intent.putExtra(EXTRA_ME, me);
        activity.startActivity(intent);
    }

    public static void start(Activity activity, String userLogin) {
        Intent intent = new Intent(activity, UserActivity.class);
        intent.putExtra(EXTRA_USER_LOGIN, userLogin);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getComponent().inject(this);
        ButterKnife.bind(this);
        mPresenter.onAttach(this);

        mUserLogin = getIntent().getStringExtra(EXTRA_USER_LOGIN);
        mMe = getIntent().getBooleanExtra(EXTRA_ME, false);

        if (!mMe && TextUtils.isEmpty(mUserLogin)) {
            finish();
        }

        mToolbar.setTitle(R.string.user_page);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter.loadUserDetail();
    }

    @OnClick(R.id.recent_reply)
    void clickRecentReply() {
        if (mMe) {
            mNavigator.showUserReplies(mMe);
        } else {
            mNavigator.showUserReplies(getUserLogin());
        }
    }

    @OnClick(R.id.topic_layout)
    void clickUserTopic() {
        if (mMe) {
            mNavigator.showUserTopics(mMe);
        } else {
            mNavigator.showUserTopics(getUserLogin());
        }
    }

    @OnClick(R.id.favorite_layout)
    void clickUserFavorite() {
        if (mMe) {
            mNavigator.showUserFavorites(mMe);
        } else {
            mNavigator.showUserFavorites(getUserLogin());
        }
    }

    @OnClick(R.id.user_github)
    void clickGithub() {
        mNavigator.showWeb(mUserGithub.getText().toString());
    }

    @OnClick(R.id.user_twitter)
    void clickTwitter() {
        mNavigator.showWeb(mUserTwitter.getText().toString());
    }

    @OnClick(R.id.user_website)
    void clickWebsite() {
        mNavigator.showWeb(mUserWebsite.getText().toString());
    }

    @Override
    public String getUserLogin() {
        return mUserLogin;
    }

    @Override
    public void updateUserDetail(UserDetail userDetail) {

        Glide.with(this).load(userDetail.avatarUrl.replace("large_avatar", "avatar"))
                .transform(new CircleTransform(this)).into(mUserAvatar);

        mUsername.setText(MessageFormat.format("{0}({1})", userDetail.login, userDetail.name));
        mUserId.setText(MessageFormat.format(mIdDesc, userDetail.id));
        mUserJoin.setText(DateFormat.format("yyyy-MM-dd", userDetail.createdAt));

        mUserFollower.setTitle(String.valueOf(userDetail.followersCount));
        mUserFollowing.setTitle(String.valueOf(userDetail.followingCount));

        mTopicCount.setText(String.valueOf(userDetail.topicsCount));
        mFavoriteCount.setText(String.valueOf(userDetail.favoritesCount));
        mShareCount.setText("0");

        if (!TextUtils.isEmpty(userDetail.github)) {
            mUserGithub.setVisibility(View.VISIBLE);
            mUserGithub.setText(MessageFormat.format(mGithubUser, userDetail.github));
        }

        if (!TextUtils.isEmpty(userDetail.twitter)) {
            mUserTwitter.setVisibility(View.VISIBLE);
            mUserTwitter.setText(
                    MessageFormat.format(mTwitterUser, userDetail.twitter));
        }

        if (!TextUtils.isEmpty(userDetail.website)) {
            mUserWebsite.setVisibility(View.VISIBLE);
            mUserWebsite.setText(userDetail.website);
        }
    }

    @Override
    public boolean isMe() {
        return mMe;
    }

}
