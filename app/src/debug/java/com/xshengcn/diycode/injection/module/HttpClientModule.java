package com.xshengcn.diycode.injection.module;

import android.content.Context;
import android.net.ConnectivityManager;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.xshengcn.diycode.data.PreferencesHelper;
import com.xshengcn.diycode.data.remote.OfflineRequestInterceptor;
import com.xshengcn.diycode.data.remote.ResponseInterceptor;

import dagger.Module;
import dagger.Provides;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

@Module
public class HttpClientModule {

    private static final String CACHE_FILE_NAME = "okhttp.cache";
    private static final long MAX_CACHE_SIZE = 10 * 1024 * 1024;

    @Singleton
    @Provides
    public OkHttpClient provideHttpClient(Context context, PreferencesHelper preferencesHelper,
            ConnectivityManager manager) {
        File cacheDir = new File(context.getCacheDir(), CACHE_FILE_NAME);
        Cache cache = new Cache(cacheDir, MAX_CACHE_SIZE);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .cache(cache)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(logging)
                .addNetworkInterceptor(new ResponseInterceptor())
                .addInterceptor(new OfflineRequestInterceptor(preferencesHelper, manager));

        return builder.build();
    }

}
