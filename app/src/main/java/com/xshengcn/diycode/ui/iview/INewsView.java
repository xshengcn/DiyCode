package com.xshengcn.diycode.ui.iview;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.model.news.News;
import java.util.List;

public interface INewsView {

  void showNewes(List<News> newses, boolean clean);

  void showLoadMoreFailed();

  void showLoadNoMore();

  int getItemOffset();

  void changeStateView(@MultiStateView.ViewState int state);

  boolean isRefreshing();

  void showRefreshErrorAndComplete();

}
