package com.xshengcn.diycode.entity.user;

import com.google.gson.annotations.SerializedName;
import com.xshengcn.diycode.entity.common.User;

public class Notification {

  @SerializedName("id") public int id;
  @SerializedName("type") public String type;
  @SerializedName("read") public boolean read;
  @SerializedName("actor") public User actor;
  @SerializedName("mention_type") public String mentionType;
  @SerializedName("mention") public String mention;
  @SerializedName("topic") public String topic;
  @SerializedName("reply") public String reply;
  @SerializedName("node") public String node;
  @SerializedName("created_at") public String createdAt;
  @SerializedName("updated_at") public String updatedAt;

}
