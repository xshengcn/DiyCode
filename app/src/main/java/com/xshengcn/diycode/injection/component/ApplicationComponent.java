package com.xshengcn.diycode.injection.component;

import com.xshengcn.diycode.DiyCodeApplication;
import com.xshengcn.diycode.injection.module.ActivityModule;
import com.xshengcn.diycode.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(DiyCodeApplication application);

    ActivityComponent plus(ActivityModule module);
}
