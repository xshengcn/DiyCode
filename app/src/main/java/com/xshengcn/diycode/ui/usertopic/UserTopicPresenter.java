package com.xshengcn.diycode.ui.usertopic;

import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.ui.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class UserTopicPresenter extends BasePresenter<IUserTopicView> {

  private final DiyCodeClient client;
  private final DiyCodePrefs prefs;

  @Inject public UserTopicPresenter(DiyCodeClient client, DiyCodePrefs prefs) {
    this.client = client;
    this.prefs = prefs;
  }

  @Override public void onAttach(IUserTopicView view) {
    super.onAttach(view);

    client.getNotifications(0).subscribe();
    client.getNotificationsUnreadCount().subscribe();
    //loadUserTopics(true);
  }

  public void loadUserTopics(boolean clean) {
    final IUserTopicView view = getView();
    Disposable disposable = client.getUserTopics(view.getUserLogin(), view.getLoadOffset())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(topics -> getView().showTopics(topics), throwable -> {
        });
    addDisposable(disposable);
  }
}
