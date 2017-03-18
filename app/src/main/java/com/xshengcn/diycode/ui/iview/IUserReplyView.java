package com.xshengcn.diycode.ui.iview;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.data.model.user.UserReply;

import java.util.List;

public interface IUserReplyView {

    String getUser();

    int getItemOffset();

    boolean isRefreshing();

    void showRefreshErrorAndComplete();

    void showLoadMoreFailed();

    void showUserReplies(List<UserReply> replies, boolean clean);

    void showNoMoreTopic();

    void changeStateView(@MultiStateView.ViewState int state);

}
