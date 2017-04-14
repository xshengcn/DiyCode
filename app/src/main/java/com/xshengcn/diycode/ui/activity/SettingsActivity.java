package com.xshengcn.diycode.ui.activity;

import static com.xshengcn.diycode.util.RxUtils.applySchedulers;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.PreferencesHelper;
import com.xshengcn.diycode.ui.ActivityNavigator;
import com.xshengcn.diycode.ui.widget.SettingRowView;
import com.xshengcn.diycode.ui.widget.SettingSwitchRowView;
import com.xshengcn.diycode.util.CacheDataUtils;

import java.text.MessageFormat;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.setting_account)
    SettingRowView mSettingAccount;
    @BindView(R.id.setting_account_divider)
    View mSettingAccountDivider;
    @BindView(R.id.setting_account_state)
    TextView mSettingAccountState;
    @BindView(R.id.setting_night_mode)
    SettingSwitchRowView mSettingNightMode;
    @BindView(R.id.setting_clear_cache)
    SettingRowView mSettingClearCache;
    @BindView(R.id.setting_about)
    TextView mSettingAbout;
    @BindView(R.id.setting_licenses)
    TextView mSettingLicenses;
    @BindView(R.id.setting_version)
    SettingRowView mSettingVersion;

    @BindString(R.string.total_cache_description)
    String mCacheDesc;

    @Inject
    PreferencesHelper mPreferencesHelper;
    @Inject
    ActivityNavigator mNavigator;


    public static void start(@Nullable Activity activity) {
        activity.startActivity(new Intent(activity, SettingsActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getComponent().inject(this);
        ButterKnife.bind(this);

        mToolbar.setTitle(R.string.settings);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mPreferencesHelper.getToken() == null) {
            mSettingAccount.setVisibility(View.GONE);
            mSettingAccountDivider.setVisibility(View.GONE);

            mSettingAccountState.setText(R.string.login);
        } else {

//            if (mPreferencesHelper.getUser() != null) {
//                mSettingAccount.setSettingDescription(mPreferencesHelper.getUser().email);
//            } else {
//                mSettingAccount.setSettingDescription("null");
//            }

            mSettingAccountState.setText(R.string.logout);
        }

        setupAppVersion();
        setupAppCache();

    }


    private void setupAppVersion() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(
                    getPackageName(), 0);
            String version = info.versionName;
            mSettingVersion.setSettingDescription(version);
        } catch (NameNotFoundException e) {
            Logger.e(e.getMessage());
        }

    }

    private void setupAppCache() {
        mSettingClearCache.setSettingDescription(
                MessageFormat.format(mCacheDesc, CacheDataUtils.getTotalCacheSize(this)));
    }

    @OnClick(R.id.setting_licenses)
    void clickLicenses() {
        mNavigator.showLicenses();
    }


    @OnClick(R.id.setting_clear_cache)
    void clearCache(View view) {
        Observable.create(o -> {
            try {
                o.onNext(CacheDataUtils.clearAllCache(SettingsActivity.this));
                o.onComplete();
            } catch (Exception ex) {
                o.onError(ex);
            }
        })
                .compose(applySchedulers())
                .subscribe();
    }

    private void managerAccount() {
        if (mPreferencesHelper.getToken() == null) {
            mNavigator.showLogin();
        } else {
            Toast.makeText(this, "退出", Toast.LENGTH_SHORT).show();
        }
    }
}
