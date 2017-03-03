package com.xshengcn.diycode.ui.topicdetail;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.entity.topic.Topic;
import com.xshengcn.diycode.entity.topic.TopicAndReplies;
import com.xshengcn.diycode.entity.topic.TopicReply;
import java.util.List;

public interface ITopicDetailView {


  Topic getTopic();

  void showTopicAndReplies(TopicAndReplies topicAndReplies);

  int getItemOffset();

  void showMoreReplies(List<TopicReply> topicReplies);

  void showLoadMoreFailed();

  void showLoadNoMore();


  void changeStateView(@MultiStateView.ViewState int state);

  boolean isRefreshing();

  void showRefreshErrorAndComplete();

}
