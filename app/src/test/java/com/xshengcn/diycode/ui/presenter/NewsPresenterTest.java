package com.xshengcn.diycode.ui.presenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.model.news.News;
import com.xshengcn.diycode.ui.iview.INewsView;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

@RunWith(MockitoJUnitRunner.class)
public class NewsPresenterTest {

    @Mock
    DataManager mDataManager;
    @Mock
    INewsView mNewsView;
    private NewsPresenter mPresenter;

    @BeforeClass
    public static void onlyOnce() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                schedulerCallable -> Schedulers.trampoline());
    }

    @Before
    public void setUp() throws Exception {
        mPresenter = new NewsPresenter(mDataManager);
        mPresenter.onAttach(mNewsView);

    }

    @Test
    public void testInit() throws Exception {
        List<News> newses = mock(ArrayList.class);
        when(newses.size()).thenReturn(DataManager.PAGE_LIMIT);
        when(mNewsView.isRefreshing()).thenReturn(false);
        when(mDataManager.getAllNewses(mNewsView.getItemOffset()))
                .thenReturn(Single.just(newses));
        mPresenter.onRefresh();

        verify(mNewsView).changeStateView(MultiStateView.VIEW_STATE_LOADING);
        verify(mNewsView).showNewes(newses, true);
    }

    @Test
    public void testInitError() throws Exception {

        when(mNewsView.isRefreshing()).thenReturn(false);
        when(mDataManager.getAllNewses(mNewsView.getItemOffset()))
                .thenReturn(Single.error(mock(HttpException.class)));
        mPresenter.onRefresh();

        verify(mNewsView).changeStateView(MultiStateView.VIEW_STATE_LOADING);
        verify(mNewsView).changeStateView(MultiStateView.VIEW_STATE_ERROR);
    }

    @Test
    public void testRefresh() throws Exception {
        List<News> newses = mock(ArrayList.class);
        when(newses.size()).thenReturn(DataManager.PAGE_LIMIT);
        when(mNewsView.isRefreshing()).thenReturn(true);
        when(mDataManager.getAllNewses(mNewsView.getItemOffset()))
                .thenReturn(Single.just(newses));
        mPresenter.onRefresh();

        verify(mNewsView).showNewes(newses, true);
    }

    @Test
    public void testRefreshError() throws Exception {

        when(mNewsView.isRefreshing()).thenReturn(true);
        when(mDataManager.getAllNewses(mNewsView.getItemOffset()))
                .thenReturn(Single.error(mock(HttpException.class)));
        mPresenter.onRefresh();

        verify(mNewsView).showRefreshErrorAndComplete();
    }

    @Test
    public void testLoadMore() throws Exception {
        List<News> newses = mock(ArrayList.class);
        when(newses.size()).thenReturn(DataManager.PAGE_LIMIT);

        Integer offset = DataManager.PAGE_LIMIT;
        when(mNewsView.getItemOffset()).thenReturn(offset);

        when(mDataManager.getAllNewses(offset)).thenReturn(Single.just(newses));
        mPresenter.loadMore();
        verify(mNewsView).showNewes(newses, false);

    }

    @Test
    public void testLoadMoreError() throws Exception {
        Integer offset = DataManager.PAGE_LIMIT;
        when(mNewsView.getItemOffset()).thenReturn(offset);
        when(mDataManager.getAllNewses(offset))
                .thenReturn(Single.error(mock(HttpException.class)));
        mPresenter.loadMore();
        verify(mNewsView).showLoadMoreFailed();
    }

    @Test
    public void testLoadNoMore() throws Exception {
        List<News> newses = mock(ArrayList.class);
        when(newses.size()).thenReturn(10);
        Integer offset = DataManager.PAGE_LIMIT;
        when(mNewsView.getItemOffset()).thenReturn(offset);
        when(mDataManager.getAllNewses(offset)).thenReturn(Single.just(newses));
        mPresenter.loadMore();
        verify(mNewsView).showNewes(newses, false);
        verify(mNewsView).showLoadNoMore();
    }

    @After
    public void tearDown() throws Exception {
        mPresenter.onDetach();
        RxAndroidPlugins.reset();
    }

}