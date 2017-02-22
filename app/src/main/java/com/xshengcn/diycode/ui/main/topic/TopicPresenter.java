package com.xshengcn.diycode.ui.main.topic;

import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.entity.topic.Topic;
import com.xshengcn.diycode.ui.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class TopicPresenter extends BasePresenter<ITopicView> {

  private final DiyCodeClient client;

  @Inject public TopicPresenter(DiyCodeClient client) {
    this.client = client;
  }

  @Override public void onAttach(ITopicView view) {
    super.onAttach(view);
    loadTopics(true);
  }

  public void refresh() {
    loadTopics(true);
  }

  public void loadMore() {
    loadTopics(false);
  }

  public void loadTopics(boolean clean) {
    final ITopicView view = getView();
    int offset = clean ? 0 : view.getTopicCount();
    if (view.getTopicCount() == 0) {
      view.showProgressBar();
    }
    Disposable disposable = client.getTopics(offset)
        //.delay(2000, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(topics -> handleOnNext(topics, clean), throwable -> handleOnError(throwable),
            () -> handleOnComplete());
    addDisposable(disposable);
  }

  private void handleOnComplete() {
    // nop
  }


  private void handleOnError(Throwable throwable) {
    final ITopicView view = getView();
    if (view.getTopicCount() == 0) {
      view.showNoConnection();
    } else {
      view.showLoadMoreError();
    }
  }

  private void handleOnNext(List<Topic> topics, boolean clean) {
    final ITopicView view = getView();
    view.showTopics(topics, clean);
    if (topics.size() < DiyCodeClient.PAGE_LIMIT) {
      view.showNoMore();
    }
  }

  @Override public void onDetach() {
    super.onDetach();
  }
}
