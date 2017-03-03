package com.xshengcn.diycode.ui.login;

import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.ui.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class LoginPresenter extends BasePresenter<ILoginView> {

  private final DiyCodeClient client;
  private final DiyCodePrefs prefs;

  @Inject public LoginPresenter(DiyCodeClient client, DiyCodePrefs prefs) {
    this.client = client;
    this.prefs = prefs;
  }

  @Override public void onAttach(ILoginView view) {
    super.onAttach(view);
  }

  public void login() {
    Disposable disposable = client.login(getView().getUsername(), getView().getPassword())
        .doOnNext(token -> prefs.setToken(token))
        .flatMap(token -> client.getMe())
        .doOnNext(user -> prefs.setUser(user))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(userDetail -> getView().closeActivity(), throwable -> {
        });
    addDisposable(disposable);
  }

}
