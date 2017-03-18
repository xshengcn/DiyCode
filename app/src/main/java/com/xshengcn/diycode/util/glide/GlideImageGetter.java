package com.xshengcn.diycode.util.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xshengcn.diycode.R;

import java.lang.ref.WeakReference;

public class GlideImageGetter implements Html.ImageGetter {
    private Context mContext;
    private WeakReference<TextView> mReference;
    private int mImgMaxWidth;

    public GlideImageGetter(Context context, TextView textView, int imgMaxWidth) {
        this.mContext = context;
        this.mReference = new WeakReference<>(textView);
        this.mImgMaxWidth = imgMaxWidth;
    }

    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable urlDrawable = new URLDrawable();
        TextView textView = mReference.get();
        if (textView != null) {
            Glide.with(mContext)
                    .load(source)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(new ImageSpanTarget(urlDrawable, textView, mImgMaxWidth));
        }
        return urlDrawable;
    }
}
