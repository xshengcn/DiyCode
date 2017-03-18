package com.xshengcn.diycode.data.model.site;

public abstract class SiteListItem {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    abstract public int getType();

}
