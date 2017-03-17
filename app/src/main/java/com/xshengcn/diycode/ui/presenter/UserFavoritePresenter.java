package com.xshengcn.diycode.ui.presenter;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.model.topic.Topic;
import com.xshengcn.diycode.ui.iview.IUserFavoriteView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;

public class UserFavoritePresenter extends BasePresenter<IUserFavoriteView> {

  private final DataManager dataManager;
  private final DiyCodePrefs prefs;

  @Inject public UserFavoritePresenter(DataManager dataManager, DiyCodePrefs prefs) {
    this.dataManager = dataManager;
    this.prefs = prefs;
  }

  @Override public void onAttach(IUserFavoriteView view) {
    super.onAttach(view);
    onRefresh();
  }

  public void onRefresh() {
    final IUserFavoriteView view = getView();
    if (!view.isRefreshing()) {
      view.changeStateView(MultiStateView.VIEW_STATE_LOADING);
    }
    loadUserTopics(true);
  }

  public void loadMore() {
    loadUserTopics(false);
  }

  private void loadUserTopics(boolean clean) {
    final IUserFavoriteView view = getView();
    int offset = clean ? 0 : view.getItemOffset();
    Disposable disposable = dataManager.getUserFavorites(view.getUserLogin(), offset)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(topics -> handleNext(topics, clean), this::handleError);
    addDisposable(disposable);
  }

  private void handleNext(List<Topic> topics, boolean clean) {
    final IUserFavoriteView view = getView();

    if (view.getItemOffset() == 0 && topics.isEmpty()) {
      view.changeStateView(MultiStateView.VIEW_STATE_EMPTY);
      return;
    }

    view.showTopics(topics, clean);
    if (topics.size() < DataManager.PAGE_LIMIT) {
      view.showNoMoreTopic();
    }
  }

  private void handleError(Throwable throwable) {
    final IUserFavoriteView view = getView();
    if (view.isRefreshing()) {
      view.showRefreshErrorAndComplete();
    } else if (view.getItemOffset() > 0) {
      view.showLoadMoreFailed();
    } else {
      view.changeStateView(MultiStateView.VIEW_STATE_ERROR);
    }
  }
}
