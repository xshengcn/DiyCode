package com.xshengcn.diycode.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.ui.ActivityNavigator;
import com.xshengcn.diycode.ui.adapter.TopicAdapter;
import com.xshengcn.diycode.ui.iview.ITopicView;
import com.xshengcn.diycode.ui.presenter.TopicPresenter;
import com.xshengcn.diycode.ui.widget.itemdecoration.OffsetDecoration;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreHandler;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreWrapper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicFragment extends BaseFragment
        implements ITopicView, TopicAdapter.OnItemClickListener, LoadMoreHandler {

    private static final String ARGS_USER = "TopicFragment.user";
    private static final String ARGS_ME = "TopicFragment.me";

    @BindView(R.id.recycler_View)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.state_view)
    MultiStateView stateView;
    @BindDimen(R.dimen.spacing_xsmall)
    int space;

    @Inject
    TopicAdapter adapter;
    @Inject
    TopicPresenter presenter;
    @Inject
    ActivityNavigator navigator;

    private LoadMoreWrapper mWrapper;

    /**
     * 如果获取全部的主题
     */
    public static TopicFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TopicFragment fragment = new TopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static TopicFragment newInstance(@Nullable String userLogin, boolean me) {

        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(userLogin)) {
            args.putString(ARGS_USER, userLogin);
        }
        args.putBoolean(ARGS_ME, me);
        TopicFragment fragment = new TopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefresh());
        recyclerView.setPadding(0, space, 0, space);
        recyclerView.addItemDecoration(new OffsetDecoration(space, space, 0, 0));
        adapter.setOnItemClickListener(this);
        mWrapper = new LoadMoreWrapper(this, adapter);
        recyclerView.setAdapter(mWrapper);
        presenter.onAttach(this);
        presenter.onRefresh();
    }

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public String getUser() {
        return getArguments().getString(ARGS_USER);
    }

    @Override
    public boolean isMe() {
        return getArguments().getBoolean(ARGS_ME);
    }

    @Override
    public void showTopics(List<Topic> topics, boolean clean) {

        stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        mWrapper.hideFooter();
        if (clean) {
            adapter.clear();
        }
        int start = adapter.getItemCount();
        adapter.addTopics(topics);
        if (clean) {
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyItemRangeInserted(start, topics.size());
        }
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
    public int getItemOffset() {
        return adapter.getItemCount();
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
    public boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    @Override
    public void showRefreshErrorAndComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void clickItem(Topic topic, int position) {
        navigator.showTopicDetail(topic.id, topic.title);
    }

    @Override
    public void clickThumbUp(Topic topic, int position) {

    }

    @Override
    public void clickFavorite(Topic topic, int position) {

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

