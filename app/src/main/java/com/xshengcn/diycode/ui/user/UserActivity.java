package com.xshengcn.diycode.ui.user;

import android.app.Activity;
import android.content.Intent;
import com.xshengcn.diycode.ui.BaseActivity;

public class UserActivity extends BaseActivity {

  public static void start(Activity activity) {
    activity.startActivity(new Intent(activity, UserActivity.class));
  }
}
