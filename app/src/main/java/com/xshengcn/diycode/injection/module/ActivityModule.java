package com.xshengcn.diycode.injection.module;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import dagger.Module;
import dagger.Provides;

@Module public class ActivityModule {

  private final AppCompatActivity activity;

  public ActivityModule(AppCompatActivity activity) {
    this.activity = activity;
  }

  @Provides public AppCompatActivity activity() {
    return activity;
  }

  @Provides public FragmentManager provideFragmentManager() {
    return activity.getSupportFragmentManager();
  }
}
