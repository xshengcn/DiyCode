package com.xshengcn.diycode.ui.presenter;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.model.news.News;
import com.xshengcn.diycode.ui.iview.INewsView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;

public class NewsPresenter extends BasePresenter<INewsView> {

  private final DataManager dataManager;

  @Inject public NewsPresenter(DataManager dataManager) {
    this.dataManager = dataManager;
  }

  @Override public void onAttach(INewsView view) {
    super.onAttach(view);
  }

  public void onRefresh() {
    final INewsView view = getView();
    if (!view.isRefreshing()) {
      view.changeStateView(MultiStateView.VIEW_STATE_LOADING);
    }
    loadTopics(true);
  }

  public void loadMore() {
    loadTopics(false);
  }

  private void loadTopics(boolean clean) {
    final INewsView view = getView();
    int offset = clean ? 0 : view.getItemOffset();
    Disposable disposable = dataManager.getAllNewses(offset)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(topics -> handleOnNext(topics, clean), this::handleOnError);
    addDisposable(disposable);
  }

  private void handleOnError(Throwable throwable) {
    final INewsView view = getView();
    if (view.isRefreshing()) {
      view.showRefreshErrorAndComplete();
    } else if (view.getItemOffset() > 0) {
      view.showLoadMoreFailed();
    } else {
      view.changeStateView(MultiStateView.VIEW_STATE_ERROR);
    }
  }

  private void handleOnNext(List<News> newses, boolean clean) {
    final INewsView view = getView();
    if (view.getItemOffset() == 0 && newses.isEmpty()) {
      view.changeStateView(MultiStateView.VIEW_STATE_EMPTY);
      return;
    }

    view.showNewes(newses, clean);
    if (newses.size() < DataManager.PAGE_LIMIT) {
      view.showLoadNoMore();
    }
  }

  @Override public void onDetach() {
    super.onDetach();
  }
}
