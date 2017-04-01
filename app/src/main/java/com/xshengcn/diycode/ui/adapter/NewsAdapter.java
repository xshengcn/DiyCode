package com.xshengcn.diycode.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.news.News;
import com.xshengcn.diycode.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.HttpUrl;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final ArrayList<News> mNewses;
    private final Context mContext;
    private OnItemClickListener mOnItemClickListener;

    @Inject
    public NewsAdapter(Context context) {
        this.mContext = context;
        mNewses = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = mNewses.get(position);

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
            holder.date.setText(DateUtils.computePastTime(mContext, news.repliedAt));
        } else if (news.createdAt != null) {
            holder.date.setText(DateUtils.computePastTime(mContext, news.createdAt));
        }

        Glide.with(mContext).load(news.user.avatarUrl.replace("large_avatar", "avatar"))
                .into(holder.avatar);
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.clickItem(news, position);
            }
        });

        holder.thumbUp.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.clickThumbUp(news, position);
            }
        });

        holder.favorite.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.clickFavorite(news, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewses.size();
    }

    public void addNewses(List<News> newses) {
        int positionStart = getItemCount();
        this.mNewses.addAll(newses);
        notifyItemRangeInserted(positionStart, newses.size());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void clear() {
        mNewses.clear();
    }

    public ArrayList<News> getNewses() {
        return mNewses;
    }

    public interface OnItemClickListener {

        void clickItem(News news, int position);

        void clickThumbUp(News news, int position);

        void clickFavorite(News news, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.node)
        TextView node;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.item_header)
        RelativeLayout itemHeader;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.url)
        TextView url;
        @BindView(R.id.thumb_up)
        ImageView thumbUp;
        @BindView(R.id.favorite)
        ImageView favorite;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
