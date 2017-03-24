package com.xshengcn.diycode;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.xshengcn.diycode.injection.component.ApplicationComponent;
import com.xshengcn.diycode.injection.component.DaggerApplicationComponent;
import com.xshengcn.diycode.injection.module.ApplicationModule;

public class DiyCodeApplication extends Application {

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        Logger.init().logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
    }

    public ApplicationComponent getComponent() {
        return mComponent;
    }
}
