package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.PreferencesHelper;
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

    public void login() {
        Disposable disposable = mDataManager.login(getView().getUsername(), getView().getPassword())
                .doOnNext(mPreferencesHelper::setToken)
                .flatMap(token -> mDataManager.getMe())
                .doOnNext(mPreferencesHelper::setUser)
                .subscribe(userDetail -> getView().closeActivity(), throwable -> {
                });
        addDisposable(disposable);
    }

}
