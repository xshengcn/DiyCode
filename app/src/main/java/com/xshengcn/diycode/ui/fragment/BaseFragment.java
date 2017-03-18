package com.xshengcn.diycode.ui.fragment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.xshengcn.diycode.injection.component.FragmentComponent;
import com.xshengcn.diycode.injection.module.FragmentModule;
import com.xshengcn.diycode.ui.activity.BaseActivity;

public class BaseFragment extends Fragment {

    private FragmentComponent mFragmentComponent;

    @NonNull
    public FragmentComponent getComponent() {
        if (mFragmentComponent != null) {
            return mFragmentComponent;
        }

        Activity activity = getActivity();
        if (!(activity instanceof BaseActivity)) {
            throw new IllegalStateException(
                    "The activity of this fragment is not an instance of BaseActivity");
        }
        mFragmentComponent = ((BaseActivity) activity).getComponent()
                .plus(new FragmentModule(this));
        return mFragmentComponent;
    }
}
