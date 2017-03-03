package com.xshengcn.diycode.ui.login;

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
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.ui.BaseActivity;
import com.xshengcn.diycode.util.TextWatcherAdapter;
import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements ILoginView {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.username) EditText username;
  @BindView(R.id.password) EditText password;
  @BindView(R.id.login) Button login;
  @BindView(R.id.usernameWrapper) TextInputLayout usernameWrapper;
  @BindView(R.id.passwordWrapper) TextInputLayout passwordWrapper;
  @BindString(R.string.username_not_empty) String usernameNotEmpty;
  @BindString(R.string.password_not_empty) String passwordNotEmpty;
  @BindString(R.string.tips_login) String tips_login;

  @Inject LoginPresenter presenter;

  private ProgressDialog loginDialog;

  public static void start(Activity activity) {
    activity.startActivity(new Intent(activity, LoginActivity.class));
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    getComponent().inject(this);
    ButterKnife.bind(this);

    presenter.onAttach(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("");
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    login.setOnClickListener(view -> login(view));

    username.addTextChangedListener(new TextWatcherAdapter() {
      @Override public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
          usernameWrapper.setError(usernameNotEmpty);
        } else {
          usernameWrapper.setErrorEnabled(false);
        }
      }
    });

    password.addTextChangedListener(new TextWatcherAdapter() {
      @Override public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
          passwordWrapper.setError(passwordNotEmpty);
        } else {
          passwordWrapper.setErrorEnabled(false);
        }
      }
    });
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        super.onBackPressed();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void login(View view) {
    if (TextUtils.isEmpty(getUsername())) {
      usernameWrapper.setError(usernameNotEmpty);
    } else if (TextUtils.isEmpty(getPassword())) {
      passwordWrapper.setError(passwordNotEmpty);
    } else {
      presenter.login();
    }
  }

  @Override public String getUsername() {
    return username.getText().toString();
  }

  @Override public String getPassword() {
    return password.getText().toString();
  }

  @Override public void showError(String errorDescription) {
    Toast.makeText(this, errorDescription, Toast.LENGTH_SHORT).show();
  }

  @Override public void showLoginDialog() {
    if (loginDialog == null) {
      loginDialog = new ProgressDialog(this);
      loginDialog.setMessage(tips_login);
    }
    loginDialog.show();
  }

  @Override public void hideLoginDialog() {
    if (loginDialog != null) {
      loginDialog.dismiss();
    }
  }

  @Override public void closeActivity() {
    finish();
  }
}
