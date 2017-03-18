package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.DiyCodePrefs;
import com.xshengcn.diycode.ui.fragment.TopicFragment;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserTopicActivity extends BaseActivity {

    private static final String EXTRA_USER_LOGIN = "UserTopicActivity.userLogin";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @BindString(R.string.topics)
    String myTopics;
    @BindString(R.string.user_topics)
    String userTopics;

    @Inject
    DiyCodePrefs prefs;

    public static void start(Activity activity, String userLogin) {
        Intent intent = new Intent(activity, UserTopicActivity.class);
        intent.putExtra(EXTRA_USER_LOGIN, userLogin);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_topic);
        ButterKnife.bind(this);
        getComponent().inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String user = getIntent().getStringExtra(EXTRA_USER_LOGIN);

        if (prefs.getUser() != null && TextUtils.equals(user, prefs.getUser().login)) {
            getSupportActionBar().setTitle(myTopics);
        } else {
            getSupportActionBar().setTitle(String.format(userTopics, user));
        }

        replaceFragment(TopicFragment.newInstance(user), R.id.fragment_container);
    }
}
