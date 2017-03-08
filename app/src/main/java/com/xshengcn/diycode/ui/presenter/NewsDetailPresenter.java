package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.ui.iview.INewsDetailView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import javax.inject.Inject;

public class NewsDetailPresenter extends BasePresenter<INewsDetailView> {

  private final DiyCodeClient client;

  @Inject public NewsDetailPresenter(DiyCodeClient client) {
    this.client = client;
  }

  @Override public void onAttach(INewsDetailView view) {
    super.onAttach(view);

    getReplies(0);
  }

  private void getReplies(int offset) {
    client.getNewsReplies(getView().getNewsId(), offset)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(replies -> getView().showReplies(replies),
            throwable -> handleThrowable(throwable));
  }

  private void handleThrowable(Throwable throwable) {

  }
}
