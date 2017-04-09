package com.xshengcn.diycode.ui.iview;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.data.model.topic.Topic;

import java.util.List;

public interface ITopicView {

    String getUser();

    boolean isMe();

    void showTopics(List<Topic> topics, boolean clean);

    void showLoadMoreFailed();

    void showLoadNoMore();

    int getItemOffset();

    void changeStateView(@MultiStateView.ViewState int state);

    boolean isRefreshing();

    void showRefreshErrorAndComplete();
}
