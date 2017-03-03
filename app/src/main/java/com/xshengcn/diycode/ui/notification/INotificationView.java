package com.xshengcn.diycode.ui.notification;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.entity.user.Notification;
import java.util.List;

public interface INotificationView {

  int getItemOffset();

  boolean isRefreshing();

  void showRefreshErrorAndComplete();

  void showLoadMoreFailed();

  void showNotifications(List<Notification> notifications, boolean clean);

  void showNoMoreNotification();

  void changeStateView(@MultiStateView.ViewState int state);

}
