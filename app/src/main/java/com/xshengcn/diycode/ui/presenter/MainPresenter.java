package com.xshengcn.diycode.ui.presenter;

import android.app.Activity;

import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.api.event.UserDetailUpdate;
import com.xshengcn.diycode.api.event.UserLogin;
import com.xshengcn.diycode.ui.ActivityNavigator;
import com.xshengcn.diycode.ui.iview.IMainView;
import com.xshengcn.diycode.util.RxBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;

public class MainPresenter extends BasePresenter<IMainView> {

    private final DiyCodePrefs prefs;
    private final DiyCodeClient client;
    private final RxBus rxBus;
    private final ActivityNavigator navigator;

    @Inject
    public MainPresenter(DiyCodePrefs prefs, DiyCodeClient client, RxBus rxBus,
                         ActivityNavigator navigator) {
        this.prefs = prefs;
        this.client = client;
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
            Disposable disposable = client.getNotificationsUnreadCount()
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

    public void clickHeader(Activity activity) {
        navigator.showUser(activity);
    }

    public void clickSearch(Activity activity) {
        navigator.showSearch(activity);
    }

    public void clickNotification(Activity activity) {
        navigator.showNotification(activity);
    }

    public void clickUserTopic(Activity activity) {
        navigator.showUserTopics(activity);
    }


    public void clickUserFavorite(Activity activity) {
        navigator.showUserFavorites(activity);
    }

    public void clickUserReply(Activity activity) {
        navigator.showUserReplies(activity);
    }

    public void clickCreateTopic(Activity activity) {
        navigator.showCreateTopic(activity);
    }
}

