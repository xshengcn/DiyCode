package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.ui.iview.INewsDetailView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import javax.inject.Inject;

public class NewsDetailPresenter extends BasePresenter<INewsDetailView> {

  private final DataManager dataManager;

  @Inject public NewsDetailPresenter(DataManager dataManager) {
    this.dataManager = dataManager;
  }

  @Override public void onAttach(INewsDetailView view) {
    super.onAttach(view);

    getReplies(0);
  }

  private void getReplies(int offset) {
    dataManager.getNewsReplies(getView().getNewsId(), offset)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(replies -> getView().showReplies(replies),
            throwable -> handleThrowable(throwable));
  }

  private void handleThrowable(Throwable throwable) {

  }
}
