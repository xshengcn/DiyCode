package com.xshengcn.diycode.ui.main.topic;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.entity.topic.Topic;
import java.util.List;

public interface ITopicView {

  void showTopics(List<Topic> topics, boolean clean);

  void showLoadMoreFailed();

  void showLoadNoMore();

  int getItemOffset();

  void changeStateView(@MultiStateView.ViewState int state);

  boolean isRefreshing();

  void showRefreshErrorAndComplete();
}
