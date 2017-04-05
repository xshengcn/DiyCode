package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.user.Notification;
import com.xshengcn.diycode.ui.adapter.NotificationAdapter;
import com.xshengcn.diycode.ui.iview.INotificationView;
import com.xshengcn.diycode.ui.presenter.NotificationPresenter;
import com.xshengcn.diycode.ui.widget.itemdecoration.InsetDividerDecoration;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreHandler;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreWrapper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity
        extends BaseActivity implements INotificationView, LoadMoreHandler {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.recycler_View)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.state_view)
    MultiStateView stateView;

    @BindString(R.string.my_topics)
    String myTopics;
    @BindString(R.string.user_topics)
    String userTopics;
    @BindColor(R.color.colorDivider)
    int dividerColor;
    @BindDimen(R.dimen.divider)
    int divider;

    @Inject
    NotificationPresenter presenter;
    @Inject
    NotificationAdapter adapter;
    private LoadMoreWrapper mWrapper;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, NotificationActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);
        ButterKnife.bind(this);
        getComponent().inject(this);

        toolbar.setTitle(R.string.menu_notification);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout.setOnRefreshListener(presenter::onRefresh);
        mWrapper = new LoadMoreWrapper(this, adapter);
        recyclerView.addItemDecoration(
                new InsetDividerDecoration(NotificationAdapter.ViewHolder.class, divider, 0,
                        dividerColor));
        recyclerView.setAdapter(mWrapper);
        presenter.onAttach(this);
    }


    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }


    @Override
    public int getItemOffset() {
        return adapter.getItemCount();
    }

    @Override
    public boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    @Override
    public void showRefreshErrorAndComplete() {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, "gangan", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadMoreFailed() {
        mWrapper.showLoadFailed();
    }

    @Override
    public void showNotifications(List<Notification> notifications, boolean clean) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        mWrapper.hideFooter();

        if (clean) {
            adapter.clear();
            adapter.addTopics(notifications);
            mWrapper.notifyDataSetChanged();
        } else {
            int start = adapter.getItemCount();
            adapter.addTopics(notifications);
            mWrapper.notifyItemRangeInserted(start, notifications.size());
        }
    }

    @Override
    public void showNoMoreNotification() {
        mWrapper.showNoMore();
    }


    @Override
    public void changeStateView(@MultiStateView.ViewState int state) {
        stateView.setViewState(state);
        if (state == MultiStateView.VIEW_STATE_ERROR) {
            ButterKnife.findById(stateView.getView(state), R.id.no_connection_retry)
                    .setOnClickListener(v -> presenter.onRefresh());
        }
    }

    @Override
    public boolean canLoadMore() {
        return !swipeRefreshLayout.isRefreshing();
    }

    @Override
    public void loadMore() {
        presenter.loadMore();
    }
}
