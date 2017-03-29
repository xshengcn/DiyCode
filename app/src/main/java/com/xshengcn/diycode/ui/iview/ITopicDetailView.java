package com.xshengcn.diycode.ui.iview;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.data.model.topic.TopicAndComments;
import com.xshengcn.diycode.data.model.topic.TopicComment;

import java.util.List;

public interface ITopicDetailView {


    Topic getTopic();

    void showTopicAndReplies(TopicAndComments topicAndComments);

    int getItemOffset();

    void showMoreReplies(List<TopicComment> topicReplies);

    void showLoadMoreFailed();

    void showLoadNoMore();


    void changeStateView(@MultiStateView.ViewState int state);

    boolean isRefreshing();

    void showRefreshErrorAndComplete();

}
