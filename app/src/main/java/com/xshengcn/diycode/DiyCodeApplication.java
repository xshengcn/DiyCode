package com.xshengcn.diycode;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;
import com.xshengcn.diycode.injection.component.ApplicationComponent;
import com.xshengcn.diycode.injection.component.DaggerApplicationComponent;
import com.xshengcn.diycode.injection.module.ApplicationModule;

public class DiyCodeApplication extends Application {

  private ApplicationComponent component;

  @Override public void onCreate() {
    super.onCreate();

    component =
        DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();

    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
  }

  public ApplicationComponent getComponent() {
    return component;
  }
}
