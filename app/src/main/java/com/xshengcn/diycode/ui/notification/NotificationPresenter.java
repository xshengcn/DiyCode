package com.xshengcn.diycode.ui.notification;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.entity.user.Notification;
import com.xshengcn.diycode.ui.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;

public class NotificationPresenter extends BasePresenter<INotificationView> {

  private final DiyCodeClient client;
  private final DiyCodePrefs prefs;

  @Inject public NotificationPresenter(DiyCodeClient client, DiyCodePrefs prefs) {
    this.client = client;
    this.prefs = prefs;
  }

  @Override public void onAttach(INotificationView view) {
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
    Disposable disposable = client.getNotifications(offset)
        .observeOn(AndroidSchedulers.mainThread())
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
    if (notifications.size() < DiyCodeClient.PAGE_LIMIT) {
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
