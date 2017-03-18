package com.xshengcn.diycode.util;

import android.support.annotation.NonNull;

import java.util.Date;

public class DateUtils {

    public static String computePastTime(@NonNull Date date) {

        String result = "刚刚";

        Date now = new Date(System.currentTimeMillis());
        long diff = (now.getTime() - date.getTime()) / 1000;
        if (diff < 60) {
            result = "刚刚";
        } else if ((diff /= 60) < 60) {
            result = diff + "分钟前";
        } else if ((diff /= 60) < 24) {
            result = diff + "小时前";
        } else if ((diff /= 24) < 30) {
            result = diff + "天前";
        } else if ((diff /= 30) < 12) {
            result = diff + "月前";
        } else {
            diff /= 12;
            result = diff + "年前";
        }

        return result;
    }
}
