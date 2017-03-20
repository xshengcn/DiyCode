package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.ui.iview.ITopicCreatorView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class TopicCreatorPresenter extends BasePresenter<ITopicCreatorView> {

    private final DataManager mDataManager;

    @Inject
    public TopicCreatorPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void onAttach(ITopicCreatorView view) {
        super.onAttach(view);

        loadTopicNodes();
    }

    private void loadTopicNodes() {
        final ITopicCreatorView view = getView();
        Disposable disposable = mDataManager.getTopicNodes()
                .subscribe(map -> view.showNodes(map), throwable -> {
                });
        addDisposable(disposable);
    }

    public void createTopic() {
        final ITopicCreatorView view = getView();
        Disposable disposable = mDataManager
                .createTopic(view.getNodeId(), view.getTopicTitle(), view.getTopicBody())
                .doOnSubscribe(disposable1 -> view.showProgressDialog())
                .subscribe(topic -> {

                }, throwable -> view.hideProgressDialog());
        addDisposable(disposable);
    }
}
