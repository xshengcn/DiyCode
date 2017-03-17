package com.xshengcn.diycode.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.xshengcn.diycode.R;

public class ActionBroadcastReceiver extends BroadcastReceiver {
  public static final String KEY_ACTION_SOURCE = "com.xshengcn.diycode.ACTION_SOURCE";
  public static final int ACTION_MENU_SHARE = 1;

  @Override
  public void onReceive(Context context, Intent intent) {
    String url = intent.getDataString();
    if (!TextUtils.isEmpty(url)) {
      int actionId = intent.getIntExtra(KEY_ACTION_SOURCE, -1);
      doAction(context, actionId, url);
    }
  }

  private void doAction(Context context, int actionId, String url) {
    switch (actionId) {
      case ACTION_MENU_SHARE:
        shareUrl(context, url);
        break;
    }
  }

  private void shareUrl(Context context, String url) {
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_TEXT, url);
    Intent chooserIntent = Intent.createChooser(shareIntent, context.getString(R.string.share));
    chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(chooserIntent);
  }
}