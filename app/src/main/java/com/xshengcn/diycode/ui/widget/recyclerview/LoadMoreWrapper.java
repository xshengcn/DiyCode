package com.xshengcn.diycode.ui.widget.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.util.ViewUtils;

public class LoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_LOAD_FAILED_VIEW = Integer.MAX_VALUE - 1;
    public static final int ITEM_TYPE_NO_MORE_VIEW = Integer.MAX_VALUE - 2;
    public static final int ITEM_TYPE_LOAD_MORE_VIEW = Integer.MAX_VALUE - 3;
    public static final int ITEM_TYPE_NO_VIEW = Integer.MAX_VALUE - 4;

    private final LoadMoreHandler mHandler;
    private final RecyclerView.Adapter mInnerAdapter;
    private int mCurrentType = ITEM_TYPE_NO_VIEW;

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (!mHandler.canLoadMore() || (mCurrentType != ITEM_TYPE_NO_VIEW)) {
                return;
            }

            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = getItemCount();
            int firstVisibleItem = ViewUtils.findFirstVisibleItemPosition(recyclerView);

            if (totalItemCount == 0) {
                return;
            }

            if (((totalItemCount - visibleItemCount) <= firstVisibleItem)
                    && !recyclerView.isComputingLayout()) {
                recyclerView.post(() -> {
                    showLoadMore();
                    mHandler.loadMore();
                });
            }
        }
    };

    private RecyclerView.AdapterDataObserver mAdapterDataObserver =
            new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {

                    LoadMoreWrapper.this.notifyDataSetChanged();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    LoadMoreWrapper.this.notifyItemRangeChanged(positionStart, itemCount);
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                    LoadMoreWrapper.this.notifyItemRangeChanged(positionStart, itemCount, payload);
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    LoadMoreWrapper.this.notifyItemRangeInserted(positionStart, itemCount);
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    LoadMoreWrapper.this.notifyItemRangeRemoved(positionStart, itemCount);
                }
            };

    public LoadMoreWrapper(@NonNull LoadMoreHandler handler,
            @NonNull RecyclerView.Adapter innerAdapter) {
        this.mHandler = handler;
        this.mInnerAdapter = innerAdapter;
    }

    public void showLoadMore() {
        if (mCurrentType == ITEM_TYPE_LOAD_MORE_VIEW) {
            return;
        }
        boolean insert = mCurrentType == ITEM_TYPE_NO_VIEW;
        mCurrentType = ITEM_TYPE_LOAD_MORE_VIEW;
        if (insert) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemChanged(getItemCount());
        }
    }

    public void showLoadFailed() {
        if (mCurrentType == ITEM_TYPE_LOAD_FAILED_VIEW) {
            return;
        }
        boolean insert = mCurrentType == ITEM_TYPE_NO_VIEW;
        mCurrentType = ITEM_TYPE_LOAD_FAILED_VIEW;
        if (insert) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemChanged(getItemCount());
        }
    }

    public void showNoMore() {
        if (mCurrentType == ITEM_TYPE_NO_MORE_VIEW) {
            return;
        }
        boolean insert = mCurrentType == ITEM_TYPE_NO_VIEW;
        mCurrentType = ITEM_TYPE_NO_MORE_VIEW;
        if (insert) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemChanged(getItemCount());
        }
    }

    public void hideFooter() {
        if (mCurrentType == ITEM_TYPE_NO_VIEW) {
            return;
        }
        mCurrentType = ITEM_TYPE_NO_VIEW;
        notifyItemRemoved(getItemCount());
    }

    @Override
    public int getItemCount() {
        int itemCount = mInnerAdapter.getItemCount();
        return (mCurrentType == ITEM_TYPE_NO_VIEW) ? itemCount : (itemCount + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if ((mCurrentType != ITEM_TYPE_NO_VIEW) && (position == (getItemCount() - 1))) {
            return mCurrentType;
        } else {
            return mInnerAdapter.getItemViewType(position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(mScrollListener);
        mInnerAdapter.registerAdapterDataObserver(mAdapterDataObserver);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ((holder instanceof LoadMoreViewHolder)) {
            Logger.d("LOAD MORE");
        } else if (holder instanceof LoadFailedViewHolder) {
            Logger.d("LOAD MORE FAILED");
        } else if (holder instanceof NoMoreViewHolder) {
            Logger.d("NO MORE");
        } else {
            mInnerAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        recyclerView.removeOnScrollListener(mScrollListener);
        mInnerAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
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

