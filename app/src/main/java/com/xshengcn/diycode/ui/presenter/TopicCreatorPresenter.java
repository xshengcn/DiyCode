package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.model.topic.TopicDetail;
import com.xshengcn.diycode.ui.iview.ITopicCreatorView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class TopicCreatorPresenter extends BasePresenter<ITopicCreatorView> {

    private final DataManager mDataManager;

    @Inject
    public TopicCreatorPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    public void loadTopicNodes() {
        final ITopicCreatorView view = getView();
        Disposable disposable = mDataManager.getTopicNodes()
                .subscribe(view::showNodes, throwable -> {
                });
        addDisposable(disposable);
    }

    public void createTopic() {
        final ITopicCreatorView view = getView();
        view.showProgressDialog();
        Disposable disposable = mDataManager
                .createTopic(view.getNodeId(), view.getTopicTitle(), view.getTopicBody())
                .doOnSubscribe(disposable1 -> view.showProgressDialog())
                .subscribe(this::createTopicSuccess, this::createTopicFailed);
        addDisposable(disposable);
    }

    private void createTopicSuccess(TopicDetail topicDetail) {
        final ITopicCreatorView view = getView();
        view.hideProgressDialog();
        view.intoTopicDetail(topicDetail.id);
    }

    private void createTopicFailed(Throwable throwable) {
        final ITopicCreatorView view = getView();
        view.hideProgressDialog();
    }
}
