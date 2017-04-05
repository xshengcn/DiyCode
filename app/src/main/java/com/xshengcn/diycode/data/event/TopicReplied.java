package com.xshengcn.diycode.data.event;

import com.xshengcn.diycode.data.model.topic.TopicReply;

public class TopicReplied {

    private TopicReply mTopicReply;

    public TopicReplied(TopicReply topicReply) {
        mTopicReply = topicReply;
    }

    public TopicReply getTopicReply() {
        return mTopicReply;
    }
}
