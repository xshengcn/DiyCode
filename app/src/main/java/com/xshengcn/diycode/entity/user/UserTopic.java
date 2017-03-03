package com.xshengcn.diycode.entity.user;

import com.google.gson.annotations.SerializedName;
import com.xshengcn.diycode.entity.common.Abilities;
import com.xshengcn.diycode.entity.common.User;
import java.util.Date;

public class UserTopic {

  @SerializedName("id") public int id;
  @SerializedName("body_html") public String bodyHtml;
  @SerializedName("created_at") public Date createdAt;
  @SerializedName("updated_at") public String updatedAt;
  @SerializedName("deleted") public String deleted;
  @SerializedName("topic_id") public int topicId;
  @SerializedName("user") public User user;
  @SerializedName("likes_count") public int likesCount;
  @SerializedName("abilities") public Abilities abilities;
  @SerializedName("body") public String body;
  @SerializedName("topic_title") public String topicTitle;
}
