package com.xshengcn.diycode.ui.iview;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.model.project.Project;
import java.util.List;

public interface IProjectView {

  void showProjects(List<Project> projects, boolean clean);

  void showLoadMoreFailed();

  void showLoadNoMore();

  int getItemOffset();

  void changeStateView(@MultiStateView.ViewState int state);

  boolean isRefreshing();

  void showRefreshErrorAndComplete();
}
