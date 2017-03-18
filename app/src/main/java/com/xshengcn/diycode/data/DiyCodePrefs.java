package com.xshengcn.diycode.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xshengcn.diycode.data.event.UserDetailUpdate;
import com.xshengcn.diycode.data.event.UserLogin;
import com.xshengcn.diycode.data.model.Token;
import com.xshengcn.diycode.data.model.user.UserDetail;
import com.xshengcn.diycode.util.RxBus;

import javax.inject.Inject;

public class DiyCodePrefs {

    private static final String USER_PREF = "DIY_CODE_PREF";
    private static final String KEY_TOKEN_INFO = "KEY_TOKEN_INFO";
    private static final String KEY_USER_INFO = "KEY_USER_INFO";
    private final SharedPreferences mPreferences;
    private final RxBus mBus;

    private Token mToken;
    private UserDetail mUser;

    @Inject
    public DiyCodePrefs(Context context, RxBus rxBus) {
        mPreferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        this.mBus = rxBus;
        String tokenStr = mPreferences.getString(KEY_TOKEN_INFO, null);

        if (!TextUtils.isEmpty(tokenStr)) {
            mToken = new Gson().fromJson(tokenStr, Token.class);
        }

        String userStr = mPreferences.getString(KEY_USER_INFO, null);
        if (!TextUtils.isEmpty(userStr)) {
            mUser = new Gson().fromJson(userStr, UserDetail.class);
        }
    }

    public Token getToken() {
        return mToken;
    }

    public void setToken(Token token) {
        this.mToken = token;
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(KEY_TOKEN_INFO, new Gson().toJson(token));
        editor.apply();

        mBus.send(new UserLogin());
    }

    public UserDetail getUser() {
        return mUser;
    }

    public void setUser(UserDetail user) {
        this.mUser = user;
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(KEY_USER_INFO, new Gson().toJson(user));
        editor.apply();
        mBus.send(new UserDetailUpdate());
    }
}
