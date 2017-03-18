package com.xshengcn.diycode.ui.iview;

import com.xshengcn.diycode.data.model.user.UserDetail;

public interface IMainView {

    void setupNavigationView(UserDetail user);

    void showNotificationMenuBadge(Boolean showBadge);

}
