package com.xshengcn.diycode.data;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.xshengcn.diycode.data.event.UserDetailUpdate;
import com.xshengcn.diycode.data.event.UserLogin;
import com.xshengcn.diycode.data.model.Token;
import com.xshengcn.diycode.data.model.user.UserDetail;
import com.xshengcn.diycode.util.RxBus;
import com.xshengcn.diycode.util.RxUtils;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

public class PreferencesHelper {

    private static final String PREF_FILE_NAME = "DIY_CODE_PREF";
    private static final String KEY_TOKEN_INFO = "KEY_TOKEN_INFO";
    private static final String KEY_USER_INFO = "KEY_USER_INFO";
    private final SharedPreferences mPreferences;
    private final RxBus mBus;

    private Token mToken;

    @Inject
    public PreferencesHelper(Context context, RxBus rxBus) {
        mPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        this.mBus = rxBus;
        String tokenStr = mPreferences.getString(KEY_TOKEN_INFO, null);

        if (!TextUtils.isEmpty(tokenStr)) {
            mToken = new Gson().fromJson(tokenStr, Token.class);
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

    public Single<UserDetail> getUserDetail() {
        return Single.create((SingleOnSubscribe<UserDetail>) e -> {
            String userStr = mPreferences.getString(KEY_USER_INFO, null);
            if (!TextUtils.isEmpty(userStr)) {
                UserDetail detail = new Gson().fromJson(userStr, UserDetail.class);
                e.onSuccess(detail);
            } else {
                e.onError(new RuntimeException("userStr is empty"));
            }
        }).compose(RxUtils.applySingleSchedulers());
    }

    public void setUserDetail(UserDetail user) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(KEY_USER_INFO, new Gson().toJson(user));
        editor.apply();
        mBus.send(new UserDetailUpdate());
    }
}
