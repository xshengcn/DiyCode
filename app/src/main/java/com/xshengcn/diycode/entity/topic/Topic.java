package com.xshengcn.diycode.entity.topic;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.xshengcn.diycode.entity.common.Abilities;
import com.xshengcn.diycode.entity.common.User;
import java.util.Date;

public class Topic implements Parcelable {

  public static final Creator<Topic> CREATOR = new Creator<Topic>() {
    @Override public Topic createFromParcel(Parcel in) {
      return new Topic(in);
    }

    @Override public Topic[] newArray(int size) {
      return new Topic[size];
    }
  };
  @SerializedName("id") public int id;
  @SerializedName("title") public String title;
  @SerializedName("created_at") public Date createdAt;
  @SerializedName("updated_at") public Date updatedAt;
  @SerializedName("replied_at") public Date repliedAt;
  @SerializedName("replies_count") public int repliesCount;
  @SerializedName("node_name") public String nodeName;
  @SerializedName("node_id") public int nodeId;
  @SerializedName("last_reply_user_id") public int lastReplyUserId;
  @SerializedName("last_reply_user_login") public String lastReplyUserLogin;
  @SerializedName("user") public User user;
  @SerializedName("deleted") public boolean deleted;
  @SerializedName("excellent") public boolean excellent;
  @SerializedName("abilities") public Abilities abilities;

  protected Topic(Parcel in) {
    id = in.readInt();
    title = in.readString();
    repliesCount = in.readInt();
    nodeName = in.readString();
    nodeId = in.readInt();
    lastReplyUserId = in.readInt();
    lastReplyUserLogin = in.readString();
    user = in.readParcelable(User.class.getClassLoader());
    deleted = in.readByte() != 0;
    excellent = in.readByte() != 0;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(title);
    dest.writeInt(repliesCount);
    dest.writeString(nodeName);
    dest.writeInt(nodeId);
    dest.writeInt(lastReplyUserId);
    dest.writeString(lastReplyUserLogin);
    dest.writeParcelable(user, flags);
    dest.writeByte((byte) (deleted ? 1 : 0));
    dest.writeByte((byte) (excellent ? 1 : 0));
  }
}
