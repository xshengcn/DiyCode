package com.xshengcn.diycode.ui.notification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.entity.user.Notification;
import com.xshengcn.diycode.ui.BaseActivity;
import com.xshengcn.diycode.widget.itemdecoration.OffsetDecoration;
import com.xshengcn.diycode.widget.recyclerview.LoadMoreHandler;
import com.xshengcn.diycode.widget.recyclerview.LoadMoreWrapper;
import java.util.List;
import javax.inject.Inject;

public class NotificationActivity extends BaseActivity implements INotificationView, LoadMoreHandler {

  private static final String EXTRA_USER_LOGIN = "UserTopicActivity.userLogin";
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;
  @BindView(R.id.recycler_View) RecyclerView recyclerView;
  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.state_view) MultiStateView stateView;

  @BindDimen(R.dimen.spacing_xsmall) int space;
  @BindString(R.string.topics) String myTopics;
  @BindString(R.string.user_topics) String userTopics;

  @Inject NotificationPresenter presenter;
  @Inject NotificationAdapter adapter;
  private LoadMoreWrapper wrapper;

  public static void start(Activity activity) {
    Intent intent = new Intent(activity, NotificationActivity.class);
    activity.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_topic);
    ButterKnife.bind(this);
    getComponent().inject(this);


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

  @Override public void showNotifications(List<Notification> notifications, boolean clean) {
    if (swipeRefreshLayout.isRefreshing()) {
      swipeRefreshLayout.setRefreshing(false);
    }
    stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    wrapper.hideFooter();

    if (clean) {
      adapter.clear();
      adapter.addTopics(notifications);
      wrapper.notifyDataSetChanged();
    } else {
      int start = adapter.getItemCount();
      adapter.addTopics(notifications);
      wrapper.notifyItemRangeInserted(start, notifications.size());
    }
  }

  @Override public void showNoMoreNotification() {
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
