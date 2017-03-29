package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.model.site.SiteListItem;
import com.xshengcn.diycode.ui.iview.ISiteView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SitePresenter extends BasePresenter<ISiteView> {

    private final DataManager mDataManager;

    @Inject
    public SitePresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    public void loadSite() {
        addDisposable(mDataManager.getSites().doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                getView().showLoading();
            }
        }).subscribe(this::handleNext, this::handleError));
    }


    private void handleError(Throwable throwable) {
        getView().showLoadSiteError();
    }

    private void handleNext(List<SiteListItem> siteListItems) {
        getView().showSites(siteListItems);
    }
}
