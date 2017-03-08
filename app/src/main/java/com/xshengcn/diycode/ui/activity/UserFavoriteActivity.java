package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.entity.topic.Topic;
import com.xshengcn.diycode.ui.adapter.TopicAdapter;
import com.xshengcn.diycode.ui.iview.IUserFavoriteView;
import com.xshengcn.diycode.ui.presenter.UserFavoritePresenter;
import com.xshengcn.diycode.widget.itemdecoration.OffsetDecoration;
import com.xshengcn.diycode.widget.recyclerview.LoadMoreHandler;
import com.xshengcn.diycode.widget.recyclerview.LoadMoreWrapper;
import java.util.List;
import javax.inject.Inject;

public class UserFavoriteActivity extends BaseActivity
    implements IUserFavoriteView, LoadMoreHandler {

  private static final String EXTRA_USER_LOGIN = "UserTopicActivity.userLogin";
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;
  @BindView(R.id.recycler_View) RecyclerView recyclerView;
  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.state_view) MultiStateView stateView;

  @BindDimen(R.dimen.spacing_xsmall) int space;
  @BindString(R.string.favorites) String myFavorites;
  @BindString(R.string.user_favorites) String userFavorites;

  @Inject UserFavoritePresenter presenter;
  @Inject TopicAdapter adapter;
  @Inject DiyCodePrefs prefs;
  private String userLogin;
  private LoadMoreWrapper wrapper;

  public static void start(Activity activity, String userLogin) {
    Intent intent = new Intent(activity, UserFavoriteActivity.class);
    intent.putExtra(EXTRA_USER_LOGIN, userLogin);
    activity.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_favorite);
    ButterKnife.bind(this);
    getComponent().inject(this);

    userLogin = getIntent().getStringExtra(EXTRA_USER_LOGIN);

    if (prefs.getUser() != null && TextUtils.equals(userLogin, prefs.getUser().login)) {
      toolbar.setTitle(myFavorites);
    } else {
      toolbar.setTitle(String.format(userFavorites, userLogin));
    }

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    swipeRefreshLayout.setOnRefreshListener(presenter::onRefresh);
    recyclerView.setPadding(0, space, 0, space);
    wrapper = new LoadMoreWrapper(this, adapter);
    recyclerView.addItemDecoration(new OffsetDecoration(space, space, 0, 0));
    recyclerView.setAdapter(wrapper);
    presenter.onAttach(this);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        super.onBackPressed();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override protected void onDestroy() {
    presenter.onDetach();
    super.onDestroy();
  }

  @Override public String getUserLogin() {
    return userLogin;
  }

  @Override public int getItemOffset() {
    return adapter.getItemCount();
  }

  @Override public boolean isRefreshing() {
    return swipeRefreshLayout.isRefreshing();
  }

  @Override public void showRefreshErrorAndComplete() {
    swipeRefreshLayout.setRefreshing(false);
    Toast.makeText(this, "gangan", Toast.LENGTH_SHORT).show();
  }

  @Override public void showLoadMoreFailed() {
    wrapper.showLoadFailed();
  }

  @Override public void showTopics(List<Topic> topics, boolean clean) {
    if (swipeRefreshLayout.isRefreshing()) {
      swipeRefreshLayout.setRefreshing(false);
    }
    stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    wrapper.hideFooter();

    if (clean) {
      adapter.clear();
      adapter.addTopics(topics);
      wrapper.notifyDataSetChanged();
    } else {
      int start = adapter.getItemCount();
      adapter.addTopics(topics);
      wrapper.notifyItemRangeInserted(start, topics.size());
    }
  }

  @Override public void showNoMoreTopic() {
    wrapper.showNoMore();
  }

  @Override public void changeStateView(@MultiStateView.ViewState int state) {
    stateView.setViewState(state);
    if (state == MultiStateView.VIEW_STATE_ERROR) {
      ButterKnife.findById(stateView.getView(state), R.id.no_connection_retry)
          .setOnClickListener(v -> presenter.onRefresh());
    }
  }

  @Override public boolean canLoadMore() {
    return !swipeRefreshLayout.isRefreshing();
  }

  @Override public void loadMore() {
    presenter.loadMore();
  }
}
