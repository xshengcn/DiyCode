package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.xshengcn.diycode.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MarkdownPreviewActivity extends BaseActivity {

  private static final String EXTRA_CONTENT = "MarkdownPreviewActivity.Content";
  @BindView(R.id.web_view) WebView webView;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;

  private String text;

  public static void start(Activity activity, String text) {
    Intent intent = new Intent(activity, MarkdownPreviewActivity.class);
    intent.putExtra(EXTRA_CONTENT, text);
    activity.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_markdown_preview);
    ButterKnife.bind(this);

    text = getIntent().getStringExtra(EXTRA_CONTENT);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    if (Build.VERSION.SDK_INT >= 21) {
      WebView.enableSlowWholeDocumentDraw();
    }
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setVerticalScrollBarEnabled(false);
    webView.setHorizontalScrollBarEnabled(false);
    webView.addJavascriptInterface(new JavaScriptInterface(), "handler");
    webView.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return true;
      }

      @Override public void onPageFinished(WebView view, String url) {
        //super.onPageFinished(view, url);
        //parseMarkdown(text);

      }
    });
    //webView.loadUrl("file:///android_asset/markdown.html");
    webView.loadUrl("file:///android_asset/markdown-helper.html");
  }


  @Override protected void onDestroy() {
    super.onDestroy();
  }

  public final void parseMarkdown(String str) {
    webView.loadUrl("javascript:parseMarkdown(\"" + str.replace("\n", "\\n")
        .replace("\"", "\\\"")
        .replace("'", "\\'") + "\", " + true + ")");
  }

  final class JavaScriptInterface {

    @JavascriptInterface public void none() {

    }
  }
}
