package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.xshengcn.diycode.R;
import com.xshengcn.diycode.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LicensesActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.web_view)
    WebView mWebView;

    public static void start(@NonNull Activity activity) {
        activity.startActivity(new Intent(activity, LicensesActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenses);
        ButterKnife.bind(this);

        mToolbar.setTitle(R.string.open_source_licenses);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWebView.loadUrl("file:///android_asset/licenses.html");
    }

}
