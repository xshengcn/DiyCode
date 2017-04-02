package com.xshengcn.diycode.ui.iview;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.data.model.topic.TopicAndReplies;
import com.xshengcn.diycode.data.model.topic.TopicReply;

import java.util.List;

public interface ITopicDetailView {


    int getTopicId();

    String getTopicTitle();

    void showTopicAndReplies(TopicAndReplies topicAndReplies);

    int getItemOffset();

    void showMoreReplies(List<TopicReply> topicReplies);

    void showLoadMoreFailed();

    void showLoadNoMore();


    void changeStateView(@MultiStateView.ViewState int state);

    boolean isRefreshing();

    void showRefreshErrorAndComplete();

}
