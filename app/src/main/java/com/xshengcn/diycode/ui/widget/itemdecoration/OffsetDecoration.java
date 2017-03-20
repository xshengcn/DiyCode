package com.xshengcn.diycode.ui.widget.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class OffsetDecoration extends RecyclerView.ItemDecoration {

    private final int mLeft;
    private final int mRight;
    private final int mBottom;
    private final int mTop;

    public OffsetDecoration(int offset) {
        this.mLeft = offset;
        this.mRight = offset;
        this.mBottom = offset;
        this.mTop = offset;
    }

    public OffsetDecoration(int top, int bottom, int right, int left) {
        this.mTop = top;
        this.mBottom = bottom;
        this.mRight = right;
        this.mLeft = left;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            RecyclerView.State state) {
        outRect.left = mLeft;
        outRect.right = mRight;
        outRect.bottom = mBottom;
        outRect.top = mTop;
    }
}
