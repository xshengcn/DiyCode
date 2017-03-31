package com.xshengcn.diycode.ui.presenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.model.topic.TopicNode;
import com.xshengcn.diycode.data.model.topic.TopicNodeCategory;
import com.xshengcn.diycode.ui.iview.ITopicCreatorView;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

@RunWith(MockitoJUnitRunner.class)
public class TopicCreatorPresenterTest {

    @Mock
    DataManager mDataManager;
    @Mock
    ITopicCreatorView mView;
    private TopicCreatorPresenter mPresenter;

    @BeforeClass
    public static void onlyOnce() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                schedulerCallable -> Schedulers.trampoline());
    }

    @Before
    public void setUp() throws Exception {
        mPresenter = new TopicCreatorPresenter(mDataManager);
        mPresenter.onAttach(mView);
    }

    @Test
    public void testLoadTopicNodesSuccess() throws Exception {
        Map<TopicNodeCategory, List<TopicNode>> nodesMap = mock(Map.class);
        when(mDataManager.getTopicNodes()).thenReturn(Observable.just(nodesMap));

        mPresenter.loadTopicNodes();
        verify(mView).showNodes(nodesMap);
    }


    @After
    public void tearDown() throws Exception {
        RxAndroidPlugins.reset();
    }
}