package com.xshengcn.diycode.ui.presenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.model.topic.TopicAndReplies;
import com.xshengcn.diycode.data.model.topic.TopicReply;
import com.xshengcn.diycode.ui.iview.ITopicDetailView;
import com.xshengcn.diycode.util.RxBus;

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
public class TopicDetailPresenterTest {

    @Mock
    DataManager mDataManager;
    @Mock
    RxBus mBus;
    @Mock
    ITopicDetailView mTopicDetailView;
    private TopicDetailPresenter mPresenter;

    @BeforeClass
    public static void onlyOnce() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                schedulerCallable -> Schedulers.trampoline());
    }

    @Before
    public void setUp() throws Exception {
        mPresenter = new TopicDetailPresenter(mDataManager, mBus);
        mPresenter.onAttach(mTopicDetailView);
    }


    @Test
    public void testInit() throws Exception {

        int topicId = 0;
        TopicAndReplies topicAndReplies = mock(TopicAndReplies.class);

        when(mTopicDetailView.isRefreshing()).thenReturn(false);
        when(mTopicDetailView.getTopicId()).thenReturn(topicId);
        when(mDataManager.getTopicAndComments(topicId))
                .thenReturn(Observable.just(topicAndReplies));

        mPresenter.onRefresh();
        verify(mTopicDetailView).showTopicAndReplies(any(TopicAndReplies.class));
    }

    @Test
    public void testInitError() throws Exception {
        int topicId = 0;
        when(mTopicDetailView.isRefreshing()).thenReturn(false);
        when(mTopicDetailView.getTopicId()).thenReturn(topicId);
        when(mDataManager.getTopicAndComments(topicId))
                .thenReturn(Observable.error(mock(HttpException.class)));

        mPresenter.onRefresh();
        verify(mTopicDetailView).changeStateView(MultiStateView.VIEW_STATE_ERROR);
    }

    @Test
    public void testInitNoMore() throws Exception {

        int topicId = 0;
        TopicAndReplies topicAndReplies = mock(TopicAndReplies.class);
        topicAndReplies.replies = mock(ArrayList.class);
        when(topicAndReplies.replies.size()).thenReturn(10);
        when(mTopicDetailView.isRefreshing()).thenReturn(false);
        when(mTopicDetailView.getTopicId()).thenReturn(topicId);
        when(mDataManager.getTopicAndComments(topicId))
                .thenReturn(Observable.just(topicAndReplies));

        mPresenter.onRefresh();
        verify(mTopicDetailView).showTopicAndReplies(any(TopicAndReplies.class));
        verify(mTopicDetailView).showLoadNoMore();
    }

    @Test
    public void testRefresh() throws Exception {

        int topicId = 0;
        TopicAndReplies topicAndReplies = mock(TopicAndReplies.class);
        when(mTopicDetailView.isRefreshing()).thenReturn(true);
        when(mTopicDetailView.getTopicId()).thenReturn(topicId);
        when(mDataManager.getTopicAndComments(topicId))
                .thenReturn(Observable.just(topicAndReplies));

        mPresenter.onRefresh();
        verify(mTopicDetailView).showTopicAndReplies(topicAndReplies);
    }

    @Test
    public void testRefreshError() throws Exception {

        int topicId = 0;
        when(mTopicDetailView.isRefreshing()).thenReturn(true);
        when(mTopicDetailView.getTopicId()).thenReturn(topicId);
        when(mDataManager.getTopicAndComments(topicId))
                .thenReturn(Observable.error(mock(HttpException.class)));

        mPresenter.onRefresh();
        verify(mTopicDetailView).showRefreshErrorAndComplete();
    }

    @Test
    public void testRefreshNoMore() throws Exception {

        int topicId = 0;
        TopicAndReplies topicAndReplies = mock(TopicAndReplies.class);
        topicAndReplies.replies = mock(ArrayList.class);
        when(topicAndReplies.replies.size()).thenReturn(10);
        when(mTopicDetailView.isRefreshing()).thenReturn(true);
        when(mTopicDetailView.getTopicId()).thenReturn(topicId);
        when(mDataManager.getTopicAndComments(topicId))
                .thenReturn(Observable.just(topicAndReplies));

        mPresenter.onRefresh();
        verify(mTopicDetailView).showTopicAndReplies(topicAndReplies);
        verify(mTopicDetailView).showLoadNoMore();
    }

    @Test
    public void testLoadMore() throws Exception {

        int topicId = 0;
        List<TopicReply> replies = mock(ArrayList.class);
        when(replies.size()).thenReturn(DataManager.PAGE_LIMIT);
        when(mTopicDetailView.getTopicId()).thenReturn(topicId);
        when(mTopicDetailView.getItemOffset()).thenReturn(DataManager.PAGE_LIMIT);
        when(mDataManager.getTopicReplies(topicId, DataManager.PAGE_LIMIT))
                .thenReturn(Observable.just(replies));

        mPresenter.loadMoreReplies();
        verify(mTopicDetailView).showMoreReplies(replies);
    }

    @Test
    public void testLoadMoreError() throws Exception {

        int topicId = 0;
        when(mTopicDetailView.getTopicId()).thenReturn(topicId);
        when(mTopicDetailView.getItemOffset()).thenReturn(DataManager.PAGE_LIMIT);
        when(mDataManager.getTopicReplies(topicId, DataManager.PAGE_LIMIT))
                .thenReturn(Observable.error(mock(HttpException.class)));

        mPresenter.loadMoreReplies();
        verify(mTopicDetailView).showLoadMoreFailed();
    }

    @Test
    public void testLoadNoMore() throws Exception {

        int topicId = 0;
        List<TopicReply> replies = mock(ArrayList.class);
        when(replies.size()).thenReturn(10);
        when(mTopicDetailView.getTopicId()).thenReturn(topicId);
        when(mTopicDetailView.getItemOffset()).thenReturn(DataManager.PAGE_LIMIT);
        when(mDataManager.getTopicReplies(topicId, DataManager.PAGE_LIMIT))
                .thenReturn(Observable.just(replies));

        mPresenter.loadMoreReplies();
        verify(mTopicDetailView).showMoreReplies(replies);
    }

    @After
    public void tearDown() throws Exception {
        mPresenter.onDetach();
        RxAndroidPlugins.reset();
    }
}