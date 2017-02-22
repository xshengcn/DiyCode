package com.xshengcn.diycode.util.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.TextView;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.lang.ref.WeakReference;

public class ImageSpanTarget extends SimpleTarget<Bitmap> {

  private final WeakReference<URLDrawable> drawableReference;
  private final WeakReference<TextView> textViewReference;
  private final int imgMaxWidth;

  public ImageSpanTarget(URLDrawable drawable, TextView textView, int imgMaxWidth) {
    this.drawableReference = new WeakReference<>(drawable);
    this.textViewReference = new WeakReference<>(textView);
    this.imgMaxWidth = imgMaxWidth;
  }

  @Override
  public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
    TextView textView = textViewReference.get();
    if (textView != null) {
      URLDrawable drawable = drawableReference.get();
      BitmapDrawable bd = new BitmapDrawable(textView.getResources(), resource);
      final double aspectRatio = 1.0 * bd.getIntrinsicWidth() / bd.getIntrinsicHeight();

      final int width = Math.min(imgMaxWidth, bd.getIntrinsicWidth());
      final int height = (int) (width / aspectRatio);
      bd.setBounds(0, 0, width, height);
      drawable.setDrawable(bd);
      drawable.setBounds(0, 0, width, height);

      textView.setText(textView.getText());
    }
  }
}
