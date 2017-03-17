package com.xshengcn.diycode.model.site;

public class SiteHeaderItem extends SiteListItem {

  public String name;
  public int id;

  public SiteHeaderItem() {
  }

  public SiteHeaderItem(String name, int id) {
    this.name = name;
    this.id = id;
  }

  @Override public int getType() {
    return TYPE_HEADER;
  }
}
