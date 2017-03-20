package com.xshengcn.diycode.ui.presenter;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.DiyCodePrefs;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.ui.iview.IUserFavoriteView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class UserFavoritePresenter extends BasePresenter<IUserFavoriteView> {

    private final DataManager mDataManager;
    private final DiyCodePrefs mPrefs;

    @Inject
    public UserFavoritePresenter(DataManager dataManager, DiyCodePrefs prefs) {
        this.mDataManager = dataManager;
        this.mPrefs = prefs;
    }

    @Override
    public void onAttach(IUserFavoriteView view) {
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
        Disposable disposable = mDataManager.getUserFavorites(view.getUserLogin(), offset)
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
