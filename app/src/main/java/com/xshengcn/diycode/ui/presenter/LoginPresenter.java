package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.ui.iview.ILoginView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class LoginPresenter extends BasePresenter<ILoginView> {

  private final DataManager dataManager;
  private final DiyCodePrefs prefs;

  @Inject public LoginPresenter(DataManager dataManager, DiyCodePrefs prefs) {
    this.dataManager = dataManager;
    this.prefs = prefs;
  }

  @Override public void onAttach(ILoginView view) {
    super.onAttach(view);
  }

  public void login() {
    Disposable disposable = dataManager.login(getView().getUsername(), getView().getPassword())
        .doOnNext(prefs::setToken)
        .flatMap(token -> dataManager.getMe())
        .doOnNext(user -> prefs.setUser(user))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(userDetail -> getView().closeActivity(), throwable -> {
        });
    addDisposable(disposable);
  }

}
