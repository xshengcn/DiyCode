package com.xshengcn.diycode.injection.module;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    public AppCompatActivity activity() {
        return mActivity;
    }

    @Provides
    public FragmentManager provideFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }
}
