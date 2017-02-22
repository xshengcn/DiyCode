package com.xshengcn.diycode.ui.main;

import com.xshengcn.diycode.entity.user.UserDetail;

public interface IMainView {

  void toUserActivity();

  void toLoginActivity();

  void setupNavigationView(UserDetail user);

  void showNotificationMenuBadge(Boolean showBadge);
}
