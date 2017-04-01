package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.data.model.topic.TopicAndReplies;
import com.xshengcn.diycode.data.model.topic.TopicReply;
import com.xshengcn.diycode.ui.ActivityNavigator;
import com.xshengcn.diycode.ui.adapter.TopicDetailAdapter;
import com.xshengcn.diycode.ui.iview.ITopicDetailView;
import com.xshengcn.diycode.ui.presenter.TopicDetailPresenter;
import com.xshengcn.diycode.ui.widget.itemdecoration.InsetDividerDecoration;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreHandler;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreWrapper;
import com.xshengcn.diycode.util.HtmlUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicDetailActivity extends BaseActivity
        implements ITopicDetailView, HtmlUtils.Callback, LoadMoreHandler {

    private static final String EXTRA_TOPIC = "TopicDetailActivity.topic";
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
    @BindDimen(R.dimen.divider)
    int divider;

    @Inject
    TopicDetailPresenter presenter;
    @Inject
    TopicDetailAdapter adapter;
    @Inject
    ActivityNavigator navigator;

    private LoadMoreWrapper mWrapper;
    private Topic mTopic;

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!recyclerView.canScrollVertically(0) && fab != null) {
                fab.show();
            }
        }
    };

    public static void start(Activity activity, Topic topic) {
        Intent intent = new Intent(activity, TopicDetailActivity.class);
        intent.putExtra(EXTRA_TOPIC, topic);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        ButterKnife.bind(this);
        getComponent().inject(this);

        mTopic = getIntent().getParcelableExtra(EXTRA_TOPIC);
        toolbar.setTitle(R.string.topic);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.addItemDecoration(
                new InsetDividerDecoration(TopicDetailAdapter.ViewHolder.class,
                        divider, 0, colorDivider));
        adapter.setContentCallBack(this);
        adapter.setOnHeaderClickListener(user -> {
        });
        mWrapper = new LoadMoreWrapper(this, adapter);
        recyclerView.setAdapter(mWrapper);
        swipeRefreshLayout.setOnRefreshListener(presenter::onRefresh);
        fab.setOnClickListener(v -> navigator.showReply(getTopic().title, getTopic().id));
        recyclerView.addOnScrollListener(mScrollListener);
        presenter.onAttach(this);
        presenter.onRefresh();
    }

    @Override
    protected void onDestroy() {
        recyclerView.removeOnScrollListener(mScrollListener);
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public Topic getTopic() {
        return mTopic;
    }

    @Override
    public void showTopicAndReplies(TopicAndReplies topicAndReplies) {
        stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        mWrapper.hideFooter();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        adapter.getTopicAndReplies().replies.clear();
        adapter.setTopicContent(topicAndReplies.detail);
        adapter.addReplies(topicAndReplies.replies);
        mWrapper.notifyDataSetChanged();
    }

    @Override
    public int getItemOffset() {
        return adapter.getTopicAndReplies().replies.size();
    }

    @Override
    public void showMoreReplies(List<TopicReply> topicReplies) {
        mWrapper.hideFooter();
        int start = adapter.getItemCount();
        adapter.addReplies(topicReplies);
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
            navigator.showUser();
        } else {
            navigator.showWeb(url);
        }
    }

    private void openUri(Activity activity, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    @Override
    public void clickImage(String source) {
        PhotoViewerActivity.start(this, source);
    }

    @Override
    public boolean canLoadMore() {
        TopicAndReplies details = adapter.getTopicAndReplies();
        return details.detail.repliesCount > details.replies.size()
                && !swipeRefreshLayout.isRefreshing();
    }

    @Override
    public void loadMore() {
        presenter.loadMoreReplies();
    }
}
