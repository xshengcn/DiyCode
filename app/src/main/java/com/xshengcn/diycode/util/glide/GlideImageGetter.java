package com.xshengcn.diycode.util.glide;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.xshengcn.diycode.R;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public final class GlideImageGetter
        implements Html.ImageGetter, View.OnAttachStateChangeListener, Drawable.Callback {

    private final TextView mTextView;
    private final int mMaxWidth;

    private final Set<ViewTarget<TextView, GlideDrawable>> mViewTargetSet = Collections
            .newSetFromMap(new WeakHashMap<>());

    public GlideImageGetter(TextView textView, int maxWidth) {
        this.mTextView = textView;
        mMaxWidth = maxWidth;
        mTextView.setTag(R.id.drawable_callback_tag, this);
        mTextView.addOnAttachStateChangeListener(this);
    }

    @Override
    public Drawable getDrawable(String url) {
        URLDrawable urlDrawable = new URLDrawable(url);
        ImageGetterViewTarget imageGetterViewTarget = new ImageGetterViewTarget(mTextView,
                urlDrawable, mMaxWidth);
        Glide.with(mTextView.getContext().getApplicationContext())
                .load(url)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageGetterViewTarget);

        mViewTargetSet.add(imageGetterViewTarget);
        return urlDrawable;

    }

    @Override
    public void onViewAttachedToWindow(View v) {
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        for (ViewTarget<TextView, GlideDrawable> viewTarget : mViewTargetSet) {
            Glide.clear(viewTarget);
        }
        mViewTargetSet.clear();
        v.removeOnAttachStateChangeListener(this);

//        v.setTag(R.id.drawable_callback_tag, null);
    }


    @Override
    public void invalidateDrawable(Drawable who) {
        mTextView.invalidate();
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
    }

    private static final class ImageGetterViewTarget extends ViewTarget<TextView, GlideDrawable> {

        private final URLDrawable mDrawable;
        private final int mMaxWidth;

        private Request mRequest;

        private ImageGetterViewTarget(TextView view, URLDrawable drawable, int maxWdith) {
            super(view);

            mDrawable = drawable;
            mMaxWidth = maxWdith;
        }


        @Override
        public void onLoadStarted(Drawable placeholder) {
            super.onLoadStarted(placeholder);
            final TextView textView = getView();
            final double aspectRatio =
                    (1.0 * placeholder.getIntrinsicWidth()) / placeholder.getIntrinsicHeight();
            if (mDrawable.getSource()
                    .startsWith("https://diycode.b0.upaiyun.com/assets/emojis/")) {
                return;
            }
            final int width = Math.min(placeholder.getIntrinsicWidth(), mMaxWidth);
            final int height = (int) (width / aspectRatio);
            Rect rect = new Rect(0, 0, width, height);
            placeholder.setBounds(rect);
            mDrawable.setBounds(rect);
            mDrawable.setDrawable(placeholder);
//            textView.setTag(R.id.drawable_callback_tag, null);
            textView.setText(textView.getText());
        }

        @Override
        public void onResourceReady(GlideDrawable resource,
                GlideAnimation<? super GlideDrawable> glideAnimation) {
            final TextView textView = getView();
            final double aspectRatio =
                    (1.0 * resource.getIntrinsicWidth()) / resource.getIntrinsicHeight();
            boolean isEmoji = mDrawable.getSource()
                    .startsWith("https://diycode.b0.upaiyun.com/assets/emojis/");
            final int width = isEmoji ? resource.getIntrinsicWidth() : mMaxWidth;
            final int height = (int) (width / aspectRatio);

            Rect rect = new Rect(0, 0, width, height);
            resource.setBounds(rect);
            mDrawable.setBounds(rect);
            mDrawable.setDrawable(resource);

            if (resource.isAnimated()) {
                Drawable.Callback callback = (Drawable.Callback) textView.getTag(
                        R.id.drawable_callback_tag);
                mDrawable.setCallback(callback);
                resource.setLoopCount(GlideDrawable.LOOP_FOREVER);
                resource.start();
            } else {
                textView.setTag(R.id.drawable_callback_tag, null);
            }

            textView.setText(textView.getText());
        }


        @Override
        public Request getRequest() {
            return mRequest;
        }

        @Override
        public void setRequest(Request request) {
            this.mRequest = request;
        }
    }
}