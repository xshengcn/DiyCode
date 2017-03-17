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
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.customtabs.CustomTabActivityHelper;
import com.xshengcn.diycode.model.news.News;
import com.xshengcn.diycode.model.project.Project;
import com.xshengcn.diycode.ui.adapter.NewsAdapter;
import com.xshengcn.diycode.ui.adapter.ProjectAdapter;
import com.xshengcn.diycode.ui.iview.IProjectView;
import com.xshengcn.diycode.ui.presenter.ProjectPresenter;
import com.xshengcn.diycode.widget.itemdecoration.OffsetDecoration;
import com.xshengcn.diycode.widget.recyclerview.LoadMoreHandler;
import com.xshengcn.diycode.widget.recyclerview.LoadMoreWrapper;
import java.util.List;
import javax.inject.Inject;

public class ProjectFragment extends BaseFragment
    implements IProjectView, NewsAdapter.OnItemClickListener, LoadMoreHandler {
  private static final String BUNDLE_PROJECT = "ProjectFragment.project";

  //private static final String BUNDLE_DATA = "NewsFragment.data";

  @BindView(R.id.recycler_View) RecyclerView recyclerView;
  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.state_view) MultiStateView stateView;
  @BindDimen(R.dimen.spacing_xsmall) int space;
  @Inject ProjectAdapter adapter;
  @Inject ProjectPresenter presenter;

  private LoadMoreWrapper wrapper;

  public static ProjectFragment newInstance() {

    Bundle args = new Bundle();

    ProjectFragment fragment = new ProjectFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_news, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    swipeRefreshLayout.setOnRefreshListener(presenter::onRefresh);
    recyclerView.setPadding(0, space, 0, space);
    recyclerView.addItemDecoration(new OffsetDecoration(space, space, 0, 0));
    wrapper = new LoadMoreWrapper(this, adapter);
    recyclerView.setAdapter(wrapper);
    presenter.onAttach(this);
    presenter.onRefresh();
  }

  @Override public void onDestroyView() {
    presenter.onDetach();
    super.onDestroyView();
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    getComponent().inject(this);
  }

  @Override public void clickItem(News news, int position) {
    //NewsDetailActivity.start(getActivity(), news);
    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
    builder.setToolbarColor(Color.WHITE);
    builder.setShowTitle(true);

    CustomTabActivityHelper.openCustomTab(getActivity(), builder.build(), Uri.parse(news.address),
        null);
  }

  @Override public void clickThumbUp(News news, int position) {

  }

  @Override public void clickFavorite(News news, int position) {

  }

  @Override public void showProjects(List<Project> projects, boolean clean) {
    if (swipeRefreshLayout.isRefreshing()) {
      swipeRefreshLayout.setRefreshing(false);
    }
    stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    wrapper.hideFooter();

    if (clean) {
      adapter.clear();
      adapter.addProjects(projects);
      wrapper.notifyDataSetChanged();
    } else {
      int start = adapter.getItemCount();
      adapter.addProjects(projects);
      wrapper.notifyItemRangeInserted(start, projects.size());
    }
  }

  @Override public void showLoadMoreFailed() {
    wrapper.showLoadFailed();
  }

  @Override public void showLoadNoMore() {
    wrapper.showNoMore();
  }

  @Override public int getItemOffset() {
    return adapter.getItemCount();
  }

  @Override public void changeStateView(@MultiStateView.ViewState int state) {
    stateView.setViewState(state);
    if (state == MultiStateView.VIEW_STATE_ERROR) {
      ButterKnife.findById(stateView.getView(state), R.id.no_connection_retry)
          .setOnClickListener(v -> presenter.onRefresh());
    }
  }

  @Override public boolean isRefreshing() {
    return swipeRefreshLayout.isRefreshing();
  }

  @Override public void showRefreshErrorAndComplete() {
    swipeRefreshLayout.setRefreshing(false);
  }

  @Override public boolean canLoadMore() {
    return !swipeRefreshLayout.isRefreshing();
  }

  @Override public void loadMore() {
    presenter.loadMore();
  }
}
