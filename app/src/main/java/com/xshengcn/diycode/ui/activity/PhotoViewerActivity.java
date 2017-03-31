package com.xshengcn.diycode.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xshengcn.diycode.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoViewerActivity extends BaseActivity {

    private static final int UI_ANIMATION_DELAY = 300;
    private static final String EXTRA_IMAGE_SOURCE = "PhotoViewerActivity.source";
    @BindView(R.id.image_view)
    PhotoView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private final Runnable mRunnable = () -> getSupportActionBar().show();

    private boolean mVisible;
    private Handler mHandler = new Handler();

    public static void start(Activity activity, String source) {
        Intent intent = new Intent(activity, PhotoViewerActivity.class);
        intent.putExtra(EXTRA_IMAGE_SOURCE, source);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String source = getIntent().getStringExtra(EXTRA_IMAGE_SOURCE);
        imageView.enable();
        Glide.with(this).load(source).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.image_view)
    void toggle(View view) {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mVisible = true;

        // Show the system bar
        imageView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mHandler.postDelayed(mRunnable, UI_ANIMATION_DELAY);
    }


    private void hide() {
        mVisible = false;

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mHandler.removeCallbacks(mRunnable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }
}
