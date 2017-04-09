package com.xshengcn.diycode.ui.presenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.PreferencesHelper;
import com.xshengcn.diycode.data.model.Token;
import com.xshengcn.diycode.ui.iview.ILoginView;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Mock
    DataManager mDataManager;
    @Mock
    PreferencesHelper mPreferencesHelper;
    @Mock
    ILoginView mLoginView;
    private LoginPresenter mPresenter;

    @BeforeClass
    public static void onlyOnce() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                schedulerCallable -> Schedulers.trampoline());
    }

    @Before
    public void setUp() throws Exception {
        mPresenter = new LoginPresenter(mDataManager, mPreferencesHelper);
        mPresenter.onAttach(mLoginView);
    }

    @Test
    public void testLoginSuccessful() throws Exception {
        Token token = mock(Token.class);
        String username = "xshengcn";
        String password = "password";
        when(mDataManager.login(username, password)).thenReturn(Single.just(token));

        mPresenter.login(username, password);

        verify(mLoginView).showLoginDialog();
        verify(mLoginView).hideLoginDialog();
        verify(mLoginView).loginSuccess();

    }

    @Test
    public void testLoginFailed() throws Exception {
        String username = "xshengcn";
        String password = "password";
        when(mDataManager.login(username, password))
                .thenReturn(Single.error(mock(HttpException.class)));

        mPresenter.login(username, password);

        verify(mLoginView).showLoginDialog();
        verify(mLoginView).hideLoginDialog();
        verify(mLoginView).loginError();
    }

    @After
    public void tearDown() throws Exception {
        mPresenter.onDetach();
        RxJavaPlugins.reset();
    }

}