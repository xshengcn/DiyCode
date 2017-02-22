package com.xshengcn.diycode.ui.main;

import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.ui.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class MainPresenter extends BasePresenter<IMainView> {

  private final DiyCodePrefs prefs;
  private final DiyCodeClient client;

  @Inject public MainPresenter(DiyCodePrefs prefs, DiyCodeClient client) {
    this.prefs = prefs;
    this.client = client;
  }

  @Override public void onAttach(IMainView view) {
    super.onAttach(view);

    checkUser();

    loadNotificationCount();
  }

  private void loadNotificationCount() {
    final IMainView view = getView();
    Disposable disposable = client.getNotificationsUnreadCount()
        .map(unread -> unread.count > 0)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(b -> view.showNotificationMenuBadge(b), throwable -> {
        });
    addDisposable(disposable);
  }

  public void clickHeader() {
    if (prefs.getToken() != null) {
      getView().toUserActivity();
    } else {
      getView().toLoginActivity();
    }
  }

  public void checkUser() {
    if (prefs.getUser() != null) {
      getView().setupNavigationView(prefs.getUser());
    }
  }
}
