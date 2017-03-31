package com.xshengcn.diycode.util.glide;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.xshengcn.diycode.R;

import java.util.HashSet;
import java.util.Set;

public class GlideImageGetter implements Html.ImageGetter, Drawable.Callback {


    private final TextView mTextView;

    private final Set<ImageGetterViewTarget> mTargets;

    public static GlideImageGetter get(View view) {
        return (GlideImageGetter) view.getTag(R.id.drawable_callback_tag);
    }

    public void clear() {
        GlideImageGetter prev = get(mTextView);
        if (prev == null) {
            return;
        }

        for (ImageGetterViewTarget target : prev.mTargets) {
            Glide.clear(target);
        }
    }

    public GlideImageGetter(TextView textView) {
        this.mTextView = textView;

        clear();
        mTargets = new HashSet<>();
        mTextView.setTag(R.id.drawable_callback_tag, this);
    }

    @Override
    public Drawable getDrawable(String url) {
        final URLDrawable urlDrawable = new URLDrawable(url);
        Glide.with(mTextView.getContext())
                .load(url)
                .placeholder(R.drawable.ic_picture_holder)
                .error(R.drawable.ic_picture_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new ImageGetterViewTarget(mTextView, urlDrawable));

        return urlDrawable;

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

    private class ImageGetterViewTarget extends ViewTarget<TextView, GlideDrawable> {

        private final URLDrawable mDrawable;

        private ImageGetterViewTarget(TextView view, URLDrawable drawable) {
            super(view);
            mTargets.add(this);
            this.mDrawable = drawable;
        }

        @Override
        public void onResourceReady(GlideDrawable resource,
                GlideAnimation<? super GlideDrawable> glideAnimation) {
            final double aspectRatio =
                    (1.0 * resource.getIntrinsicWidth()) / resource.getIntrinsicHeight();
            boolean isEmoji = mDrawable.getSource()
                    .startsWith("https://diycode.b0.upaiyun.com/assets/emojis/");
            final int width = isEmoji ? resource.getIntrinsicWidth() : mTextView.getWidth();
            final int height = (int) (width / aspectRatio);
            resource.setBounds(0, 0, width, height);

            mDrawable.setDrawable(resource);
            mDrawable.setBounds(0, 0, width, height);

            if (resource.isAnimated()) {
                mDrawable.setCallback(get(getView()));
                resource.setLoopCount(GlideDrawable.LOOP_FOREVER);
                resource.start();
            }

            getView().setText(getView().getText());
            getView().invalidate();
        }
    }
}
