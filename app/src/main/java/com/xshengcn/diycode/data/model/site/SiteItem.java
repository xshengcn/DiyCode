package com.xshengcn.diycode.data.model.site;

public class SiteItem extends SiteListItem {

    public Site site;

    public SiteItem() {
    }

    public SiteItem(Site site) {
        this.site = site;
    }

    @Override
    public int getType() {
        return TYPE_ITEM;
    }
}
