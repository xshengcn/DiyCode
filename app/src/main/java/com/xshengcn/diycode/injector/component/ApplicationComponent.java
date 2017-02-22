package com.xshengcn.diycode.injector.component;

import android.content.Context;
import com.xshengcn.diycode.DiyCodeApplication;
import com.xshengcn.diycode.injector.module.ActivityModule;
import com.xshengcn.diycode.injector.module.ApplicationModule;
import dagger.Component;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;

@Singleton @Component(modules = ApplicationModule.class) public interface ApplicationComponent {

  void inject(DiyCodeApplication application);

  Context context();

  OkHttpClient okHttpClient();

  ActivityComponent plus(ActivityModule module);
}
