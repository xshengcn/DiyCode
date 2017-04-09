package com.xshengcn.diycode.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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

import com.bumptech.glide.Glide;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.user.UserDetail;
import com.xshengcn.diycode.ui.ActivityNavigator;
import com.xshengcn.diycode.ui.fragment.NewsFragment;
import com.xshengcn.diycode.ui.fragment.ProjectFragment;
import com.xshengcn.diycode.ui.fragment.TopicFragment;
import com.xshengcn.diycode.ui.iview.IMainView;
import com.xshengcn.diycode.ui.presenter.MainPresenter;
import com.xshengcn.diycode.ui.widget.FabDialog;
import com.xshengcn.diycode.util.glide.CircleTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity
        implements IMainView, FabDialog.OnButtonClickListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.header)
    @Nullable
    ImageView mHeaderImage;
    @BindView(R.id.name)
    @Nullable
    TextView mNameText;
    @BindView(R.id.email)
    @Nullable
    TextView mEmailText;

    @Inject
    MainPresenter mPresenter;
    @Inject
    ActivityNavigator mNavigator;
    @BindView(R.id.fab_menu)
    FloatingActionButton mFabMenu;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private boolean mHasNotification;
    private FabDialog mFabDialog;

    private CompositeDisposable mDisposable = new CompositeDisposable();

    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (newState == DrawerLayout.STATE_DRAGGING) {
                mDisposable.clear();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getComponent().inject(this);
        ButterKnife.bind(this);
        setupActionBar();

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), this));
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);

        View headerView = mNavView.getHeaderView(0);
        mHeaderImage = (ImageView) headerView.findViewById(R.id.header);
        mHeaderImage.setOnClickListener(this::clickNavHeader);
        mNameText = (TextView) headerView.findViewById(R.id.name);
        mEmailText = (TextView) headerView.findViewById(R.id.email);

        mNavView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        mFabMenu.setOnClickListener(this::clickFabMenu);
        mDrawerLayout.addDrawerListener(mDrawerListener);

        mPresenter.onAttach(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void clickNavHeader(View view) {
        mDrawerLayout.closeDrawers();
        mDisposable.clear();
        mDisposable.add(Observable.just("")
                .subscribeOn(Schedulers.single())
                .delay(200, TimeUnit.MILLISECONDS)
                .subscribe(s -> mNavigator.showUser(true)));
    }

    private void clickFabMenu(View view) {
        if (mFabDialog == null) {
            mFabDialog = new FabDialog(this);
            mFabDialog.setOnButtonClickListener(this);
        }
        mFabDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFabDialog != null) {
            mFabDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        mDisposable.clear();
        mDrawerLayout.removeDrawerListener(mDrawerListener);
        super.onDestroy();
    }

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        mDrawerLayout.closeDrawers();
        mDisposable.clear();
        mDisposable.add(Observable.just(menuItem.getItemId())
                .subscribeOn(Schedulers.single())
                .delay(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::clickNavMenu));
        return true;
    }

    private void clickNavMenu(int id) {
        switch (id) {
            case R.id.nav_topic:
                mNavigator.showUserTopics(true);
                break;
            case R.id.nav_favorite:
                mNavigator.showUserFavorites(true);
                break;
            case R.id.nav_reply:
                mNavigator.showUserReplies(true);
                break;
            case R.id.nav_site:
                mNavigator.showSite();
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_feedback:
                break;
            case R.id.nav_setting:
                mNavigator.showSettings();
                break;
        }
    }

    private void setupActionBar() {
        mToolbar.setTitle("");
        mToolbar.setLogo(R.drawable.ic_logo);
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (mHasNotification) {
            menu.findItem(R.id.action_notification).setIcon(R.drawable.ic_menu_notification_red);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                mNavigator.showSearch();
                break;
            case R.id.action_notification:
                mNavigator.showNotification();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setupNavigationView(UserDetail user) {
        Glide.with(this)
                .load(user.avatarUrl.replace("large_avatar", "avatar"))
                .transform(new CircleTransform(this))
                .into(mHeaderImage);
        mNameText.setText(user.name);
        mEmailText.setText(user.email);
    }

    @Override
    public void showNotificationMenuBadge(Boolean showBadge) {
        mHasNotification = showBadge;
        invalidateOptionsMenu();
    }

    @Override
    public void clickNewsButton() {

    }

    @Override
    public void clickTopicButton() {
        mNavigator.showCreateTopic();
    }

    public class MainPagerAdapter extends FragmentPagerAdapter {

        private final String[] mTitles;
        private final List<Fragment> mFragments;

        public MainPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mTitles = context.getResources().getStringArray(R.array.main_tabs);
            mFragments = new ArrayList<>();
            mFragments.add(TopicFragment.newInstance());
            mFragments.add(NewsFragment.newInstance());
            mFragments.add(ProjectFragment.newInstance());
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

}
