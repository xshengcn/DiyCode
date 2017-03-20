package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.model.site.SiteListItem;
import com.xshengcn.diycode.ui.iview.ISiteView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SitePresenter extends BasePresenter<ISiteView> {

    private final DataManager mDataManager;

    @Inject
    public SitePresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void onAttach(ISiteView view) {
        super.onAttach(view);
        mDataManager.getSites()
                .subscribe(this::handleNext, this::handleError);
    }

    private void handleError(Throwable throwable) {

    }

    private void handleNext(List<SiteListItem> siteListItems) {
        getView().showSites(siteListItems);
    }
}
