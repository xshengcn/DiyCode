package com.xshengcn.diycode.ui.userfavorite.usertopic;

import com.xshengcn.diycode.entity.topic.Topic;
import java.util.List;

public interface IUserFavoriteView {

  String getUserLogin();

  void showTopics(List<Topic> topics);

  int getLoadOffset();
}
