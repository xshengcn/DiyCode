package com.xshengcn.diycode.model.site;

public class SiteItem extends SiteListItem {

  public Site site;

  public SiteItem() {
  }

  public SiteItem(Site site) {
    this.site = site;
  }

  @Override public int getType() {
    return TYPE_ITEM;
  }
}
