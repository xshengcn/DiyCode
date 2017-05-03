package com.xshengcn.diycode.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xshengcn.diycode.R;
import com.xshengcn.diycode.util.AppUtils;

public class SettingSwitchRowView extends RelativeLayout {

    private View mRootView;
    private TextView mSettingTitle;
    private TextView mSettingDescription;
    private SwitchCompat mSettingSwitch;


    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    public SettingSwitchRowView(Context context) {
        this(context, null);
    }

    public SettingSwitchRowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingSwitchRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRootView = inflate(context, R.layout.view_setting_switch_row, this);

        mSettingTitle = (TextView) findViewById(R.id.setting_title);
        mSettingDescription = (TextView) findViewById(R.id.setting_description);
        mSettingSwitch = (SwitchCompat) findViewById(R.id.setting_switch);

        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RowView);

            String title = a.getString(R.styleable.RowView_rowTitle);
            String description = a.getString(R.styleable.RowView_rowDescription);

            mSettingTitle.setText(title);
            mSettingDescription.setText(description);

            this.setOnClickListener(v -> toggle());
            mSettingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
                }
            });

            a.recycle();
        }
    }

    public void init(boolean defaultValue, CompoundButton.OnCheckedChangeListener listener) {
        mSettingSwitch.setChecked(defaultValue);
        mOnCheckedChangeListener = listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mRootView.setEnabled(enabled);
        mSettingSwitch.setEnabled(enabled);
        if (enabled) {
            mSettingTitle.setTextColor(AppUtils.getColor(getContext(), R.color.colorTextPrimary));
            mSettingDescription.setTextColor(AppUtils.getColor(getContext(), R.color.colorTextTertiary));
        } else {
            int disabledTextColor = AppUtils.getColor(getContext(), R.color.colorTextQuaternary);
            mSettingTitle.setTextColor(disabledTextColor);
            mSettingDescription.setTextColor(disabledTextColor);
        }
    }

    private void toggle() {
        boolean isChecked = mSettingSwitch.isChecked();
        mSettingSwitch.setChecked(!isChecked);
    }
}
