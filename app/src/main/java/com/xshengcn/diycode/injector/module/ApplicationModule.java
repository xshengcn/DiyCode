package com.xshengcn.diycode.injector.module;

import android.content.Context;
import android.net.ConnectivityManager;
import com.xshengcn.diycode.BuildConfig;
import com.xshengcn.diycode.DiyCodeApplication;
import com.xshengcn.diycode.DiyCodePrefs;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.api.OfflineRequestInterceptor;
import com.xshengcn.diycode.api.ResponseInterceptor;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module public class ApplicationModule {

  static final String CACHE_FILE_NAME = "okhttp.cache";
  static final long MAX_CACHE_SIZE = 100 * 1024 * 1024;

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

  @Singleton @Provides public DiyCodeClient providerDiyCodeClient(OkHttpClient okHttpClient) {
    return new DiyCodeClient(okHttpClient);
  }

  @Singleton @Provides public OkHttpClient provideHttpClient(Context context, DiyCodePrefs prefs,
      ConnectivityManager manager) {
    File cacheDir = new File(context.getCacheDir(), CACHE_FILE_NAME);
    Cache cache = new Cache(cacheDir, MAX_CACHE_SIZE);

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(
        BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

    OkHttpClient.Builder builder = new OkHttpClient.Builder().cache(cache)
        .addInterceptor(logging)
        .addNetworkInterceptor(new ResponseInterceptor())
        .addInterceptor(new OfflineRequestInterceptor(prefs, manager));

    return builder.build();
  }

  @Singleton @Provides public DiyCodePrefs provideUserPrefs(Context context) {
    return new DiyCodePrefs(context);
  }
}
