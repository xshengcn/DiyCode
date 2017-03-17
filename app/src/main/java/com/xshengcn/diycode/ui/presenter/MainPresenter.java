package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.event.UserDetailUpdate;
import com.xshengcn.diycode.data.event.UserLogin;
import com.xshengcn.diycode.ui.ActivityNavigator;
import com.xshengcn.diycode.ui.iview.IMainView;
import com.xshengcn.diycode.util.RxBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;

public class MainPresenter extends BasePresenter<IMainView> {

    private final DiyCodePrefs prefs;
    private final DataManager dataManager;
    private final RxBus rxBus;
    private final ActivityNavigator navigator;

    @Inject
    public MainPresenter(DiyCodePrefs prefs, DataManager dataManager, RxBus rxBus,
                         ActivityNavigator navigator) {
        this.prefs = prefs;
        this.dataManager = dataManager;
        this.rxBus = rxBus;
        this.navigator = navigator;
    }

    @Override
    public void onAttach(IMainView view) {
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
            Disposable disposable = dataManager.getNotificationsUnreadCount()
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


}

