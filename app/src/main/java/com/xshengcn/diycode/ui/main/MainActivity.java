package com.xshengcn.diycode.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.entity.user.UserDetail;
import com.xshengcn.diycode.ui.BaseActivity;
import com.xshengcn.diycode.ui.topiccreate.TopicCreatorActivity;
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

  @Inject MainPresenter presenter;
  @Inject MainPagerAdapter adapter;
  @BindView(R.id.fab_create_topic) FloatingActionButton fabCreateTopic;
  @BindView(R.id.fab_create_news) FloatingActionButton fabCreateNews;
  @BindView(R.id.fab_menu) FloatingActionMenu fabMenu;
  @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;

  private boolean hasNotification;

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
    header.setOnClickListener(v -> presenter.clickHeader(this));
    name = (TextView) headerView.findViewById(R.id.name);
    email = (TextView) headerView.findViewById(R.id.email);

    navView.setNavigationItemSelectedListener(menuItem -> onNavigationItemSelected(menuItem));

    fabMenu.setClosedOnTouchOutside(true);
    fabMenu.setOnMenuToggleListener(this::onFabMenuToggle);

    fabCreateTopic.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        TopicCreatorActivity.start(MainActivity.this);
      }
    });
    presenter.onAttach(this);
  }

  private void onFabMenuToggle(boolean open) {
    if (open) {
      fabMenu.setBackgroundResource(R.color.fab_menu);
    } else {
      fabMenu.setBackgroundResource(android.R.color.transparent);
    }
  }

  @Override protected void onDestroy() {
    presenter.onDetach();
    super.onDestroy();
  }

  private boolean onNavigationItemSelected(MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case R.id.nav_topic:
        presenter.clickUserTopic(this);
        break;
      case R.id.nav_favorite:
        presenter.clickUserFavorite(this);
        break;
      case R.id.nav_reply:
        presenter.clickUserReply(this);
        break;
    }
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
    if (hasNotification) {
      menu.findItem(R.id.action_notice).setIcon(R.drawable.ic_menu_notice_red);
    }
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_search:
        presenter.clickSearch(this);
        break;
      case R.id.action_notice:
        presenter.clickNotification(this);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
    } else if (fabMenu.isOpened()) {
      fabMenu.close(true);
    } else {
      super.onBackPressed();
    }
  }

  @Override public void setupNavigationView(UserDetail user) {
    Glide.with(this)
        .load(user.avatarUrl.replace("large_avatar", "avatar"))
        .transform(new CircleTransform(this))
        .into(header);
    name.setText(user.name);
    email.setText(user.email);
  }

  @Override public void showNotificationMenuBadge(Boolean showBadge) {
    hasNotification = showBadge;
    invalidateOptionsMenu();
  }
}
