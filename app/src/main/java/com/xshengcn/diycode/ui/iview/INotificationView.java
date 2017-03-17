package com.xshengcn.diycode.ui.iview;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.model.user.Notification;
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
