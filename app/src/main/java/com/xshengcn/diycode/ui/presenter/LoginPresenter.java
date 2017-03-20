package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.DiyCodePrefs;
import com.xshengcn.diycode.ui.iview.ILoginView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenter<ILoginView> {

    private final DataManager mDataManager;
    private final DiyCodePrefs mPrefs;

    @Inject
    public LoginPresenter(DataManager dataManager, DiyCodePrefs prefs) {
        this.mDataManager = dataManager;
        this.mPrefs = prefs;
    }

    @Override
    public void onAttach(ILoginView view) {
        super.onAttach(view);
    }

    public void login() {
        Disposable disposable = mDataManager.login(getView().getUsername(), getView().getPassword())
                .doOnNext(mPrefs::setToken)
                .flatMap(token -> mDataManager.getMe())
                .doOnNext(user -> mPrefs.setUser(user))
                .subscribe(userDetail -> getView().closeActivity(), throwable -> {
                });
        addDisposable(disposable);
    }

}
