package com.xshengcn.diycode.ui;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.xshengcn.diycode.DiyCodeApplication;
import com.xshengcn.diycode.injection.component.ActivityComponent;
import com.xshengcn.diycode.injection.module.ActivityModule;

@SuppressLint("Registered") public class BaseActivity extends AppCompatActivity {

  private ActivityComponent activityComponent;

  @NonNull public ActivityComponent getComponent() {
    if (activityComponent == null) {
      DiyCodeApplication mainApplication = (DiyCodeApplication) getApplication();
      activityComponent = mainApplication.getComponent().plus(new ActivityModule(this));
    }
    return activityComponent;
  }
}
