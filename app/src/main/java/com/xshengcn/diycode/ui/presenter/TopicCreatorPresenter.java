package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.ui.iview.ITopicCreatorView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class TopicCreatorPresenter extends BasePresenter<ITopicCreatorView> {

  private final DataManager dataManager;

  @Inject public TopicCreatorPresenter(DataManager dataManager) {
    this.dataManager = dataManager;
  }

  @Override public void onAttach(ITopicCreatorView view) {
    super.onAttach(view);

    loadTopicNodes();
  }

  private void loadTopicNodes() {
    final ITopicCreatorView view = getView();
    Disposable disposable = dataManager.getTopicNodes()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(map -> view.showNodes(map), throwable -> {});
    addDisposable(disposable);
  }

  public void createTopic() {
    final ITopicCreatorView view = getView();
    Disposable disposable =
        dataManager.createTopic(view.getNodeId(), view.getTopicTitle(), view.getTopicBody())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable1 -> view.showProgressDialog())
            .subscribe(topic -> {

            }, throwable -> view.hideProgressDialog());
    addDisposable(disposable);
  }
}
