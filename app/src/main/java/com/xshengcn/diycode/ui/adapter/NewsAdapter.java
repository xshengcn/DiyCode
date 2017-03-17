package com.xshengcn.diycode.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.model.news.News;
import com.xshengcn.diycode.util.DateUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import okhttp3.HttpUrl;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

  private final ArrayList<News> newses;
  private OnItemClickListener onItemClickListener;
  private final Context context;

  @Inject public NewsAdapter(Context context) {
    this.context = context;
    newses = new ArrayList<>();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_news, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    News news = newses.get(position);

    holder.title.setText(news.title);

    //        holder.node.setText(news.nodeName);
    //        Logger.d(news.repliesCount);
    //        if (news.repliesCount > 0) {
    //            holder.repliesCount.setVisibility(View.VISIBLE);
    //        } else {
    //            holder.repliesCount.setVisibility(View.INVISIBLE);
    //        }
    //        holder.repliesCount.setText(String.valueOf(news.repliesCount));

    holder.name.setText(news.user.name);

    holder.node.setText(news.nodeName);

    holder.url.setText(HttpUrl.parse(news.address).host());

    if (news.repliedAt != null) {
      holder.date.setText(DateUtils.computePastTime(news.repliedAt));
    } else if (news.createdAt != null) {
      holder.date.setText(DateUtils.computePastTime(news.createdAt));
    }

    //        if (TextUtils.isEmpty(news.lastReplyUserLogin)) {
    //            holder.lastReplySplit.setVisibility(View.INVISIBLE);
    //            holder.lastReplyTips.setVisibility(View.INVISIBLE);
    //            holder.lastReply.setVisibility(View.INVISIBLE);
    //        } else {
    //            holder.lastReplySplit.setVisibility(View.VISIBLE);
    //            holder.lastReplyTips.setVisibility(View.VISIBLE);
    //            holder.lastReply.setVisibility(View.VISIBLE);
    //
    //            holder.lastReply.setText(news.lastReplyUserLogin);
    //        }

    Glide.with(context).load(news.user.avatarUrl).into(holder.avatar);
    holder.itemView.setOnClickListener(v -> {
      if (onItemClickListener != null) onItemClickListener.clickItem(news, position);
    });

    holder.thumbUp.setOnClickListener(v -> {
      if (onItemClickListener != null) onItemClickListener.clickThumbUp(news, position);
    });

    holder.favorite.setOnClickListener(v -> {
      if (onItemClickListener != null) onItemClickListener.clickFavorite(news, position);
    });
  }

  @Override public int getItemCount() {
    return newses.size();
  }

  public void addNewses(List<News> newses) {
    int positionStart = getItemCount();
    this.newses.addAll(newses);
    notifyItemRangeInserted(positionStart, newses.size());
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public void clear() {
    newses.clear();
  }

  public ArrayList<News> getNewses() {
    return newses;
  }

  public interface OnItemClickListener {

    void clickItem(News news, int position);

    void clickThumbUp(News news, int position);

    void clickFavorite(News news, int position);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.node) TextView node;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.item_header) RelativeLayout itemHeader;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.url) TextView url;
    @BindView(R.id.thumb_up) ImageView thumbUp;
    @BindView(R.id.favorite) ImageView favorite;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
