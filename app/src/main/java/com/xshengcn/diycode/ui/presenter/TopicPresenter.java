package com.xshengcn.diycode.ui.presenter;

import android.text.TextUtils;

import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.ui.iview.ITopicView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class TopicPresenter extends BasePresenter<ITopicView> {

    private final DataManager mDataManager;

    @Inject
    public TopicPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void onAttach(ITopicView view) {
        super.onAttach(view);
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
        Disposable disposable = getTopicDisposable(offset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topics -> handleOnNext(topics, clean), this::handleOnError);
        addDisposable(disposable);
    }

    private Observable<List<Topic>> getTopicDisposable(int offset) {
        String s = getView().getUser();
        if (TextUtils.isEmpty(s)) {
            return mDataManager.getTopics(offset);
        } else {
            return mDataManager.getUserTopics(s, offset);
        }
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
        if (topics.size() < DataManager.PAGE_LIMIT) {
            view.showLoadNoMore();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
