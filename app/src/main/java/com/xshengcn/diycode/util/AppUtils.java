package com.xshengcn.diycode.util;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ColorRes;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.text.Spanned;

public class AppUtils {

    private AppUtils() {
    }

    public static Spanned fromHtml(String source, ImageGetter imageGetter, TagHandler tagHandler) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY, imageGetter, tagHandler);
        } else {
            return Html.fromHtml(source, imageGetter, tagHandler);
        }
    }

    public static int getColor(Context context, @ColorRes int res) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            return context.getColor(res);
        }else {
            return context.getResources().getColor(res);
        }
    }

}
