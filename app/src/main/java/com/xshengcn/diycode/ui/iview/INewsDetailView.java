package com.xshengcn.diycode.ui.iview;

import com.xshengcn.diycode.data.model.news.NewsReply;

import java.util.List;

public interface INewsDetailView {

    int getNewsId();

    void showReplies(List<NewsReply> replies);
}
