package com.xshengcn.diycode.ui.login;

public interface ILoginView {

  String getUsername();

  String getPassword();

  void showError(String errorDescription);

  void showLoginDialog();

  void hideLoginDialog();

  void closeActivity();
}
