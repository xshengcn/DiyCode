package com.xshengcn.diycode.ui.presenter;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.model.site.SiteListItem;
import com.xshengcn.diycode.ui.iview.ISiteView;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

@RunWith(MockitoJUnitRunner.class)
public class SitePresenterTest {

    @Mock
    DataManager mDataManager;
    @Mock
    ISiteView mSiteView;
    private SitePresenter mSitePresenter;

    @BeforeClass
    public static void onlyOnce() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                schedulerCallable -> Schedulers.trampoline());
    }

    @Before
    public void setUp() throws Exception {
        mSitePresenter = new SitePresenter(mDataManager);
        mSitePresenter.onAttach(mSiteView);
    }

    @Test
    public void testLoadSiteSuccessful() throws Exception {
        List<SiteListItem> sites = mock(ArrayList.class);
        when(mDataManager.getSites()).thenReturn(Observable.just(sites));
        mSitePresenter.loadSite();
        verify(mSiteView).showLoading();
        verify(mSiteView).showSites(sites);
    }

    @Test
    public void testLoadSiteFailed() throws Exception {
        when(mDataManager.getSites()).thenReturn(Observable.error(mock(HttpException.class)));
        mSitePresenter.loadSite();
        verify(mSiteView).showLoading();
        verify(mSiteView).showLoadSiteError();
    }

    @After
    public void tearDown() throws Exception {
        RxAndroidPlugins.reset();
    }


}