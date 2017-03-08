package com.xshengcn.diycode.ui.presenter;

import android.app.Activity;
import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.entity.topic.TopicAndReplies;
import com.xshengcn.diycode.entity.topic.TopicContent;
import com.xshengcn.diycode.entity.topic.TopicReply;
import com.xshengcn.diycode.ui.ActivityNavigator;
import com.xshengcn.diycode.ui.iview.ITopicDetailView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;

public class TopicDetailPresenter extends BasePresenter<ITopicDetailView> {

  private final DiyCodeClient client;
  private final ActivityNavigator navigator;

  @Inject public TopicDetailPresenter(DiyCodeClient client, ActivityNavigator navigator) {
    this.client = client;
    this.navigator = navigator;
  }

  @Override public void onAttach(ITopicDetailView view) {
    super.onAttach(view);
    onRefresh();
  }

  public void onRefresh() {
    final ITopicDetailView view = getView();
    if (!view.isRefreshing()) {
      view.changeStateView(MultiStateView.VIEW_STATE_LOADING);
    }
    loadTopicAndReplies();
  }

  public void loadTopicAndReplies() {

    Observable<TopicContent> topicObservable = client.getTopicDetail(getView().getTopic().id);
    Observable<List<TopicReply>> reliesObservable =
        client.getTopicReplies(getView().getTopic().id, 0);

    Disposable disposable = Observable.zip(topicObservable, reliesObservable, TopicAndReplies::new)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::loadTopicAndRepliesNext, this::loadTopicAndRepliesError);
    addDisposable(disposable);
  }

  private void loadTopicAndRepliesNext(TopicAndReplies topicAndReplies) {
    final ITopicDetailView view = getView();
    view.showTopicAndReplies(topicAndReplies);
    if (topicAndReplies.replies.isEmpty()
        || topicAndReplies.replies.size() < DiyCodeClient.PAGE_LIMIT) {
      view.showLoadNoMore();
    }
  }

  private void loadTopicAndRepliesError(Throwable throwable) {
    final ITopicDetailView view = getView();
    if (view.isRefreshing()) {
      view.showRefreshErrorAndComplete();
    } else {
      view.changeStateView(MultiStateView.VIEW_STATE_ERROR);
    }
  }

  public void loadMoreReplies() {
    final ITopicDetailView view = getView();
    Disposable disposable = client.getTopicReplies(view.getTopic().id, view.getItemOffset())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::loadMoreRepliesNext, this::loadMoreRepliesError);
    addDisposable(disposable);
  }

  private void loadMoreRepliesError(Throwable throwable) {
    final ITopicDetailView view = getView();
    view.showLoadMoreFailed();
  }

  private void loadMoreRepliesNext(List<TopicReply> replies) {
    final ITopicDetailView view = getView();
    if (replies == null || replies.isEmpty()) {
      view.showLoadNoMore();
    } else if (replies.size() < DiyCodeClient.PAGE_LIMIT) {
      view.showMoreReplies(replies);
      view.showLoadNoMore();
    } else {
      view.showMoreReplies(replies);
    }
  }

  @Override public void onDetach() {
    super.onDetach();
  }

  public void clickUserHeader(Activity activity, String user) {

  }

  public void clickReply(Activity activity) {
    navigator.showReply(activity, getView().getTopic().title, getView().getTopic().id);
  }
}
