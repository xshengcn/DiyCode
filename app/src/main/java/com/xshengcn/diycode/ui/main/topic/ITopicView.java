package com.xshengcn.diycode.ui.main.topic;

import com.xshengcn.diycode.entity.topic.Topic;
import java.util.List;

public interface ITopicView {

  void showTopics(List<Topic> topics, boolean clean);

  void showNoMore();

  int getTopicCount();

  void showProgressBar();

  void showNoConnection();

  void showLoadMoreError();
}
