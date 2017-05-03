package com.xshengcn.diycode.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.news.News;
import com.xshengcn.diycode.ui.adapter.NewsAdapter;
import com.xshengcn.diycode.ui.iview.INewsView;
import com.xshengcn.diycode.ui.presenter.NewsPresenter;
import com.xshengcn.diycode.ui.widget.itemdecoration.OffsetDecoration;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreHandler;
import com.xshengcn.diycode.ui.widget.recyclerview.LoadMoreWrapper;
import com.xshengcn.diycode.util.BrowserUtil;
import com.xshengcn.diycode.util.customtabs.CustomTabActivityHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends BaseFragment
        implements INewsView, NewsAdapter.OnItemClickListener, LoadMoreHandler {

    private static final String BUNDLE_NEWS = "NewsFragment.news";

    //private static final String BUNDLE_DATA = "NewsFragment.data";

    @BindView(R.id.recycler_View)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.state_view)
    MultiStateView stateView;
    @BindDimen(R.dimen.spacing_xsmall)
    int space;
    @Inject
    NewsAdapter adapter;
    @Inject
    NewsPresenter presenter;

    private LoadMoreWrapper mWrapper;

    public static NewsFragment newInstance() {

        Bundle args = new Bundle();

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(presenter::onRefresh);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        getComponent().inject(this);
    }

    @Override
    public void clickItem(News news, int position) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Color.WHITE);
        builder.setShowTitle(true);

        CustomTabActivityHelper
                .openCustomTab(getActivity(), builder.build(), Uri.parse(news.address),
                        BrowserUtil::onCustomTabFallback);
    }

    @Override
    public void clickThumbUp(News news, int position) {

    }

    @Override
    public void clickFavorite(News news, int position) {

    }

    @Override
    public void showNewes(List<News> newses, boolean clean) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        mWrapper.hideFooter();

        if (clean) {
            adapter.clear();
            adapter.addNewses(newses);
            adapter.notifyDataSetChanged();
        } else {
            int start = adapter.getItemCount();
            adapter.addNewses(newses);
            adapter.notifyItemRangeInserted(start, newses.size());
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
    public boolean canLoadMore() {
        return !swipeRefreshLayout.isRefreshing();
    }

    @Override
    public void loadMore() {
        presenter.loadMore();
    }
}
