package com.xshengcn.diycode.ui.iview;

import com.xshengcn.diycode.data.model.user.UserDetail;

public interface IUserView {

    String getUserLogin();

    void updateUserDetail(UserDetail userDetail);

    boolean isMe();
}
