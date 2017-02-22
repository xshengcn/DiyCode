package com.xshengcn.diycode.ui.userfavorite.usertopic;

import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.ui.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class UserFavoritePresenter extends BasePresenter<IUserFavoriteView> {

  private final DiyCodeClient client;
  private final DiyCodePrefs prefs;

  @Inject public UserFavoritePresenter(DiyCodeClient client, DiyCodePrefs prefs) {
    this.client = client;
    this.prefs = prefs;
  }

  @Override public void onAttach(IUserFavoriteView view) {
    super.onAttach(view);

    loadUserFavorites(true);
  }

  public void loadUserFavorites(boolean clean) {
    final IUserFavoriteView view = getView();
    Disposable disposable = client.getUserFavorites(view.getUserLogin(), view.getLoadOffset())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(topics -> getView().showTopics(topics), throwable -> {
        });
    addDisposable(disposable);
  }
}
