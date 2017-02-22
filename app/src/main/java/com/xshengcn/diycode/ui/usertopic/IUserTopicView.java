package com.xshengcn.diycode.ui.usertopic;

import com.xshengcn.diycode.entity.topic.Topic;
import java.util.List;

public interface IUserTopicView {

  String getUserLogin();

  void showTopics(List<Topic> topics);

  int getLoadOffset();
}
