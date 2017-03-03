package com.xshengcn.diycode;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.xshengcn.diycode.api.event.UserDetailUpdate;
import com.xshengcn.diycode.api.event.UserLogin;
import com.xshengcn.diycode.entity.Token;
import com.xshengcn.diycode.entity.user.UserDetail;
import com.xshengcn.diycode.util.RxBus;
import javax.inject.Inject;

public class DiyCodePrefs {

  private static final String USER_PREF = "DIY_CODE_PREF";
  private static final String KEY_TOKEN_INFO = "KEY_TOKEN_INFO";
  private static final String KEY_USER_INFO = "KEY_USER_INFO";
  private final SharedPreferences prefs;
  private final RxBus rxBus;

  private Token token;
  private UserDetail user;

  @Inject public DiyCodePrefs(Context context, RxBus rxBus) {
    prefs = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
    this.rxBus = rxBus;
    String tokenStr = prefs.getString(KEY_TOKEN_INFO, null);

    if (!TextUtils.isEmpty(tokenStr)) {
      token = new Gson().fromJson(tokenStr, Token.class);
    }

    String userStr = prefs.getString(KEY_USER_INFO, null);
    if (!TextUtils.isEmpty(userStr)) {
      user = new Gson().fromJson(userStr, UserDetail.class);
    }
  }

  public Token getToken() {
    return token;
  }

  public void setToken(Token token) {
    this.token = token;
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(KEY_TOKEN_INFO, new Gson().toJson(token));
    editor.apply();

    rxBus.send(new UserLogin());
  }

  public UserDetail getUser() {
    return user;
  }

  public void setUser(UserDetail user) {
    this.user = user;
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(KEY_USER_INFO, new Gson().toJson(user));
    editor.apply();

    rxBus.send(new UserDetailUpdate());
  }
}
