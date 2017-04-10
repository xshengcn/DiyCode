package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.PreferencesHelper;
import com.xshengcn.diycode.data.event.UserDetailUpdate;
import com.xshengcn.diycode.data.event.UserLogin;
import com.xshengcn.diycode.ui.iview.IMainView;
import com.xshengcn.diycode.util.RxBus;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainPresenter extends BasePresenter<IMainView> {

    private final PreferencesHelper mPreferencesHelper;
    private final DataManager mDataManager;
    private final RxBus mBus;

    @Inject
    public MainPresenter(PreferencesHelper preferencesHelper, DataManager dataManager,
            RxBus rxBus) {
        this.mPreferencesHelper = preferencesHelper;
        this.mDataManager = dataManager;
        this.mBus = rxBus;
    }

    @Override
    public void onAttach(IMainView view) {
        super.onAttach(view);

        Disposable userLogin = mBus.toObservable()
                .filter(o -> o instanceof UserLogin)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> loadNotificationCount());
        addDisposable(userLogin);

        Disposable userDetailUpdate = mBus.toObservable()
                .filter(o -> o instanceof UserDetailUpdate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> checkUser());
        addDisposable(userDetailUpdate);

        checkUser();
        loadNotificationCount();
        updateUserDetail();

    }

    private void updateUserDetail() {
        if (mPreferencesHelper.getToken() != null) {
            addDisposable(mDataManager.getMe().subscribe());
        }
    }

    private void loadNotificationCount() {
        if (mPreferencesHelper.getToken() != null) {
            final IMainView view = getView();
            Disposable disposable = mDataManager.getNotificationsUnreadCount()
                    .map(unread -> unread.count > 0)
                    .subscribe(view::showNotificationMenuBadge, throwable -> {
                    });
            addDisposable(disposable);
        }
    }

    private void checkUser() {
        mPreferencesHelper.getUserDetail().subscribe(getView()::setupNavigationView, e -> {});
    }

}

