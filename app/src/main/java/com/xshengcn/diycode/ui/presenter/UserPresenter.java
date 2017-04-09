package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.PreferencesHelper;
import com.xshengcn.diycode.ui.iview.IUserView;

import javax.inject.Inject;

import io.reactivex.Single;

public class UserPresenter extends BasePresenter<IUserView> {

    private final DataManager mDataManager;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public UserPresenter(DataManager dataManager, PreferencesHelper preferencesHelper) {
        mDataManager = dataManager;
        mPreferencesHelper = preferencesHelper;
    }


    @Override
    public void onAttach(IUserView view) {
        super.onAttach(view);
    }

    public void loadUserDetail() {
        final IUserView view = getView();
        if (view.isMe()) {
            addDisposable(Single.concat(mPreferencesHelper.getUserDetail(), mDataManager.getMe())
                    .firstElement()
                    .subscribe(view::updateUserDetail, Throwable::printStackTrace));
        } else {
            addDisposable(mDataManager.getUserDetail(view.getUserLogin())
                    .subscribe(view::updateUserDetail, Throwable::printStackTrace));
        }

    }
}
