package com.xshengcn.diycode.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xshengcn.diycode.R;

import java.text.MessageFormat;
import java.util.Date;

public class DateUtils {

    public static String computePastTime(@NonNull Context context, @NonNull Date date) {

        String result;

        Date now = new Date(System.currentTimeMillis());
        long diff = (now.getTime() - date.getTime()) / 1000;
        if (diff < 60) {
            result = context.getString(R.string.just_now);
        } else if ((diff /= 60) < 60) {
            result = MessageFormat.format(context.getString(R.string.minutes_ago), diff);
        } else if ((diff /= 60) < 24) {
            result = MessageFormat.format(context.getString(R.string.hours_ago), diff);
        } else if ((diff /= 24) < 30) {
            result = MessageFormat.format(context.getString(R.string.days_ago), diff);
        } else if ((diff /= 30) < 12) {
            result = MessageFormat.format(context.getString(R.string.months_ago), diff);
        } else {
            diff /= 12;
            result = MessageFormat.format(context.getString(R.string.years_ago), diff);
        }

        return result;
    }
}
