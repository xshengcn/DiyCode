package com.xshengcn.diycode.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.topic.TopicAndReplies;
import com.xshengcn.diycode.data.model.topic.TopicDetail;
import com.xshengcn.diycode.data.model.topic.TopicReply;
import com.xshengcn.diycode.util.DateUtils;
import com.xshengcn.diycode.util.DensityUtil;
import com.xshengcn.diycode.util.HtmlUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicDetailAdapter extends RecyclerView.Adapter {

    private static final int TYPE_TOPIC_HEADER = 0x01;
    private static final int TYPE_TOPIC_ITEM = 0x02;
    private static final int TYPE_TOPIC_ITEM_DELETE = 0x03;

    private final Context mContext;
    private final TopicAndReplies mTopicAndReplies;
    private HtmlUtils.Callback mCallback;
    private OnHeaderClickListener mOnHeaderClickListener;

    @Inject
    public TopicDetailAdapter(Context context) {
        this.mContext = context;
        mTopicAndReplies = new TopicAndReplies();
        mTopicAndReplies.replies = new ArrayList<>();
    }

    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.mOnHeaderClickListener = onHeaderClickListener;
    }

    public void setContentCallBack(HtmlUtils.Callback callBack) {
        this.mCallback = callBack;
    }

    public TopicAndReplies getTopicAndReplies() {
        return mTopicAndReplies;
    }

    public TopicDetail getTopicContent() {
        return mTopicAndReplies.detail;
    }

    public void setTopicContent(TopicDetail content) {
        this.mTopicAndReplies.detail = content;
    }

    public void addReplies(List<TopicReply> replies) {
        mTopicAndReplies.replies.addAll(replies);
    }

    public List<TopicReply> getTopicReplies() {
        return mTopicAndReplies.replies;
    }

    @Override
    public int getItemViewType(int position) {
        if (mTopicAndReplies.detail != null && position == 0) {
            return TYPE_TOPIC_HEADER;
        } else if (mTopicAndReplies.replies.get(position - 1).deleted) {
            return TYPE_TOPIC_ITEM_DELETE;
        } else {
            return TYPE_TOPIC_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOPIC_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_topic_reply_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_TOPIC_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_topic_reply, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_topic_reply_deleted, parent, false);
            return new DeletedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_TOPIC_HEADER) {
            bindHeaderViewHolder((HeaderViewHolder) holder, mTopicAndReplies.detail);
        } else if (getItemViewType(position) == TYPE_TOPIC_ITEM) {
            TopicReply reply = mTopicAndReplies.replies.get(position - 1);
            bindItemViewHolder((ViewHolder) holder, reply, position);
        } else if (getItemViewType(position) == TYPE_TOPIC_ITEM_DELETE) {
            ((DeletedViewHolder) holder).deletedFloor.setText(position + "楼 Deleted");
            Paint paint = ((DeletedViewHolder) holder).deletedFloor.getPaint();
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            paint.setAntiAlias(true);
        }
    }

    private void bindItemViewHolder(ViewHolder holder, TopicReply reply, int position) {
        Glide.with(mContext).load(reply.user.avatarUrl.replace("large_avatar", "avatar"))
                .into(holder.avatar);
        holder.name.setText(reply.user.login);
        holder.floor.setText(
                MessageFormat.format(mContext.getString(R.string.comment_floor), position));
        if (reply.updatedAt != null) {
            holder.date.setText(DateUtils.computePastTime(mContext, reply.updatedAt));
        } else if (reply.createdAt != null) {
            holder.date.setText(DateUtils.computePastTime(mContext, reply.createdAt));
        }

        holder.avatar.setOnClickListener(v -> {
            if (mOnHeaderClickListener != null) {
                mOnHeaderClickListener.clickHead(reply.user.login);
            }
        });
        int max = DensityUtil.getScreenWidth(mContext) - holder.itemContent.getPaddingLeft()
                - holder.itemContent.getPaddingRight();
        HtmlUtils.parseHtmlAndSetText(reply.bodyHtml, holder.body, mCallback, max);
    }

    private void bindHeaderViewHolder(HeaderViewHolder holder, TopicDetail detail) {
        Glide.with(mContext).load(detail.user.avatarUrl.replace("large_avatar", "avatar"))
                .into(holder.avatar);
        holder.avatar.setOnClickListener(v -> {
            if (mOnHeaderClickListener != null) {
                mOnHeaderClickListener.clickHead(detail.user.login);
            }
        });
        holder.name.setText(detail.user.login);
        holder.node.setText(detail.nodeName);
        if (detail.repliedAt != null) {
            holder.date.setText(DateUtils.computePastTime(mContext, detail.repliedAt));
        } else if (detail.createdAt != null) {
            holder.date.setText(DateUtils.computePastTime(mContext, detail.createdAt));
        }
        holder.title.setText(detail.title);

        int max = DensityUtil.getScreenWidth(mContext) - holder.detailContent.getPaddingLeft()
                - holder.detailContent.getPaddingRight();
        HtmlUtils.parseHtmlAndSetText(detail.bodyHtml, holder.body, mCallback, max);

        holder.replyCount.setVisibility(detail.repliesCount == 0 ? View.GONE : View.VISIBLE);
        holder.replyCount.setText(MessageFormat
                .format(mContext.getString(R.string.reply_count), detail.repliesCount));

        holder.thumbUp.setImageResource(
                detail.liked ? R.drawable.ic_thumb_up_selected : R.drawable.ic_thumb_up_normal);
        holder.favorite.setImageResource(
                detail.favorited ? R.drawable.ic_favorite_selected : R.drawable.ic_favorite_normal);
    }

    @Override
    public int getItemCount() {
        if (mTopicAndReplies == null) {
            return 0;
        } else if (mTopicAndReplies.detail == null) {
            return mTopicAndReplies.replies == null ? 0 : mTopicAndReplies.replies.size();
        } else {
            return mTopicAndReplies.replies == null ? 1 : 1 + mTopicAndReplies.replies.size();
        }
    }

    public void clear() {

    }

    public interface OnHeaderClickListener {

        void clickHead(String user);
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.detail_content)
        RelativeLayout detailContent;
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.split_point)
        View splitPoint;
        @BindView(R.id.node)
        TextView node;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.item_header)
        RelativeLayout itemHeader;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.body)
        TextView body;
        @BindView(R.id.favorite)
        ImageView favorite;
        @BindView(R.id.thumb_up)
        ImageView thumbUp;
        @BindView(R.id.reply_count)
        TextView replyCount;

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_content)
        View itemContent;
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.name_group)
        RelativeLayout nameGroup;
        @BindView(R.id.floor)
        TextView floor;
        @BindView(R.id.split_point_small)
        View splitPointSmall;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.item_header)
        RelativeLayout itemHeader;
        @BindView(R.id.body)
        TextView body;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class DeletedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.deleted_floor)
        TextView deletedFloor;

        public DeletedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
