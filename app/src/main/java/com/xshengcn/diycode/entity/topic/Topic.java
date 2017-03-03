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
  @SerializedName("id") public int id = -1;
  @SerializedName("title") public String title = null;
  @SerializedName("created_at") public Date createdAt = null;
  @SerializedName("updated_at") public Date updatedAt = null;
  @SerializedName("replied_at") public Date repliedAt = null;
  @SerializedName("replies_count") public int repliesCount = -1;
  @SerializedName("node_name") public String nodeName = null;
  @SerializedName("node_id") public int nodeId = -1;
  @SerializedName("last_reply_user_id") public int lastReplyUserId = -1;
  @SerializedName("last_reply_user_login") public String lastReplyUserLogin = null;
  @SerializedName("user") public User user = null;
  @SerializedName("deleted") public boolean deleted = false;
  @SerializedName("excellent") public boolean excellent = false;
  @SerializedName("abilities") public Abilities abilities = null;

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

  @Override public String toString() {
    return "Topic{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", repliedAt=" + repliedAt +
        ", repliesCount=" + repliesCount +
        ", nodeName='" + nodeName + '\'' +
        ", nodeId=" + nodeId +
        ", lastReplyUserId=" + lastReplyUserId +
        ", lastReplyUserLogin='" + lastReplyUserLogin + '\'' +
        ", user=" + user +
        ", deleted=" + deleted +
        ", excellent=" + excellent +
        ", abilities=" + abilities +
        '}';
  }
}
