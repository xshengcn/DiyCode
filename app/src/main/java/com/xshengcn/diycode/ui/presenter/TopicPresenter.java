package com.xshengcn.diycode.ui.presenter;

import android.text.TextUtils;

import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.PreferencesHelper;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.ui.iview.ITopicView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class TopicPresenter extends BasePresenter<ITopicView> {

    private final DataManager mDataManager;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public TopicPresenter(DataManager dataManager, PreferencesHelper preferencesHelper) {
        this.mDataManager = dataManager;
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public void onAttach(ITopicView view) {
        super.onAttach(view);
        view.changeStateView(MultiStateView.VIEW_STATE_LOADING);
    }

    public void onRefresh() {
        getDisposable().clear();
        loadTopics(true);
    }

    public void loadMore() {
        loadTopics(false);
    }

    public void loadTopics(boolean clean) {
        final ITopicView view = getView();
        int offset = clean ? 0 : view.getItemOffset();
        Disposable disposable = getTopicDisposable(offset)
                .subscribe(topics -> handleOnNext(topics, clean), this::handleOnError);
        addDisposable(disposable);
    }

    private Single<List<Topic>> getTopicDisposable(int offset) {
        String s = getView().getUser();
        if (TextUtils.isEmpty(s)) {
            if (getView().isMe()) {
                return mPreferencesHelper.getUserDetail(mDataManager)
                        .flatMap(detail -> mDataManager.getUserTopics(detail.login, offset));
            } else {
                return mDataManager.getTopics(offset);
            }
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
        view.showTopics(topics, clean);

        if (view.getItemOffset() == 0 && topics.isEmpty()) {
            view.changeStateView(MultiStateView.VIEW_STATE_EMPTY);
            return;
        }

        if (topics.size() < DataManager.PAGE_LIMIT) {
            view.showLoadNoMore();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
