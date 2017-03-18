package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.xshengcn.diycode.R;
import com.xshengcn.diycode.ui.fragment.SiteFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SiteActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SiteActivity.class);
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

        replaceFragment(SiteFragment.newInstance(), R.id.fragment_container);
    }
}
