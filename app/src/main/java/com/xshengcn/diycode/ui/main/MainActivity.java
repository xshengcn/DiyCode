package com.xshengcn.diycode.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.entity.user.UserDetail;
import com.xshengcn.diycode.ui.BaseActivity;
import com.xshengcn.diycode.ui.login.LoginActivity;
import com.xshengcn.diycode.ui.search.SearchActivity;
import com.xshengcn.diycode.ui.userfavorite.usertopic.UserFavoriteActivity;
import com.xshengcn.diycode.ui.usertopic.UserTopicActivity;
import com.xshengcn.diycode.util.glide.CircleTransform;
import javax.inject.Inject;

public class MainActivity extends BaseActivity implements IMainView {

  @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;
  @BindView(R.id.nav_view) NavigationView navView;
  @BindView(R.id.tab_layout) TabLayout tabLayout;
  @BindView(R.id.view_pager) ViewPager viewPager;

  @BindView(R.id.header) @Nullable ImageView header;
  @BindView(R.id.name) @Nullable TextView name;
  @BindView(R.id.email) @Nullable TextView email;

  @BindView(R.id.notice) @Nullable ImageView menuNotice;
  @BindView(R.id.notice_count) @Nullable View noticeCount;

  @Inject DiyCodeClient client;
  @Inject DiyCodePrefs prefs;
  @Inject MainPagerAdapter adapter;
  @Inject MainPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    getComponent().inject(this);
    ButterKnife.bind(this);
    setupActionBar();

    viewPager.setAdapter(adapter);
    tabLayout.setTabMode(TabLayout.MODE_FIXED);
    tabLayout.setupWithViewPager(viewPager);

    View headerView = navView.getHeaderView(0);
    header = (ImageView) headerView.findViewById(R.id.header);
    header.setOnClickListener(v -> presenter.clickHeader());
    name = (TextView) headerView.findViewById(R.id.name);
    email = (TextView) headerView.findViewById(R.id.email);

    navView.setNavigationItemSelectedListener(menuItem -> onNavigationItemSelected(menuItem));

    presenter.onAttach(this);
  }

  private boolean onNavigationItemSelected(MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case R.id.nav_topic:
        UserTopicActivity.start(this);
        break;
      case R.id.nav_favorite:
        UserFavoriteActivity.start(this);
        break;
    }
    //        menuItem.setChecked(false);

    return true;
  }

  private void setupActionBar() {
    toolbar.setTitle("");
    toolbar.setLogo(R.drawable.ic_logo);
    setSupportActionBar(toolbar);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    drawerLayout.addDrawerListener(toggle);
    toggle.syncState();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    final View view = menu.findItem(R.id.action_notice).getActionView();
    menuNotice = ButterKnife.findById(view, R.id.notice);
    noticeCount = ButterKnife.findById(view, R.id.notice_count);
    view.setOnClickListener(v -> {});
    menu.findItem(R.id.action_notice).setCheckable(true);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_search:
        SearchActivity.start(this);
        break;
      case R.id.action_notice:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override public void toUserActivity() {
    //UserActivity.start(this);
    if (noticeCount != null) {
      noticeCount.setVisibility(View.VISIBLE);
    }
  }

  @Override public void toLoginActivity() {
    LoginActivity.start(this);
  }

  @Override public void setupNavigationView(UserDetail user) {
    Glide.with(this).load(user.avatarUrl).transform(new CircleTransform(this)).into(header);
    name.setText(user.name);
    email.setText(user.email);
  }

  @Override public void showNotificationMenuBadge(Boolean showBadge) {
    if (noticeCount != null) {
      noticeCount.setVisibility(showBadge ? View.VISIBLE : View.GONE);
    }
  }
}
