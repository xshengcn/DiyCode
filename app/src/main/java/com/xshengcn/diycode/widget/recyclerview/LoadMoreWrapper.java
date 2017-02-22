package com.xshengcn.diycode.widget.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.util.ViewUtils;

public class LoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int ITEM_TYPE_LOAD_FAILED_VIEW = Integer.MAX_VALUE - 1;
  public static final int ITEM_TYPE_NO_MORE_VIEW = Integer.MAX_VALUE - 2;
  public static final int ITEM_TYPE_LOAD_MORE_VIEW = Integer.MAX_VALUE - 3;
  public static final int ITEM_TYPE_NO_VIEW = Integer.MAX_VALUE - 4;//不展示footer view
  private final LoadMoreHandler handler;
  private final RecyclerView.Adapter innerAdapter;
  private int currentType = ITEM_TYPE_NO_VIEW;
  private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      if (handler == null || !handler.canLoadMore() || currentType != ITEM_TYPE_NO_VIEW) {
        return;
      }

      int visibleItemCount = recyclerView.getChildCount();
      int totalItemCount = getItemCount();
      int firstVisibleItem = ViewUtils.findFirstVisibleItemPosition(recyclerView);

      if (totalItemCount == 0) {
        return;
      }

      if ((totalItemCount - visibleItemCount) <= firstVisibleItem
          && !recyclerView.isComputingLayout()) {
        recyclerView.post(() -> {
          showLoadMore();
          handler.loadMore();
        });
      }
    }
  };

  public LoadMoreWrapper(@NonNull LoadMoreHandler handler,
      @NonNull RecyclerView.Adapter innerAdapter) {
    this.handler = handler;
    this.innerAdapter = innerAdapter;
  }

  public void showLoadMore() {
    if (currentType == ITEM_TYPE_LOAD_MORE_VIEW) {
      return;
    }
    boolean insert = currentType == ITEM_TYPE_NO_VIEW;
    currentType = ITEM_TYPE_LOAD_MORE_VIEW;
    if (insert) {
      notifyItemInserted(getItemCount());
    } else {
      notifyItemChanged(getItemCount());
    }
  }

  public void showLoadFailed() {
    if (currentType == ITEM_TYPE_LOAD_FAILED_VIEW) {
      return;
    }
    boolean insert = currentType == ITEM_TYPE_NO_VIEW;
    currentType = ITEM_TYPE_LOAD_FAILED_VIEW;
    if (insert) {
      notifyItemInserted(getItemCount());
    } else {
      notifyItemChanged(getItemCount());
    }
  }

  public void showNoMore() {
    if (currentType == ITEM_TYPE_NO_MORE_VIEW) {
      return;
    }
    boolean insert = currentType == ITEM_TYPE_NO_VIEW;
    currentType = ITEM_TYPE_NO_MORE_VIEW;
    if (insert) {
      notifyItemInserted(getItemCount());
    } else {
      notifyItemChanged(getItemCount());
    }
  }

  public void hideFooter() {
    if (currentType == ITEM_TYPE_NO_VIEW) {
      return;
    }
    currentType = ITEM_TYPE_NO_VIEW;
    notifyItemRemoved(getItemCount());
  }

  @Override public int getItemCount() {
    int itemCount = innerAdapter.getItemCount();
    return currentType == ITEM_TYPE_NO_VIEW ? itemCount : itemCount + 1;
  }

  @Override public int getItemViewType(int position) {
    if (currentType != ITEM_TYPE_NO_VIEW && position == getItemCount() - 1) {
      return currentType;
    } else {
      return innerAdapter.getItemViewType(position);
    }
  }

  @Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    recyclerView.addOnScrollListener(scrollListener);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM_TYPE_LOAD_MORE_VIEW) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.view_item_load_more, parent, false);
      return new LoadMoreViewHolder(view);
    } else if (viewType == ITEM_TYPE_LOAD_FAILED_VIEW) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.view_item_load_failed, parent, false);
      return new LoadFailedViewHolder(view);
    } else if (viewType == ITEM_TYPE_NO_MORE_VIEW) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.view_item_no_more, parent, false);
      return new NoMoreViewHolder(view);
    } else {
      return innerAdapter.onCreateViewHolder(parent, viewType);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof LoadMoreViewHolder) {

    } else if (holder instanceof LoadFailedViewHolder) {

    } else if (holder instanceof NoMoreViewHolder) {

    } else {
      innerAdapter.onBindViewHolder(holder, position);
    }
  }

  @Override public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    recyclerView.removeOnScrollListener(scrollListener);
    super.onDetachedFromRecyclerView(recyclerView);
  }

  class LoadMoreViewHolder extends RecyclerView.ViewHolder {
    public LoadMoreViewHolder(View itemView) {
      super(itemView);
    }
  }

  class LoadFailedViewHolder extends RecyclerView.ViewHolder {
    public LoadFailedViewHolder(View itemView) {
      super(itemView);
    }
  }

  class NoMoreViewHolder extends RecyclerView.ViewHolder {
    public NoMoreViewHolder(View itemView) {
      super(itemView);
    }
  }
}

