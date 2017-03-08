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
  private Context context;
  private WeakReference<TextView> tvContent;
  private int imgMaxWidth;

  public GlideImageGetter(Context context, TextView tvContent, int imgMaxWidth) {
    this.context = context;
    this.tvContent = new WeakReference<>(tvContent);
    this.imgMaxWidth = imgMaxWidth;
  }

  @Override public Drawable getDrawable(String source) {
    final URLDrawable urlDrawable = new URLDrawable();
    TextView textView = tvContent.get();
    if (textView != null) {
      Glide.with(context)
          .load(source)
          .asBitmap()
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .placeholder(R.mipmap.ic_launcher)
          .error(R.mipmap.ic_launcher)
          .into(new ImageSpanTarget(urlDrawable, textView, imgMaxWidth));
    }
    return urlDrawable;
  }
}
