package com.xshengcn.diycode.util.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.lang.ref.WeakReference;

public class ImageSpanTarget extends SimpleTarget<Bitmap> {

    private final WeakReference<URLDrawable> mDrawableReference;
    private final WeakReference<TextView> mTextViewReference;

    public ImageSpanTarget(URLDrawable drawable, TextView textView) {
        this.mDrawableReference = new WeakReference<>(drawable);
        this.mTextViewReference = new WeakReference<>(textView);
    }

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        TextView textView = mTextViewReference.get();
        URLDrawable drawable = mDrawableReference.get();

        if (textView != null && drawable != null) {
            BitmapDrawable bd = new BitmapDrawable(textView.getResources(), resource);
            final double aspectRatio = (1.0 * bd.getIntrinsicWidth()) / bd.getIntrinsicHeight();

            final int width = Math.min(textView.getWidth(), bd.getIntrinsicWidth());
            final int height = (int) (width / aspectRatio);
            bd.setBounds(0, 0, width, height);
            drawable.setDrawable(bd);
            drawable.setBounds(0, 0, width, height);

            textView.setText(textView.getText());
        }
    }
}
