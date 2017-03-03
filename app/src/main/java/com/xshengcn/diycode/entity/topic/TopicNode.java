package com.xshengcn.diycode.entity.topic;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class TopicNode {

  @SerializedName("id") public int id;
  @SerializedName("name") public String name;
  @SerializedName("topics_count") public int topicsCount;
  @SerializedName("summary") public String summary;
  @SerializedName("section_id") public int sectionId;
  @SerializedName("sort") public int sort;
  @SerializedName("section_name") public String sectionName;
  @SerializedName("updated_at") public Date updatedAt;
}
