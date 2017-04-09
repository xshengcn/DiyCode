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

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.ui.adapter.TopicAdapter;
import com.xshengcn.diycode.ui.iview.IUserFavoriteView;
import com.xshengcn.diycode.ui.presenter.UserFavoritePresenter;
import com.xshengcn.diycode.ui.widget.itemdecoration.OffsetDecoration;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreHandler;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreWrapper;

import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserFavoriteActivity extends BaseActivity
        implements IUserFavoriteView, LoadMoreHandler {

    private static final String EXTRA_USER_LOGIN = "UserTopicActivity.userLogin";
    private static final String EXTRA_ME = "UserTopicActivity.me";

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

    @BindDimen(R.dimen.spacing_xsmall)
    int space;
    @BindString(R.string.my_favorites)
    String myFavorites;
    @BindString(R.string.user_favorites)
    String userFavorites;

    @Inject
    UserFavoritePresenter presenter;
    @Inject
    TopicAdapter adapter;
    private String mUserLogin;
    private boolean mMe;
    private LoadMoreWrapper mWrapper;

    public static void start(Activity activity, String userLogin) {
        Intent intent = new Intent(activity, UserFavoriteActivity.class);
        intent.putExtra(EXTRA_USER_LOGIN, userLogin);
        activity.startActivity(intent);
    }

    public static void start(Activity activity, boolean me) {
        Intent intent = new Intent(activity, UserFavoriteActivity.class);
        intent.putExtra(EXTRA_ME, me);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_favorite);
        ButterKnife.bind(this);
        getComponent().inject(this);

        mUserLogin = getIntent().getStringExtra(EXTRA_USER_LOGIN);
        mMe = getIntent().getBooleanExtra(EXTRA_ME, false);

        if (!mMe && TextUtils.isEmpty(mUserLogin)) {
            finish();
        }

        if (mMe) {
            toolbar.setTitle(myFavorites);
        } else {
            toolbar.setTitle(MessageFormat.format(userFavorites, mUserLogin));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout.setOnRefreshListener(presenter::onRefresh);
        recyclerView.setPadding(0, space, 0, space);
        mWrapper = new LoadMoreWrapper(this, adapter);
        recyclerView.addItemDecoration(new OffsetDecoration(space, space, 0, 0));
        recyclerView.setAdapter(mWrapper);
        presenter.onAttach(this);
        presenter.onRefresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public String getUserLogin() {
        return mUserLogin;
    }

    @Override
    public boolean isMe() {
        return mMe;
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
    public void showTopics(List<Topic> topics, boolean clean) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        mWrapper.hideFooter();

        if (clean) {
            adapter.clear();
            adapter.addTopics(topics);
            mWrapper.notifyDataSetChanged();
        } else {
            int start = adapter.getItemCount();
            adapter.addTopics(topics);
            mWrapper.notifyItemRangeInserted(start, topics.size());
        }
    }

    @Override
    public void showNoMoreTopic() {
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
