package com.xshengcn.diycode.ui.presenter;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.DiyCodePrefs;
import com.xshengcn.diycode.data.model.user.Notification;
import com.xshengcn.diycode.ui.iview.INotificationView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class NotificationPresenter extends BasePresenter<INotificationView> {

    private final DataManager mDataManager;
    private final DiyCodePrefs mPrefs;

    @Inject
    public NotificationPresenter(DataManager dataManager, DiyCodePrefs prefs) {
        this.mDataManager = dataManager;
        this.mPrefs = prefs;
    }

    @Override
    public void onAttach(INotificationView view) {
        super.onAttach(view);
        onRefresh();
    }

    public void onRefresh() {
        final INotificationView view = getView();
        if (!view.isRefreshing()) {
            view.changeStateView(MultiStateView.VIEW_STATE_LOADING);
        }
        loadUserTopics(true);
    }

    public void loadMore() {
        loadUserTopics(false);
    }

    private void loadUserTopics(boolean clean) {
        final INotificationView view = getView();
        int offset = clean ? 0 : view.getItemOffset();
        Disposable disposable = mDataManager.getNotifications(offset)
                .subscribe(notifications -> handleNext(notifications, clean), this::handleError);
        addDisposable(disposable);
    }

    private void handleNext(List<Notification> notifications, boolean clean) {
        final INotificationView view = getView();

        if (view.getItemOffset() == 0 && notifications.isEmpty()) {
            view.changeStateView(MultiStateView.VIEW_STATE_EMPTY);
            return;
        }

        view.showNotifications(notifications, clean);
        if (notifications.size() < DataManager.PAGE_LIMIT) {
            view.showNoMoreNotification();
        }
    }

    private void handleError(Throwable throwable) {
        final INotificationView view = getView();
        if (view.isRefreshing()) {
            view.showRefreshErrorAndComplete();
        } else if (view.getItemOffset() > 0) {
            view.showLoadMoreFailed();
        } else {
            view.changeStateView(MultiStateView.VIEW_STATE_ERROR);
        }
    }
}
