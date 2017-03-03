package com.xshengcn.diycode.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.ui.BaseActivity;

public class UserActivity extends BaseActivity {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.tab_layout) TabLayout tabLayout;
  @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;
  @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;

  public static void start(Activity activity) {
    activity.startActivity(new Intent(activity, UserActivity.class));
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

  }
}
