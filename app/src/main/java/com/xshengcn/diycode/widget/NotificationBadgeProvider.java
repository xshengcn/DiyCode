package com.xshengcn.diycode.widget;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.View;

public class NotificationBadgeProvider extends ActionProvider {
  /**
   * Creates a new instance.
   *
   * @param context Context for accessing resources.
   */
  public NotificationBadgeProvider(Context context) {
    super(context);
  }

  @Override public View onCreateActionView() {
    return null;
  }

  interface OnMenuClickListener {}


}
