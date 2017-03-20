package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.ui.iview.INewsDetailView;

import javax.inject.Inject;

public class NewsDetailPresenter extends BasePresenter<INewsDetailView> {

    private final DataManager mDataManager;

    @Inject
    public NewsDetailPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void onAttach(INewsDetailView view) {
        super.onAttach(view);

        getReplies(0);
    }

    private void getReplies(int offset) {
        mDataManager.getNewsReplies(getView().getNewsId(), offset)
                .subscribe(replies -> getView().showReplies(replies),
                        throwable -> handleThrowable(throwable));
    }

    private void handleThrowable(Throwable throwable) {

    }
}
