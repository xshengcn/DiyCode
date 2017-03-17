package com.xshengcn.diycode.ui.presenter;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.model.site.SiteListItem;
import com.xshengcn.diycode.ui.iview.ISiteView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.List;
import javax.inject.Inject;

public class SitePresenter extends BasePresenter<ISiteView> {

  private final DataManager dataManager;

  @Inject
  public SitePresenter(DataManager dataManager) {
    this.dataManager = dataManager;
  }

  @Override public void onAttach(ISiteView view) {
    super.onAttach(view);
    dataManager.getSites()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::handleNext, this::handleError);
  }

  private void handleError(Throwable throwable) {

  }

  private void handleNext(List<SiteListItem> siteListItems) {
    getView().showSites(siteListItems);
  }
}
