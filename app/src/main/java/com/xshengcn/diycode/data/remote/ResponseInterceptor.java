package com.xshengcn.diycode.data.remote;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

public class ResponseInterceptor implements Interceptor {
  @Override public Response intercept(Chain chain) throws IOException {
    Response originalResponse = chain.proceed(chain.request());
    String cacheControl = originalResponse.header("Cache-Control");

    if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains(
        "no-cache") ||
        cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
      return originalResponse.newBuilder()
          .header("Cache-Control", "public, max-age=" + 0)
          .build();
    } else {
      return originalResponse;
    }
  }
}
