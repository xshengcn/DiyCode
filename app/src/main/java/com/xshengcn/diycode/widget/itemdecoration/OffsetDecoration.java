package com.xshengcn.diycode.widget.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class OffsetDecoration extends RecyclerView.ItemDecoration {

  private final int left;
  private final int right;
  private final int bottom;
  private final int top;

  public OffsetDecoration(int offset) {
    left = offset;
    right = offset;
    bottom = offset;
    top = offset;
  }

  public OffsetDecoration(int top, int bottom, int right, int left) {
    this.top = top;
    this.bottom = bottom;
    this.right = right;
    this.left = left;
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    outRect.left = left;
    outRect.right = right;
    outRect.bottom = bottom;
    outRect.top = top;
  }
}
