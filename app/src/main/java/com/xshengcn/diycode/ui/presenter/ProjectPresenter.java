package com.xshengcn.diycode.ui.presenter;

import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.model.project.Project;
import com.xshengcn.diycode.ui.iview.IProjectView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ProjectPresenter extends BasePresenter<IProjectView> {

    private final DataManager mDataManager;

    @Inject
    public ProjectPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void onAttach(IProjectView view) {
        super.onAttach(view);
    }

    public void onRefresh() {
        final IProjectView view = getView();
        if (!view.isRefreshing()) {
            view.changeStateView(MultiStateView.VIEW_STATE_LOADING);
        }
        loadTopics(true);
    }

    public void loadMore() {
        loadTopics(false);
    }

    public void loadTopics(boolean clean) {
        final IProjectView view = getView();
        int offset = clean ? 0 : view.getItemOffset();
        Disposable disposable = mDataManager.getProjects(offset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(projects -> handleOnNext(projects, clean), this::handleOnError);
        addDisposable(disposable);
    }

    private void handleOnError(Throwable throwable) {
        Logger.d(throwable);
        final IProjectView view = getView();
        if (view.isRefreshing()) {
            view.showRefreshErrorAndComplete();
        } else if (view.getItemOffset() > 0) {
            view.showLoadMoreFailed();
        } else {
            view.changeStateView(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    private void handleOnNext(List<Project> projects, boolean clean) {
        final IProjectView view = getView();
        if (view.getItemOffset() == 0 && projects.isEmpty()) {
            view.changeStateView(MultiStateView.VIEW_STATE_EMPTY);
            return;
        }

        view.showProjects(projects, clean);
        if (projects.size() < DataManager.PAGE_LIMIT) {
            view.showLoadNoMore();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
