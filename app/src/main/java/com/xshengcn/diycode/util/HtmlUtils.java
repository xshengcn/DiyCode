package com.xshengcn.diycode.util;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import com.xshengcn.diycode.util.glide.GlideImageGetter;

public class HtmlUtils {

  public static CharSequence trimTrailingWhitespace(CharSequence source) {

    if (source == null) return "";

    int i = source.length();

    while (--i >= 0 && Character.isWhitespace(source.charAt(i))) {
    }

    return source.subSequence(0, i + 1);
  }

  public static void parseHtmlAndSetText(Context context, String source, TextView textView,
      int imgMaxWidth, ClickCallback callback) {
    Spanned spanned = Html.fromHtml(source, new GlideImageGetter(context, textView, imgMaxWidth),
        new CodeTagHandler());
    URLSpan[] uslSpans = spanned.getSpans(0, spanned.length(), URLSpan.class);
    ImageSpan[] imageSpans = spanned.getSpans(0, spanned.length(), ImageSpan.class);
    SpannableStringBuilder style = new SpannableStringBuilder(spanned);

    for (URLSpan urlSpan : uslSpans) {
      style.setSpan(new ClickableURLSpan(urlSpan.getURL(), callback), spanned.getSpanStart(urlSpan),
          spanned.getSpanEnd(urlSpan), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      style.removeSpan(urlSpan);
    }

    for (ImageSpan imageSpan : imageSpans) {
      style.setSpan(new ClickableImageSpan(imageSpan.getSource(), callback),
          spanned.getSpanStart(imageSpan), spanned.getSpanEnd(imageSpan),
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    textView.setMovementMethod(LinkMovementMethod.getInstance());

    textView.setText(trimTrailingWhitespace(style));
  }

  public interface ClickCallback {

    void clickUrl(String url);

    void clickImage(String source);
  }

  private static class ClickableURLSpan extends URLSpan {

    private ClickCallback callback;

    ClickableURLSpan(String url, ClickCallback callback) {
      super(url);
      this.callback = callback;
    }

    @Override public void onClick(View widget) {
      if (callback != null) callback.clickUrl(getURL());
    }
  }

  private static class ClickableImageSpan extends ClickableSpan {

    private String source;
    private ClickCallback callback;

    public ClickableImageSpan(String source, ClickCallback callback) {
      this.source = source;
      this.callback = callback;
    }

    @Override public void onClick(View widget) {
      if (callback != null) callback.clickImage(source);
    }
  }
}
