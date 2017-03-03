package com.xshengcn.diycode.ui.usertopic;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.entity.topic.Topic;
import java.util.List;

public interface IUserTopicView {

  String getUserLogin();

  int getItemOffset();

  boolean isRefreshing();

  void showRefreshErrorAndComplete();

  void showLoadMoreFailed();

  void showTopics(List<Topic> topics, boolean clean);

  void showNoMoreTopic();

  void changeStateView(@MultiStateView.ViewState int state);

}
