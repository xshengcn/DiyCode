package com.xshengcn.diycode.injection.module;

import android.support.v4.app.Fragment;

import dagger.Module;

@Module
public class FragmentModule {

    private final Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        this.mFragment = fragment;
    }

}
