package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.DiyCodePrefs;
import com.xshengcn.diycode.data.event.UserDetailUpdate;
import com.xshengcn.diycode.data.event.UserLogin;
import com.xshengcn.diycode.ui.ActivityNavigator;
import com.xshengcn.diycode.ui.iview.IMainView;
import com.xshengcn.diycode.util.RxBus;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainPresenter extends BasePresenter<IMainView> {

    private final DiyCodePrefs mPrefs;
    private final DataManager mDataManager;
    private final RxBus mBus;
    private final ActivityNavigator mNavigator;
    @Inject
    public MainPresenter(DiyCodePrefs prefs, DataManager dataManager, RxBus rxBus,
                         ActivityNavigator navigator) {
        this.mPrefs = prefs;
        this.mDataManager = dataManager;
        this.mBus = rxBus;
        this.mNavigator = navigator;
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
    }

    private void loadNotificationCount() {
        if (mPrefs.getToken() != null) {
            final IMainView view = getView();
            Disposable disposable = mDataManager.getNotificationsUnreadCount()
                    .map(unread -> unread.count > 0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(b -> view.showNotificationMenuBadge(b), throwable -> {
                    });
            addDisposable(disposable);
        }
    }

    public void checkUser() {
        if (mPrefs.getUser() != null) {
            getView().setupNavigationView(mPrefs.getUser());
        }
    }


}

