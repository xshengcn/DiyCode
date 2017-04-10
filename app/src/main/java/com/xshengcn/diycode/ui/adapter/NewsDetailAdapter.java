package com.xshengcn.diycode.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.news.News;
import com.xshengcn.diycode.data.model.news.NewsReply;
import com.xshengcn.diycode.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM_NEWS = 0;
    private static final int TYPE_ITEM_REPLY = 1;

    private final Context mContext;
    private final List<Object> mItems;

    private final int mImgMaxWidth;

    @Inject
    public NewsDetailAdapter(Context context) {
        this.mContext = context;
        mItems = new ArrayList<>();

        int margin = DensityUtil.dp2px(context, 16 * 2);
        mImgMaxWidth = DensityUtil.getScreenWidth(context) - margin;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof News) {
            return TYPE_ITEM_NEWS;
        }
        return TYPE_ITEM_REPLY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM_REPLY) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_news_reply, parent, false);
            return new ViewHolder(view);
        } else if (viewType == TYPE_ITEM_NEWS) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_news_reply_header, parent, false);
            return new HeaderViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addReplies(List<NewsReply> replies) {
        mItems.addAll(replies);
    }

    public void addNews(News news) {
        mItems.add(news);
    }

    public interface OnUrlClickListener {

    }

    public interface OnImageClickListener {

    }

    private static class ClickableURLSpan extends ClickableSpan {

        private String mUrl;

        ClickableURLSpan(String url) {
            mUrl = url;
        }

        @Override
        public void onClick(View widget) {
            Toast.makeText(widget.getContext(), "hello!", Toast.LENGTH_LONG).show();
        }
    }

    private static class ClickableImageSpan extends ClickableSpan {

        private String mSource;

        public ClickableImageSpan(String source) {
            this.mSource = source;
        }

        @Override
        public void onClick(View widget) {
            Logger.d(mSource);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.body)
        TextView body;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
