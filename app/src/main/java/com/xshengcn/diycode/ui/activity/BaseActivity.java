package com.xshengcn.diycode.ui.activity;

import android.annotation.SuppressLint;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.xshengcn.diycode.DiyCodeApplication;
import com.xshengcn.diycode.injection.component.ActivityComponent;
import com.xshengcn.diycode.injection.module.ActivityModule;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @NonNull
    public ActivityComponent getComponent() {
        if (mActivityComponent == null) {
            DiyCodeApplication mainApplication = (DiyCodeApplication) getApplication();
            mActivityComponent = mainApplication.getComponent().plus(new ActivityModule(this));
        }
        return mActivityComponent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    final void replaceFragment(@NonNull Fragment fragment, @IdRes @LayoutRes int layoutResId) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(layoutResId, fragment, fragment.getClass().getSimpleName());
        ft.commit();
    }

}
