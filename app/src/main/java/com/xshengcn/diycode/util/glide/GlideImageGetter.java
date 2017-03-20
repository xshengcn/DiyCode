package com.xshengcn.diycode.util.glide;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xshengcn.diycode.R;

import java.lang.ref.WeakReference;

public class GlideImageGetter implements Html.ImageGetter {

    private WeakReference<TextView> mReference;

    public GlideImageGetter(TextView textView) {
        this.mReference = new WeakReference<>(textView);
    }

    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable urlDrawable = new URLDrawable();
        TextView textView = mReference.get();
        if (textView != null) {
            Glide.with(textView.getContext())
                    .load(source)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(new ImageSpanTarget(urlDrawable, textView));
        }
        return urlDrawable;
    }
}
