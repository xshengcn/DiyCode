package com.xshengcn.diycode.ui.main;

import android.app.Activity;
import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.api.event.UserDetailUpdate;
import com.xshengcn.diycode.api.event.UserLogin;
import com.xshengcn.diycode.ui.ActivityNavigator;
import com.xshengcn.diycode.ui.BasePresenter;
import com.xshengcn.diycode.util.RxBus;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class MainPresenter extends BasePresenter<IMainView> {

  private final DiyCodePrefs prefs;
  private final DiyCodeClient client;
  private final RxBus rxBus;
  private final ActivityNavigator navigator;

  @Inject public MainPresenter(DiyCodePrefs prefs, DiyCodeClient client, RxBus rxBus,
      ActivityNavigator navigator) {
    this.prefs = prefs;
    this.client = client;
    this.rxBus = rxBus;
    this.navigator = navigator;
  }

  @Override public void onAttach(IMainView view) {
    super.onAttach(view);

    Disposable userLogin = rxBus.toObservable()
        .filter(o -> o instanceof UserLogin)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> loadNotificationCount());
    addDisposable(userLogin);

    Disposable userDetailUpdate = rxBus.toObservable()
        .filter(o -> o instanceof UserDetailUpdate)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> checkUser());
    addDisposable(userDetailUpdate);

    checkUser();
    loadNotificationCount();
  }

  private void loadNotificationCount() {
    if (prefs.getToken() != null) {
      final IMainView view = getView();
      Disposable disposable = client.getNotificationsUnreadCount()
          .map(unread -> unread.count > 0)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(b -> view.showNotificationMenuBadge(b), throwable -> {
          });
      addDisposable(disposable);
    }
  }

  public void checkUser() {
    if (prefs.getUser() != null) {
      getView().setupNavigationView(prefs.getUser());
    }
  }

  public void clickHeader(Activity activity) {
    if (!checkNeedLogin(activity)) {
      navigator.showUser(activity);
    }
  }

  public void clickSearch(Activity activity) {
    navigator.showSearch(activity);
  }

  public void clickNotification(Activity activity) {
    if (!checkNeedLogin(activity)) {
      navigator.showNotification(activity);
    }
  }

  public void clickUserTopic(Activity activity) {
    if (!checkNeedLogin(activity)) {
      navigator.showUserTopics(activity, prefs.getUser().login);
    }
  }

  public void clickUserFavorite(Activity activity) {
    if (!checkNeedLogin(activity)) {
      navigator.showUserFavorites(activity, prefs.getUser().login);
    }
  }

  public void clickUserReply(Activity activity) {
    if (!checkNeedLogin(activity)) {
      navigator.showUserReplies(activity, prefs.getUser().login);
    }
  }

  private boolean checkNeedLogin(Activity activity) {
    if (prefs.getUser() == null) {
      navigator.showLogin(activity);
      return true;
    }
    return false;
  }
}
