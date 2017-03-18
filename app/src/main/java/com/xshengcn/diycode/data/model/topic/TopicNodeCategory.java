package com.xshengcn.diycode.data.model.topic;

public class TopicNodeCategory {
    public int selectionId;
    public String selectionName;

    public TopicNodeCategory() {
    }

    public TopicNodeCategory(int selectionId, String selectionName) {
        this.selectionId = selectionId;
        this.selectionName = selectionName;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TopicNodeCategory
                && ((TopicNodeCategory) obj).selectionId == this.selectionId;
    }

    @Override
    public int hashCode() {
        return selectionId;
    }

    @Override
    public String toString() {
        return selectionName;
    }
}
