package com.xshengcn.diycode.ui.presenter;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.DiyCodePrefs;
import com.xshengcn.diycode.data.model.user.UserReply;
import com.xshengcn.diycode.ui.iview.IUserReplyView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class UserReplyPresenter extends BasePresenter<IUserReplyView> {

    private final DataManager mDataManager;
    private final DiyCodePrefs mPrefs;

    @Inject
    public UserReplyPresenter(DataManager dataManager, DiyCodePrefs prefs) {
        this.mDataManager = dataManager;
        this.mPrefs = prefs;
    }

    @Override
    public void onAttach(IUserReplyView view) {
        super.onAttach(view);
        onRefresh();
    }

    public void onRefresh() {
        final IUserReplyView view = getView();
        if (!view.isRefreshing()) {
            view.changeStateView(MultiStateView.VIEW_STATE_LOADING);
        }
        loadUserTopics(true);
    }

    public void loadMore() {
        loadUserTopics(false);
    }

    private void loadUserTopics(boolean clean) {
        final IUserReplyView view = getView();
        int offset = clean ? 0 : view.getItemOffset();
        Disposable disposable = mDataManager.getUserReplies(view.getUser(), offset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topics -> handleNext(topics, clean), this::handleError);
        addDisposable(disposable);
    }

    private void handleNext(List<UserReply> replies, boolean clean) {
        final IUserReplyView view = getView();

        if (view.getItemOffset() == 0 && replies.isEmpty()) {
            view.changeStateView(MultiStateView.VIEW_STATE_EMPTY);
            return;
        }

        view.showUserReplies(replies, clean);
        if (replies.size() < DataManager.PAGE_LIMIT) {
            view.showNoMoreTopic();
        }
    }

    private void handleError(Throwable throwable) {
        final IUserReplyView view = getView();
        if (view.isRefreshing()) {
            view.showRefreshErrorAndComplete();
        } else if (view.getItemOffset() > 0) {
            view.showLoadMoreFailed();
        } else {
            view.changeStateView(MultiStateView.VIEW_STATE_ERROR);
        }
    }
}
