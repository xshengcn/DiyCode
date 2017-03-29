package com.xshengcn.diycode.data.model.topic;

import java.util.List;

public class TopicAndComments {

    public TopicDetail detail;
    public List<TopicComment> comments;

    public TopicAndComments() {
    }

    public TopicAndComments(TopicDetail detail, List<TopicComment> comments) {
        this.detail = detail;
        this.comments = comments;
    }
}
