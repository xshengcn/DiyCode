package com.xshengcn.diycode.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.QuoteSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.xshengcn.diycode.R;
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
        Spanned spanned = Html.fromHtml(
                source, new GlideImageGetter(context, textView, imgMaxWidth), new CodeTagHandler());

        int color = context.getResources().getColor(R.color.colorTextTertiary);
        int background = context.getResources().getColor(R.color.content_background);
        int width = context.getResources().getDimensionPixelOffset(R.dimen.spacing_xsmall);
        replaceQuoteSpans((Spannable) spanned, background, color, width);
        URLSpan[] uslSpans = spanned.getSpans(0, spanned.length(), URLSpan.class);
        ImageSpan[] imageSpans = spanned.getSpans(0, spanned.length(), ImageSpan.class);
        SpannableStringBuilder style = new SpannableStringBuilder(spanned);

        for (URLSpan urlSpan : uslSpans) {
            style.setSpan(new ClickableURLSpan(urlSpan.getURL(), callback),
                    spanned.getSpanStart(urlSpan),
                    spanned.getSpanEnd(urlSpan),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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

    private static void replaceQuoteSpans(Spannable spannable, int background, @ColorInt int color,
                                          int width) {
        QuoteSpan[] quoteSpans = spannable.getSpans(0, spannable.length(), QuoteSpan.class);
        for (QuoteSpan quoteSpan : quoteSpans) {
            int start = spannable.getSpanStart(quoteSpan);
            int end = spannable.getSpanEnd(quoteSpan);
            int flags = spannable.getSpanFlags(quoteSpan);
            spannable.removeSpan(quoteSpan);
            spannable.setSpan(
                    new CustomQuoteSpan(background, color, width, width), start, end, flags);
        }
    }

    public interface ClickCallback {

        void clickUrl(String url);

        void clickImage(String source);
    }

    public static class CustomQuoteSpan implements LeadingMarginSpan, LineBackgroundSpan {
        private final int mBackgroundColor;
        private final int mStripeColor;
        private final float mStripeWidth;
        private final float mGap;

        public CustomQuoteSpan(int backgroundColor, int stripeColor, float stripeWidth, float gap) {
            this.mBackgroundColor = backgroundColor;
            this.mStripeColor = stripeColor;
            this.mStripeWidth = stripeWidth;
            this.mGap = gap;
        }

        @Override
        public int getLeadingMargin(boolean first) {
            return (int) (mStripeWidth + mGap);
        }

        @Override
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline,
                                      int bottom, CharSequence text,
                                      int start, int end, boolean first, Layout layout) {
            Paint.Style style = p.getStyle();
            int paintColor = p.getColor();
            p.setStyle(Paint.Style.FILL);
            p.setColor(mStripeColor);
            c.drawRect(x, top, x + dir * mStripeWidth, bottom, p);
            p.setStyle(style);
            p.setColor(paintColor);
        }

        @Override
        public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline,
                                   int bottom, CharSequence text, int start, int end, int lnum) {
            int paintColor = p.getColor();
            p.setColor(mBackgroundColor);
            c.drawRect(left, top, right, bottom, p);
            p.setColor(paintColor);
        }
    }

    private static class ClickableURLSpan extends URLSpan {

        private ClickCallback mCallback;

        ClickableURLSpan(String url, ClickCallback callback) {
            super(url);
            this.mCallback = callback;
        }

        @Override
        public void onClick(View widget) {
            if (mCallback != null) mCallback.clickUrl(getURL());
        }
    }

    private static class ClickableImageSpan extends ClickableSpan {

        private String mSource;
        private ClickCallback mCallback;

        public ClickableImageSpan(String source, ClickCallback callback) {
            this.mSource = source;
            this.mCallback = callback;
        }

        @Override
        public void onClick(View widget) {
            if (mCallback != null) mCallback.clickImage(mSource);
        }
    }
}
