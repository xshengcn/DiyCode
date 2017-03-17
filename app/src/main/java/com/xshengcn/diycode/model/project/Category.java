package com.xshengcn.diycode.model.project;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Category implements Parcelable{

  @SerializedName("name") public String name;
  @SerializedName("id") public int id;

  public Category() {}

  public Category(Parcel in) {
    name = in.readString();
    id = in.readInt();
  }

  public static final Creator<Category> CREATOR = new Creator<Category>() {
    @Override public Category createFromParcel(Parcel in) {
      return new Category(in);
    }

    @Override public Category[] newArray(int size) {
      return new Category[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeInt(id);
  }
}
