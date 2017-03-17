package com.xshengcn.diycode.injection.module;

import android.content.Context;
import android.net.ConnectivityManager;
import com.xshengcn.diycode.BuildConfig;
import com.xshengcn.diycode.DiyCodeApplication;
import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.remote.OfflineRequestInterceptor;
import com.xshengcn.diycode.data.remote.ResponseInterceptor;
import com.xshengcn.diycode.util.RxBus;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module public class ApplicationModule {

  private static final String CACHE_FILE_NAME = "okhttp.cache";
  private static final long MAX_CACHE_SIZE = 10 * 1024 * 1024;

  private final DiyCodeApplication application;

  public ApplicationModule(DiyCodeApplication application) {
    this.application = application;
  }

  @Singleton @Provides protected DiyCodeApplication providerApplication() {
    return application;
  }

  @Singleton @Provides protected Context providerContext() {
    return application.getApplicationContext();
  }

  @Provides public ConnectivityManager provideConnectivityManager(Context context) {
    return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  @Singleton @Provides public DataManager providerDiyCodeClient(OkHttpClient okHttpClient) {
    return new DataManager(okHttpClient);
  }

  @Singleton @Provides public OkHttpClient providerHttpClient(Context context, DiyCodePrefs prefs,
      ConnectivityManager manager) {
    File cacheDir = new File(context.getCacheDir(), CACHE_FILE_NAME);
    Cache cache = new Cache(cacheDir, MAX_CACHE_SIZE);

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(
        BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

    OkHttpClient.Builder builder =
        new OkHttpClient.Builder().connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .writeTimeout(60000, TimeUnit.MILLISECONDS)
            .cache(cache)
            .addInterceptor(logging)
            .addNetworkInterceptor(new ResponseInterceptor())
            .addInterceptor(new OfflineRequestInterceptor(prefs, manager));

    return builder.build();
  }

  @Singleton @Provides public DiyCodePrefs providerUserPrefs(Context context, RxBus rxBus) {
    return new DiyCodePrefs(context, rxBus);
  }

  @Singleton @Provides public RxBus providerRxBus() {
    return new RxBus();
  }

}
