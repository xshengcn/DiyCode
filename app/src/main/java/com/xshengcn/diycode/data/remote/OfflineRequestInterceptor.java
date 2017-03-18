package com.xshengcn.diycode.data.remote;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xshengcn.diycode.data.DiyCodePrefs;
import com.xshengcn.diycode.data.model.Token;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OfflineRequestInterceptor implements Interceptor {

    final DiyCodePrefs prefs;
    final ConnectivityManager manager;

    public OfflineRequestInterceptor(DiyCodePrefs prefs, ConnectivityManager manager) {
        this.prefs = prefs;
        this.manager = manager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if (prefs.getToken() != null) {
            Token token = prefs.getToken();
            builder.addHeader("Authorization", token.tokenType + " " + token.accessToken);
        }
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
