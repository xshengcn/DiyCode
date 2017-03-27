package com.xshengcn.diycode.data.remote;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xshengcn.diycode.data.PreferencesHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OfflineRequestInterceptor implements Interceptor {

    final PreferencesHelper mPreferencesHelper;
    final ConnectivityManager manager;

    public OfflineRequestInterceptor(PreferencesHelper mPreferencesHelper,
            ConnectivityManager manager) {
        this.mPreferencesHelper = mPreferencesHelper;
        this.manager = manager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
//        if (mPreferencesHelper.getToken() != null) {
//            Token token = mPreferencesHelper.getToken();
//            builder.addHeader("Authorization", token.tokenType + " " + token.accessToken);
//        }
        if (!isConnected()) {
            int maxStale = 60 * 60 * 24 * 7; // tolerate 4-weeks stale
            builder.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
        }

        return chain.proceed(builder.build());
    }

    private boolean isConnected() {
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
