package com.xshengcn.diycode.data.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.xshengcn.diycode.data.model.common.User;

import java.util.Date;

public class News implements Parcelable {

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("created_at")
    public Date createdAt;
    @SerializedName("updated_at")
    public Date updatedAt;
    @SerializedName("user")
    public User user;
    @SerializedName("node_name")
    public String nodeName;
    @SerializedName("node_id")
    public int nodeId;
    @SerializedName("last_reply_user_id")
    public Integer lastReplyUserId;
    @SerializedName("last_reply_user_login")
    public String lastReplyUserLogin;
    @SerializedName("replied_at")
    public Date repliedAt;
    @SerializedName("address")
    public String address;
    @SerializedName("replies_count")
    public int repliesCount;

    public News() {
    }

    public News(Parcel in) {
        id = in.readInt();
        title = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        nodeName = in.readString();
        nodeId = in.readInt();
        lastReplyUserLogin = in.readString();
        address = in.readString();
        repliesCount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeParcelable(user, flags);
        dest.writeString(nodeName);
        dest.writeInt(nodeId);
        dest.writeString(lastReplyUserLogin);
        dest.writeString(address);
        dest.writeInt(repliesCount);
    }
}
