package com.xshengcn.diycode.model;

import com.google.gson.annotations.SerializedName;

public class Token {

  @SerializedName("access_token") public String accessToken;
  @SerializedName("token_type") public String tokenType;
  @SerializedName("expires_in") public int expiresIn;
  @SerializedName("refresh_token") public String refreshToken;
  @SerializedName("created_at") public int createdAt;
}
