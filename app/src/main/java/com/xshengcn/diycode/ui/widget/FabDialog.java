package com.xshengcn.diycode.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.xshengcn.diycode.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FabDialog extends Dialog {

    @BindView(R.id.fab_menu)
    FloatingActionButton fabMenu;
    @BindView(R.id.fab_share_news)
    FloatingActionButton fabShareNews;
    @BindView(R.id.fab_create_topic)
    FloatingActionButton fabCreateTopic;
    @BindView(R.id.text_share_news)
    TextView textShareNews;
    @BindView(R.id.text_create_topic)
    TextView textCreateTopic;
    @BindView(R.id.content)
    ConstraintLayout content;

    private TranslateAnimation mTranslateAnimation;
    private OnButtonClickListener mListener;

    public FabDialog(@NonNull Context context) {
        super(context, R.style.FabDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fab);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //int flags = content.getSystemUiVisibility();
            //flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            //content.setSystemUiVisibility(flags);
            window.setStatusBarColor(Color.WHITE);
        }
        WindowManager.LayoutParams param = window.getAttributes();
        param.width = WindowManager.LayoutParams.MATCH_PARENT;
        param.height = WindowManager.LayoutParams.MATCH_PARENT;

        mTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.3f,
                Animation.RELATIVE_TO_SELF, 0f);
        mTranslateAnimation.setDuration(50);

        content.setOnClickListener(v -> dismiss());
        textShareNews.setOnClickListener(this::clickNewsButton);
        fabShareNews.setOnClickListener(this::clickNewsButton);
        textCreateTopic.setOnClickListener(this::clickTopicButton);
        fabCreateTopic.setOnClickListener(this::clickTopicButton);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewCompat.animate(fabMenu).rotation(45).setDuration(100).start();
        fabShareNews.setAnimation(mTranslateAnimation);
        fabCreateTopic.startAnimation(mTranslateAnimation);
        textCreateTopic.startAnimation(mTranslateAnimation);
        textShareNews.startAnimation(mTranslateAnimation);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fabMenu.clearAnimation();
        fabShareNews.clearAnimation();
        fabCreateTopic.clearAnimation();
        textCreateTopic.clearAnimation();
        textShareNews.clearAnimation();
    }

    private void clickTopicButton(View view) {
        //dismiss();
        if (mListener != null) {
            mListener.clickTopicButton();
        }
    }

    private void clickNewsButton(View view) {
        //dismiss();
        if (mListener != null) {
            mListener.clickNewsButton();
        }
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mListener = listener;
    }

    public interface OnButtonClickListener {

        void clickNewsButton();

        void clickTopicButton();
    }
}
