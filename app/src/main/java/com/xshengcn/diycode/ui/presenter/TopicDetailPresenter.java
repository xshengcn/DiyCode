package com.xshengcn.diycode.ui.presenter;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.model.topic.TopicAndReplies;
import com.xshengcn.diycode.data.model.topic.TopicReply;
import com.xshengcn.diycode.ui.iview.ITopicDetailView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class TopicDetailPresenter extends BasePresenter<ITopicDetailView> {

    private final DataManager mDataManager;

    @Inject
    public TopicDetailPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }


    public void onRefresh() {
        final ITopicDetailView view = getView();
        if (!view.isRefreshing()) {
            view.changeStateView(MultiStateView.VIEW_STATE_LOADING);
        }

        addDisposable(mDataManager.getTopicAndComments(getView().getTopic().id)
                .subscribe(this::loadTopicAndRepliesNext, this::loadTopicAndRepliesError));
    }


    private void loadTopicAndRepliesNext(TopicAndReplies topicAndReplies) {
        final ITopicDetailView view = getView();
        view.showTopicAndReplies(topicAndReplies);
        if (topicAndReplies.replies.isEmpty()
                || topicAndReplies.replies.size() < DataManager.PAGE_LIMIT) {
            view.showLoadNoMore();
        }
    }

    private void loadTopicAndRepliesError(Throwable throwable) {
        final ITopicDetailView view = getView();
        if (view.isRefreshing()) {
            view.showRefreshErrorAndComplete();
        } else {
            view.changeStateView(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    public void loadMoreReplies() {
        final ITopicDetailView view = getView();
        Disposable disposable =
                mDataManager.getTopicReplies(view.getTopic().id, view.getItemOffset())
                        .subscribe(this::loadMoreRepliesNext, this::loadMoreRepliesError);
        addDisposable(disposable);
    }

    private void loadMoreRepliesError(Throwable throwable) {
        final ITopicDetailView view = getView();
        view.showLoadMoreFailed();
    }

    private void loadMoreRepliesNext(List<TopicReply> replies) {
        final ITopicDetailView view = getView();
        if (replies == null || replies.isEmpty()) {
            view.showLoadNoMore();
        } else if (replies.size() < DataManager.PAGE_LIMIT) {
            view.showMoreReplies(replies);
            view.showLoadNoMore();
        } else {
            view.showMoreReplies(replies);
        }
    }

}
