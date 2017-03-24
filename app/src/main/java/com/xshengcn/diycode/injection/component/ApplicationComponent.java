package com.xshengcn.diycode.injection.component;

import com.xshengcn.diycode.DiyCodeApplication;
import com.xshengcn.diycode.injection.module.ActivityModule;
import com.xshengcn.diycode.injection.module.ApplicationModule;
import com.xshengcn.diycode.injection.module.HttpClientModule;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ApplicationModule.class, HttpClientModule.class})
public interface ApplicationComponent {

    void inject(DiyCodeApplication application);

    ActivityComponent plus(ActivityModule module);
}
