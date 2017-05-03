package com.xshengcn.diycode.ui.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xshengcn.diycode.R;
import com.xshengcn.diycode.util.AppUtils;

public class TitleDescriptionView extends LinearLayout {

    private View mRootView;
    private TextView mTitle;
    private TextView mDescription;

    public TitleDescriptionView(Context context) {
        this(context, null);
    }

    public TitleDescriptionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleDescriptionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRootView = inflate(context, R.layout.view_title_description, this);
        mTitle = (TextView) findViewById(R.id.title);
        mDescription = (TextView) findViewById(R.id.description);
        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RowView);

            String title = a.getString(R.styleable.RowView_rowTitle);
            String description = a.getString(R.styleable.RowView_rowDescription);

            mTitle.setText(title);
            mDescription.setText(description);

            a.recycle();
        }

    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setDescription(String description) {
        mDescription.setText(description);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mRootView.setEnabled(enabled);
        if (enabled) {
            mTitle.setTextColor(AppUtils.getColor(getContext(), R.color.colorTextPrimary));
            mDescription.setTextColor(AppUtils.getColor(getContext(), R.color.colorTextTertiary));
        } else {
            int disabledTextColor = AppUtils.getColor(getContext(), R.color.colorTextQuaternary);
            mTitle.setTextColor(disabledTextColor);
            mDescription.setTextColor(disabledTextColor);
        }
    }

}
