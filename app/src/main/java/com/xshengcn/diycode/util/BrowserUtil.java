package com.xshengcn.diycode.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.xshengcn.diycode.R;
import com.xshengcn.diycode.ui.receiver.ActionBroadcastReceiver;
import com.xshengcn.diycode.util.customtabs.CustomTabActivityHelper;

public class BrowserUtil {

    public static void openUrl(@NonNull Activity activity, @NonNull String url) {
        openUrl(activity, Uri.parse(url));
    }

    public static void openUrl(@NonNull Activity activity, @NonNull Uri uri) {
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = activity.getColor(R.color.colorPrimary);
        } else {
            color = activity.getResources().getColor(R.color.colorPrimary);
        }
        openUrl(color, activity, uri);
    }

    public static void openUrl(@ColorInt int color, @NonNull Activity activity, @NonNull Uri uri) {

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(color);
        builder.setShowTitle(true);

        String shareMenuTitle = activity.getString(R.string.share);
        Bitmap shareIcon = getBitmap(activity, R.drawable.ic_menu_share);
        PendingIntent shareMenuIntent =
                createPendingIntent(activity, ActionBroadcastReceiver.ACTION_MENU_SHARE);
        builder.setActionButton(shareIcon, shareMenuTitle, shareMenuIntent);

        CustomTabActivityHelper.openCustomTab(
                activity, builder.build(), uri, BrowserUtil::onCustomTabFallback);
    }

    private static void onCustomTabFallback(Activity activity, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    private static PendingIntent createPendingIntent(Context context, int actionSourceId) {
        Intent actionIntent =
                new Intent(context.getApplicationContext(), ActionBroadcastReceiver.class);
        actionIntent.putExtra(ActionBroadcastReceiver.KEY_ACTION_SOURCE,
                ActionBroadcastReceiver.ACTION_MENU_SHARE);
        return PendingIntent.getBroadcast(context.getApplicationContext(),
                ActionBroadcastReceiver.ACTION_MENU_SHARE, actionIntent, 0);
    }

    private static Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
}
