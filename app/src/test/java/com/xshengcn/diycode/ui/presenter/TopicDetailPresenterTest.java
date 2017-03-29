package com.xshengcn.diycode.ui.presenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kennyc.view.MultiStateView;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.data.model.topic.TopicAndComments;
import com.xshengcn.diycode.data.model.topic.TopicComment;
import com.xshengcn.diycode.ui.iview.ITopicDetailView;

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
    ITopicDetailView mTopicDetailView;
    private TopicDetailPresenter mPresenter;

    @BeforeClass
    public static void onlyOnce() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                schedulerCallable -> Schedulers.trampoline());
    }

    @Before
    public void setUp() throws Exception {
        mPresenter = new TopicDetailPresenter(mDataManager);
        mPresenter.onAttach(mTopicDetailView);
    }


    @Test
    public void testInit() throws Exception {

        Topic topic = mock(Topic.class);
        TopicAndComments topicAndComments = mock(TopicAndComments.class);

        when(mTopicDetailView.isRefreshing()).thenReturn(false);
        when(mTopicDetailView.getTopic()).thenReturn(topic);
        when(mDataManager.getTopicAndComments(topic.id))
                .thenReturn(Observable.just(topicAndComments));

        mPresenter.onRefresh();
        verify(mTopicDetailView).showTopicAndReplies(any(TopicAndComments.class));
    }

    @Test
    public void testInitError() throws Exception {

        Topic topic = mock(Topic.class);
        when(mTopicDetailView.isRefreshing()).thenReturn(false);
        when(mTopicDetailView.getTopic()).thenReturn(topic);
        when(mDataManager.getTopicAndComments(topic.id))
                .thenReturn(Observable.error(mock(HttpException.class)));

        mPresenter.onRefresh();
        verify(mTopicDetailView).changeStateView(MultiStateView.VIEW_STATE_ERROR);
    }

    @Test
    public void testInitNoMore() throws Exception {

        Topic topic = mock(Topic.class);
        TopicAndComments topicAndComments = mock(TopicAndComments.class);
        topicAndComments.comments = mock(ArrayList.class);
        when(topicAndComments.comments.size()).thenReturn(10);
        when(mTopicDetailView.isRefreshing()).thenReturn(false);
        when(mTopicDetailView.getTopic()).thenReturn(topic);
        when(mDataManager.getTopicAndComments(topic.id))
                .thenReturn(Observable.just(topicAndComments));

        mPresenter.onRefresh();
        verify(mTopicDetailView).showTopicAndReplies(any(TopicAndComments.class));
        verify(mTopicDetailView).showLoadNoMore();
    }

    @Test
    public void testRefresh() throws Exception {

        Topic topic = mock(Topic.class);
        TopicAndComments topicAndComments = mock(TopicAndComments.class);
        when(mTopicDetailView.isRefreshing()).thenReturn(true);
        when(mTopicDetailView.getTopic()).thenReturn(topic);
        when(mDataManager.getTopicAndComments(topic.id))
                .thenReturn(Observable.just(topicAndComments));

        mPresenter.onRefresh();
        verify(mTopicDetailView).showTopicAndReplies(topicAndComments);
    }

    @Test
    public void testRefreshError() throws Exception {

        Topic topic = mock(Topic.class);
        when(mTopicDetailView.isRefreshing()).thenReturn(true);
        when(mTopicDetailView.getTopic()).thenReturn(topic);
        when(mDataManager.getTopicAndComments(topic.id))
                .thenReturn(Observable.error(mock(HttpException.class)));

        mPresenter.onRefresh();
        verify(mTopicDetailView).showRefreshErrorAndComplete();
    }

    @Test
    public void testRefreshNoMore() throws Exception {

        Topic topic = mock(Topic.class);
        TopicAndComments topicAndComments = mock(TopicAndComments.class);
        topicAndComments.comments = mock(ArrayList.class);
        when(topicAndComments.comments.size()).thenReturn(10);
        when(mTopicDetailView.isRefreshing()).thenReturn(true);
        when(mTopicDetailView.getTopic()).thenReturn(topic);
        when(mDataManager.getTopicAndComments(topic.id))
                .thenReturn(Observable.just(topicAndComments));

        mPresenter.onRefresh();
        verify(mTopicDetailView).showTopicAndReplies(topicAndComments);
        verify(mTopicDetailView).showLoadNoMore();
    }

    @Test
    public void testLoadMore() throws Exception {

        Topic topic = mock(Topic.class);
        List<TopicComment> replies = mock(ArrayList.class);
        when(replies.size()).thenReturn(DataManager.PAGE_LIMIT);
        when(mTopicDetailView.getTopic()).thenReturn(topic);
        when(mTopicDetailView.getItemOffset()).thenReturn(DataManager.PAGE_LIMIT);
        when(mDataManager.getTopicReplies(topic.id, DataManager.PAGE_LIMIT))
                .thenReturn(Observable.just(replies));

        mPresenter.loadMoreReplies();
        verify(mTopicDetailView).showMoreReplies(replies);
    }

    @Test
    public void testLoadMoreError() throws Exception {

        Topic topic = mock(Topic.class);
        when(mTopicDetailView.getTopic()).thenReturn(topic);
        when(mTopicDetailView.getItemOffset()).thenReturn(DataManager.PAGE_LIMIT);
        when(mDataManager.getTopicReplies(topic.id, DataManager.PAGE_LIMIT))
                .thenReturn(Observable.error(mock(HttpException.class)));

        mPresenter.loadMoreReplies();
        verify(mTopicDetailView).showLoadMoreFailed();
    }

    @Test
    public void testLoadNoMore() throws Exception {

        Topic topic = mock(Topic.class);
        List<TopicComment> replies = mock(ArrayList.class);
        when(replies.size()).thenReturn(10);
        when(mTopicDetailView.getTopic()).thenReturn(topic);
        when(mTopicDetailView.getItemOffset()).thenReturn(DataManager.PAGE_LIMIT);
        when(mDataManager.getTopicReplies(topic.id, DataManager.PAGE_LIMIT))
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