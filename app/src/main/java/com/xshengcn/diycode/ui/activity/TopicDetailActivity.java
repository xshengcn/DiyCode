package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.topic.TopicAndReplies;
import com.xshengcn.diycode.data.model.topic.TopicReply;
import com.xshengcn.diycode.ui.ActivityNavigator;
import com.xshengcn.diycode.ui.adapter.TopicDetailAdapter;
import com.xshengcn.diycode.ui.iview.ITopicDetailView;
import com.xshengcn.diycode.ui.presenter.TopicDetailPresenter;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreHandler;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreWrapper;
import com.xshengcn.diycode.util.HtmlUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicDetailActivity extends BaseActivity
        implements ITopicDetailView, HtmlUtils.Callback, LoadMoreHandler {

    private static final String EXTRA_TOPIC_ID = "TopicDetailActivity.topicId";
    private static final String EXTRA_TOPIC_TITLE = "TopicDetailActivity.topicTitle";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.recycler_View)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.state_view)
    MultiStateView stateView;
    @BindColor(R.color.colorDivider)
    int colorDivider;
    @BindColor(android.R.color.white)
    int color;
    @BindString(R.string.share)
    String share;
    @BindDrawable(R.drawable.divider_drawabe)
    Drawable dividerDrawable;

    @Inject
    TopicDetailPresenter mPresenter;
    @Inject
    TopicDetailAdapter mAdapter;
    @Inject
    ActivityNavigator mNavigator;

    private LoadMoreWrapper mWrapper;
    private int mTopicId;
    private String mTopicTitle;

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!recyclerView.canScrollVertically(0) && fab != null) {
                fab.show();
            }
        }
    };

    public static void start(Activity activity, @NonNull int topicId, @NonNull String topicTitle) {
        Intent intent = new Intent(activity, TopicDetailActivity.class);
        intent.putExtra(EXTRA_TOPIC_ID, topicId);
        intent.putExtra(EXTRA_TOPIC_TITLE, topicTitle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        ButterKnife.bind(this);
        getComponent().inject(this);

        mTopicId = getIntent().getIntExtra(EXTRA_TOPIC_ID, -1);
        mTopicTitle = getIntent().getStringExtra(EXTRA_TOPIC_TITLE);
        toolbar.setTitle(R.string.topic);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DividerItemDecoration decoration = new DividerItemDecoration(this, LinearLayout.VERTICAL);
        decoration.setDrawable(dividerDrawable);
        recyclerView.addItemDecoration(decoration);
        mAdapter.setContentCallBack(this);
        mAdapter.setOnHeaderClickListener(user -> {
        });
        mWrapper = new LoadMoreWrapper(this, mAdapter);
        recyclerView.setAdapter(mWrapper);
        swipeRefreshLayout.setOnRefreshListener(mPresenter::onRefresh);
        fab.setOnClickListener(v -> mNavigator.showReply(getTopicTitle(), getTopicId()));
        recyclerView.addOnScrollListener(mScrollListener);
        mPresenter.onAttach(this);
        mPresenter.onRefresh();
    }

    @Override
    public int getTopicId() {
        return mTopicId;
    }

    @Override
    public String getTopicTitle() {
        return mTopicTitle;
    }

    @Override
    protected void onDestroy() {
        recyclerView.removeOnScrollListener(mScrollListener);
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void showTopicAndReplies(TopicAndReplies topicAndReplies) {
        stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        mWrapper.hideFooter();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        mAdapter.getTopicAndReplies().replies.clear();
        mAdapter.setTopicContent(topicAndReplies.detail);
        mAdapter.addReplies(topicAndReplies.replies);
        mWrapper.notifyDataSetChanged();
    }

    @Override
    public int getItemOffset() {
        return mAdapter.getTopicAndReplies().replies.size();
    }

    @Override
    public void showMoreReplies(List<TopicReply> topicReplies) {
        mWrapper.hideFooter();
        int start = mAdapter.getItemCount();
        mAdapter.addReplies(topicReplies);
        mWrapper.notifyItemRangeInserted(start, topicReplies.size());
    }

    @Override
    public void showLoadMoreFailed() {
        mWrapper.showLoadFailed();
    }

    @Override
    public void showLoadNoMore() {
        mWrapper.showNoMore();
    }

    @Override
    public void changeStateView(@MultiStateView.ViewState int state) {
        stateView.setViewState(state);
    }

    @Override
    public boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    @Override
    public void showRefreshErrorAndComplete() {

    }

    @Override
    public void insertUserReply(TopicReply topicReply) {
        if (mWrapper.getCurrentType() == LoadMoreWrapper.ITEM_TYPE_NO_MORE_VIEW) {
            int count = mAdapter.getItemCount();
            mAdapter.getTopicReplies().add(topicReply);
            mAdapter.notifyItemInserted(count + 1);
        }
    }

    @Override
    public void clickUrl(String url) {
        if (url.startsWith("#reply")) {
            String floorStr = url.substring(6);
            try {
                int floor = Integer.parseInt(floorStr);
                recyclerView.smoothScrollToPosition(floor);
            } catch (NumberFormatException e) {
                Logger.e(e.getMessage());
            }
        } else if (url.startsWith("/")) {
            mNavigator.showUser();
        } else {
            mNavigator.showWeb(url);
        }
    }


    @Override
    public void clickImage(String source) {
        PhotoViewerActivity.start(this, source);
    }

    @Override
    public boolean canLoadMore() {
        TopicAndReplies details = mAdapter.getTopicAndReplies();
        return details.detail.repliesCount > details.replies.size()
                && !swipeRefreshLayout.isRefreshing();
    }

    @Override
    public void loadMore() {
        mPresenter.loadMoreReplies();
    }
}
