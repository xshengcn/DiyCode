package com.xshengcn.diycode.ui.login;

import android.app.Activity;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.entity.Error;
import com.xshengcn.diycode.ui.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import javax.inject.Inject;
import okhttp3.ResponseBody;

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
        .subscribe(userDetail -> getView().closeActivity(Activity.RESULT_OK), throwable -> {
        });
    addDisposable(disposable);
  }

  private void handleError(HttpException e) {
    if (e.response() != null) {
      ResponseBody responseBody = e.response().errorBody();
      try {
        String errorMsg = responseBody.string();
        Error error = new Gson().fromJson(errorMsg, Error.class);
        if (error != null) {
          getView().showError(error.errorDescription);
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }
}
