package com.xshengcn.diycode.ui.iview;

import com.xshengcn.diycode.data.model.topic.TopicNode;
import com.xshengcn.diycode.data.model.topic.TopicNodeCategory;

import java.util.List;
import java.util.Map;

public interface ITopicCreatorView {

    void showNodes(Map<TopicNodeCategory, List<TopicNode>> topicNodeCategoryListMap);

    int getNodeId();

    String getTopicTitle();

    String getTopicBody();

    void showProgressDialog();

    void hideProgressDialog();

    void intoTopicDetail(int id);
}
