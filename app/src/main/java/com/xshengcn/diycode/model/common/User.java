package com.xshengcn.diycode.model.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
  public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
    @Override public User createFromParcel(Parcel in) {
      return new User(in);
    }

    @Override public User[] newArray(int size) {
      return new User[size];
    }
  };
  @SerializedName("id") public int id;
  @SerializedName("login") public String login;
  @SerializedName("name") public String name;
  @SerializedName("avatar_url") public String avatarUrl;

  public User() {
  }

  public User(Parcel in) {
    id = in.readInt();
    login = in.readString();
    name = in.readString();
    avatarUrl = in.readString();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(login);
    dest.writeString(name);
    dest.writeString(avatarUrl);
  }
}
