package com.xshengcn.diycode.entity.topic;

import java.util.List;

public class TopicAndReplies {

  public TopicContent content;
  public List<TopicReply> replies;

  public TopicAndReplies() {
  }

  public TopicAndReplies(TopicContent content, List<TopicReply> replies) {
    this.content = content;
    this.replies = replies;
  }
}
