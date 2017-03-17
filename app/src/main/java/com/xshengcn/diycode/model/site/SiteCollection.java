package com.xshengcn.diycode.model.site;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SiteCollection {

  @SerializedName("name") public String name;
  @SerializedName("id") public int id;
  @SerializedName("sites") public List<Site> sites;
}
