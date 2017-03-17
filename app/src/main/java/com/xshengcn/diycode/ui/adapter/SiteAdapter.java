package com.xshengcn.diycode.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.model.site.SiteHeaderItem;
import com.xshengcn.diycode.model.site.SiteItem;
import com.xshengcn.diycode.model.site.SiteListItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class SiteAdapter extends RecyclerView.Adapter {

  private ArrayList<SiteListItem> items;
  private final Context context;
  private OnItemClickListener onItemClickListener;

  @Inject public SiteAdapter(Context context) {
    this.context = context;
    items = new ArrayList<>();
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  @Override public int getItemViewType(int position) {
    return items.get(position).getType();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == SiteListItem.TYPE_HEADER) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.view_item_site_header, parent, false);
      return new HeaderViewHolder(view);
    } else {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_site, parent, false);
      return new ItemViewHolder(view);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (getItemViewType(position) == SiteListItem.TYPE_ITEM) {
      bindItemViewHolder((ItemViewHolder) holder, (SiteItem) items.get(position));
    } else if (getItemViewType(position) == SiteListItem.TYPE_HEADER) {
      bindHeaderViewHolder((HeaderViewHolder) holder, (SiteHeaderItem) items.get(position));
    }
  }

  private void bindHeaderViewHolder(HeaderViewHolder holder, SiteHeaderItem siteHeaderItem) {
    holder.header.setText(siteHeaderItem.name);
  }

  private void bindItemViewHolder(ItemViewHolder holder, SiteItem siteItem) {
    Glide.with(context).load(siteItem.site.avatarUrl).into(holder.avatar);
    holder.name.setText(siteItem.site.name);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if(onItemClickListener!=null) {
          onItemClickListener.clickItem(siteItem.site.url);
        }
      }
    });
  }

  @Override public int getItemCount() {
    return items.size();
  }

  public void addSiteListItems(List<SiteListItem> siteListItems) {
    this.items.addAll(siteListItems);
    notifyDataSetChanged();
  }

  public  interface OnItemClickListener {
    void clickItem(String url);
  }

  class HeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.site_header) TextView header;

    public HeaderViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  class ItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.name) TextView name;

    public ItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
