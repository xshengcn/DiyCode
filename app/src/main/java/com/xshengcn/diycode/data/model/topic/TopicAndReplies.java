package com.xshengcn.diycode.data.model.topic;

import java.util.List;

public class TopicAndReplies {

    public TopicDetail detail;
    public List<TopicReply> replies;

    public TopicAndReplies() {
    }

    public TopicAndReplies(TopicDetail detail, List<TopicReply> replies) {
        this.detail = detail;
        this.replies = replies;
    }

}
