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
import com.xshengcn.diycode.data.DiyCodePrefs;
import com.xshengcn.diycode.data.model.user.UserReply;
import com.xshengcn.diycode.ui.adapter.UserReplyAdapter;
import com.xshengcn.diycode.ui.iview.IUserReplyView;
import com.xshengcn.diycode.ui.presenter.UserReplyPresenter;
import com.xshengcn.diycode.ui.widget.itemdecoration.OffsetDecoration;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreHandler;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreWrapper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserReplyActivity extends BaseActivity implements IUserReplyView, LoadMoreHandler {

    private static final String EXTRA_USER = "UserTopicActivity.user";
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
    @BindString(R.string.relies)
    String myReplies;
    @BindString(R.string.user_replies)
    String userReplies;

    @Inject
    UserReplyPresenter presenter;
    @Inject
    UserReplyAdapter adapter;
    @Inject
    DiyCodePrefs prefs;
    private String mUser;
    private LoadMoreWrapper mWrapper;

    public static void start(Activity activity, String user) {
        Intent intent = new Intent(activity, UserReplyActivity.class);
        intent.putExtra(EXTRA_USER, user);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reply);
        ButterKnife.bind(this);
        getComponent().inject(this);

        mUser = getIntent().getStringExtra(EXTRA_USER);

        if (prefs.getUser() != null && TextUtils.equals(mUser, prefs.getUser().login)) {
            toolbar.setTitle(myReplies);
        } else {
            toolbar.setTitle(String.format(userReplies, mUser));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout.setOnRefreshListener(presenter::onRefresh);
        recyclerView.setPadding(0, space, 0, space);
        mWrapper = new LoadMoreWrapper(this, adapter);
        recyclerView.addItemDecoration(new OffsetDecoration(space, space, 0, 0));
        recyclerView.setAdapter(mWrapper);
        presenter.onAttach(this);
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
    public String getUser() {
        return mUser;
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
    public void showUserReplies(List<UserReply> replies, boolean clean) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        mWrapper.hideFooter();

        if (clean) {
            adapter.clear();
            adapter.addTopics(replies);
            mWrapper.notifyDataSetChanged();
        } else {
            int start = adapter.getItemCount();
            adapter.addTopics(replies);
            mWrapper.notifyItemRangeInserted(start, replies.size());
        }
    }

    @Override
    public void showNoMoreTopic() {
        mWrapper.showNoMore();
    }

    @Override
    public void changeStateView(@MultiStateView.ViewState int state) {
        stateView.setViewState(state);
        if (state == MultiStateView.VIEW_STATE_ERROR && stateView.getView(state) != null) {
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
