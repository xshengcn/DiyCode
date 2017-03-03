package com.xshengcn.diycode.ui.main.topic;

import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.entity.topic.Topic;
import com.xshengcn.diycode.ui.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;

public class TopicPresenter extends BasePresenter<ITopicView> {

  private final DiyCodeClient client;

  @Inject public TopicPresenter(DiyCodeClient client) {
    this.client = client;
  }

  @Override public void onAttach(ITopicView view) {
    super.onAttach(view);
    onRefresh();
  }

  public void onRefresh() {
    final ITopicView view = getView();
    if (!view.isRefreshing()) {
      view.changeStateView(MultiStateView.VIEW_STATE_LOADING);
    }
    loadTopics(true);
  }

  public void loadMore() {
    loadTopics(false);
  }

  public void loadTopics(boolean clean) {
    final ITopicView view = getView();
    int offset = clean ? 0 : view.getItemOffset();
    Disposable disposable = client.getTopics(offset)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(topics -> handleOnNext(topics, clean), this::handleOnError);
    addDisposable(disposable);
  }

  private void handleOnError(Throwable throwable) {
    Logger.d(throwable);
    final ITopicView view = getView();
    if (view.isRefreshing()) {
      view.showRefreshErrorAndComplete();
    } else if (view.getItemOffset() > 0) {
      view.showLoadMoreFailed();
    } else {
      view.changeStateView(MultiStateView.VIEW_STATE_ERROR);
    }
  }

  private void handleOnNext(List<Topic> topics, boolean clean) {
    final ITopicView view = getView();
    if (view.getItemOffset() == 0 && topics.isEmpty()) {
      view.changeStateView(MultiStateView.VIEW_STATE_EMPTY);
      return;
    }

    view.showTopics(topics, clean);
    if (topics.size() < DiyCodeClient.PAGE_LIMIT) {
      view.showLoadNoMore();
    }
  }

  @Override public void onDetach() {
    super.onDetach();
  }
}
