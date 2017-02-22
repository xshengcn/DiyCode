package com.xshengcn.diycode.ui.topicdetail;

import com.xshengcn.diycode.entity.topic.TopicAndReplies;
import com.xshengcn.diycode.entity.topic.TopicReply;
import java.util.List;

public interface ITopicDetailView {

  int getTopicId();

  void showTopicAndReplies(TopicAndReplies topicAndReplies);

  int getReplyOffset();

  void addReplies(List<TopicReply> topicReplies);
}
