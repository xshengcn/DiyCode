package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.ui.iview.IUserView;

import javax.inject.Inject;

public class UserPresenter extends BasePresenter<IUserView> {

    private final DataManager mDataManager;

    @Inject
    public UserPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void onAttach(IUserView view) {
        super.onAttach(view);
    }

    public void loadUserDetail() {
        final IUserView view = getView();
        if (view.isMe()) {
            addDisposable(mDataManager.getMe(false)
                    .subscribe(view::updateUserDetail, Throwable::printStackTrace));
        } else {
            addDisposable(mDataManager.getUserDetail(view.getUserLogin())
                    .subscribe(view::updateUserDetail, Throwable::printStackTrace));
        }

    }
}
