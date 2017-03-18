package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xshengcn.diycode.R;
import com.xshengcn.diycode.ui.iview.ILoginView;
import com.xshengcn.diycode.ui.presenter.LoginPresenter;
import com.xshengcn.diycode.util.TextWatcherAdapter;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements ILoginView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.usernameWrapper)
    TextInputLayout mUsernameWrapper;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.passwordWrapper)
    TextInputLayout mPasswordWrapper;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @BindString(R.string.username_not_empty)
    String mUsernameNotEmpty;
    @BindString(R.string.password_not_empty)
    String mPasswordNotEmpty;
    @BindString(R.string.tips_login)
    String mTipsLogin;

    @Inject
    LoginPresenter mPresenter;
    private ProgressDialog mLoginDialog;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getComponent().inject(this);
        ButterKnife.bind(this);

        mPresenter.onAttach(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBtnLogin.setOnClickListener(this::login);

        mUsername.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mUsernameWrapper.setError(mUsernameNotEmpty);
                } else {
                    mUsernameWrapper.setErrorEnabled(false);
                }
            }
        });

        mPassword.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mPasswordWrapper.setError(mPasswordNotEmpty);
                } else {
                    mPasswordWrapper.setErrorEnabled(false);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void login(View view) {
        if (TextUtils.isEmpty(getUsername())) {
            mUsernameWrapper.setError(mUsernameNotEmpty);
        } else if (TextUtils.isEmpty(getPassword())) {
            mPasswordWrapper.setError(mPasswordNotEmpty);
        } else {
            mPresenter.login();
        }
    }

    @Override
    public String getUsername() {
        return mUsername.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPassword.getText().toString();
    }

    @Override
    public void showError(String errorDescription) {
        Toast.makeText(this, errorDescription, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginDialog() {
        if (mLoginDialog == null) {
            mLoginDialog = new ProgressDialog(this);
            mLoginDialog.setMessage(mTipsLogin);
        }
        mLoginDialog.show();
    }

    @Override
    public void hideLoginDialog() {
        if (mLoginDialog != null) {
            mLoginDialog.dismiss();
        }
    }

    @Override
    public void closeActivity() {
        finish();
    }
}
