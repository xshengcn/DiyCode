package com.xshengcn.diycode.ui.iview;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.model.topic.Topic;
import java.util.List;

public interface IUserFavoriteView {

  String getUserLogin();

  int getItemOffset();

  boolean isRefreshing();

  void showRefreshErrorAndComplete();

  void showLoadMoreFailed();

  void showTopics(List<Topic> topics, boolean clean);

  void showNoMoreTopic();

  void changeStateView(@MultiStateView.ViewState int state);
}
