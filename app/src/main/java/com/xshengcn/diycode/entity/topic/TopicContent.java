package com.xshengcn.diycode.entity.topic;

import com.google.gson.annotations.SerializedName;
import com.xshengcn.diycode.entity.common.Abilities;
import com.xshengcn.diycode.entity.common.User;
import java.util.Date;

public class TopicContent {

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
  @SerializedName("body") public String body;
  @SerializedName("body_html") public String bodyHtml;
  @SerializedName("hits") public int hits;
  @SerializedName("likes_count") public int likesCount;
  @SerializedName("suggested_at") public String suggestedAt;
  @SerializedName("followed") public boolean followed;
  @SerializedName("liked") public boolean liked;
  @SerializedName("favorited") public boolean favorited;
}
