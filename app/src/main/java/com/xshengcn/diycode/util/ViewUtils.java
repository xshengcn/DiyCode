package com.xshengcn.diycode.util;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ViewUtils {

  public static void setViewVisibility(View view, int visibility) {
    if (view != null) {
      view.setVisibility(visibility);
    }
  }

  public static int findFirstVisibleItemPosition(RecyclerView recyclerView) {
    final View child =
        findOneVisibleChild(recyclerView, 0, recyclerView.getLayoutManager().getChildCount(), false,
            true);
    return child == null ? RecyclerView.NO_POSITION : recyclerView.getChildAdapterPosition(child);
  }

  private static View findOneVisibleChild(RecyclerView recyclerView, int fromIndex, int toIndex,
      boolean completelyVisible, boolean acceptPartiallyVisible) {
    OrientationHelper helper;
    if (recyclerView.getLayoutManager().canScrollVertically()) {
      helper = OrientationHelper.createVerticalHelper(recyclerView.getLayoutManager());
    } else {
      helper = OrientationHelper.createHorizontalHelper(recyclerView.getLayoutManager());
    }

    final int start = helper.getStartAfterPadding();
    final int end = helper.getEndAfterPadding();
    final int next = toIndex > fromIndex ? 1 : -1;
    View partiallyVisible = null;
    for (int i = fromIndex; i != toIndex; i += next) {
      final View child = recyclerView.getLayoutManager().getChildAt(i);
      final int childStart = helper.getDecoratedStart(child);
      final int childEnd = helper.getDecoratedEnd(child);
      if (childStart < end && childEnd > start) {
        if (completelyVisible) {
          if (childStart >= start && childEnd <= end) {
            return child;
          } else if (acceptPartiallyVisible && partiallyVisible == null) {
            partiallyVisible = child;
          }
        } else {
          return child;
        }
      }
    }
    return partiallyVisible;
  }
}
