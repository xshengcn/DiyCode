package com.xshengcn.diycode.util.glide;

import android.app.ActivityManager;
import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

public class GlideConfiguration implements GlideModule {

  @Override public void applyOptions(Context context, GlideBuilder builder) {
    ActivityManager activityManager =
        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    builder.setDecodeFormat(activityManager.isLowRamDevice() ? DecodeFormat.PREFER_RGB_565
        : DecodeFormat.PREFER_ARGB_8888);
  }

  @Override public void registerComponents(Context context, Glide glide) {
  }
}