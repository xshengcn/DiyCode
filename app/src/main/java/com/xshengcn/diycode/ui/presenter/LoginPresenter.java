package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.PreferencesHelper;
import com.xshengcn.diycode.data.model.user.UserDetail;
import com.xshengcn.diycode.ui.iview.ILoginView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenter<ILoginView> {

    private final DataManager mDataManager;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public LoginPresenter(DataManager dataManager, PreferencesHelper preferencesHelper) {
        this.mDataManager = dataManager;
        this.mPreferencesHelper = preferencesHelper;
    }

    @Override
    public void onAttach(ILoginView view) {
        super.onAttach(view);
    }

    public void login(String username, String password) {
        final ILoginView view = getView();
        view.showLoginDialog();
        Disposable disposable = mDataManager.login(username, password)
                .doOnNext(mPreferencesHelper::setToken)
                .flatMap(token -> mDataManager.getMe())
                .doOnNext(mPreferencesHelper::setUser)
                .subscribe(this::handleLoginSuccess, this::handleLoginError);
        addDisposable(disposable);
    }

    private void handleLoginSuccess(UserDetail userDetail) {
        final ILoginView view = getView();
        view.hideLoginDialog();
        view.loginSuccess();
    }

    private void handleLoginError(Throwable throwable) {
        final ILoginView view = getView();
        view.hideLoginDialog();
        view.loginError();
    }

}
