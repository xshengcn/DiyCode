package com.xshengcn.diycode.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.site.SiteHeaderItem;
import com.xshengcn.diycode.data.model.site.SiteItem;
import com.xshengcn.diycode.data.model.site.SiteListItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SiteAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private ArrayList<SiteListItem> mItems;
    private OnItemClickListener mOnItemClickListener;

    @Inject
    public SiteAdapter(Context context) {
        this.mContext = context;
        mItems = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SiteListItem.TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_site_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_site, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == SiteListItem.TYPE_ITEM) {
            bindItemViewHolder((ItemViewHolder) holder, (SiteItem) mItems.get(position));
        } else if (getItemViewType(position) == SiteListItem.TYPE_HEADER) {
            bindHeaderViewHolder((HeaderViewHolder) holder, (SiteHeaderItem) mItems.get(position));
        }
    }

    private void bindHeaderViewHolder(HeaderViewHolder holder, SiteHeaderItem siteHeaderItem) {
        holder.header.setText(siteHeaderItem.name);
    }

    private void bindItemViewHolder(ItemViewHolder holder, SiteItem siteItem) {
        Glide.with(mContext).load(siteItem.site.avatarUrl).into(holder.avatar);
        holder.name.setText(siteItem.site.name);
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.clickItem(siteItem.site.url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addSiteListItems(List<SiteListItem> siteListItems) {
        this.mItems.addAll(siteListItems);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {

        void clickItem(String url);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.site_header)
        TextView header;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.name)
        TextView name;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
