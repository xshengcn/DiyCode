package com.xshengcn.diycode.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;
import java.lang.ref.WeakReference;

public class DynamicImageSpan extends DynamicDrawableSpan {

  private final Context context;
  private final WeakReference<Bitmap> bitmapWeakReference;
  public DynamicImageSpan(Context context, Bitmap bitmap) {
    this.bitmapWeakReference = new WeakReference<>(bitmap);
    this.context = context;
  }

  @Override public Drawable getDrawable() {
    if (bitmapWeakReference.get() != null) {
      return new BitmapDrawable(context.getResources(), bitmapWeakReference.get());
    } else {
      return null;
    }
  }
}
