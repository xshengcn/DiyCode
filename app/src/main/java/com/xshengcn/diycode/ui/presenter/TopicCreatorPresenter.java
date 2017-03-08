package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.entity.topic.Topic;
import com.xshengcn.diycode.ui.iview.ITopicCreatorView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import javax.inject.Inject;

public class TopicCreatorPresenter extends BasePresenter<ITopicCreatorView> {

  private final DiyCodeClient client;

  @Inject public TopicCreatorPresenter(DiyCodeClient client) {
    this.client = client;
  }

  @Override public void onAttach(ITopicCreatorView view) {
    super.onAttach(view);

    loadTopicNodes();
  }

  private void loadTopicNodes() {
    final ITopicCreatorView view = getView();
    Disposable disposable = client.getTopicNodes()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(map -> view.showNodes(map), throwable -> {});
    addDisposable(disposable);
  }

  public void createTopic() {
    final ITopicCreatorView view = getView();
    Disposable disposable =
        client.createTopic(view.getNodeId(), view.getTopicTitle(), view.getTopicBody())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable1 -> view.showProgressDialog())
            .subscribe(topic -> {

            }, throwable -> view.hideProgressDialog());
    addDisposable(disposable);
  }
}
