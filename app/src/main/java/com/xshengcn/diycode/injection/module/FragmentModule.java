package com.xshengcn.diycode.injection.module;

import android.support.v4.app.Fragment;
import dagger.Module;

@Module public class FragmentModule {

  private final Fragment fragment;

  public FragmentModule(Fragment fragment) {
    this.fragment = fragment;
  }

}
