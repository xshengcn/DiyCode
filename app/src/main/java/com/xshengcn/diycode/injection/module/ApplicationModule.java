package com.xshengcn.diycode.injection.module;

import android.content.Context;
import android.net.ConnectivityManager;

import com.xshengcn.diycode.DiyCodeApplication;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.PreferencesHelper;
import com.xshengcn.diycode.util.RxBus;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

import okhttp3.OkHttpClient;

@Module
public class ApplicationModule {

    private final DiyCodeApplication mApplication;

    public ApplicationModule(DiyCodeApplication application) {
        this.mApplication = application;
    }

    @Singleton
    @Provides
    protected DiyCodeApplication provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    protected Context provideContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    public ConnectivityManager provideConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Singleton
    @Provides
    public DataManager provideDiyCodeClient(OkHttpClient okHttpClient) {
        return new DataManager(okHttpClient);
    }



    @Singleton
    @Provides
    public PreferencesHelper providePreferencesHelper(Context context, RxBus rxBus) {
        return new PreferencesHelper(context, rxBus);
    }

    @Singleton
    @Provides
    public RxBus provideRxBus() {
        return new RxBus();
    }


}
