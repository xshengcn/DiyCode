package com.xshengcn.diycode.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.topic.Topic;
import com.xshengcn.diycode.data.model.user.UserReply;
import com.xshengcn.diycode.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserReplyAdapter extends RecyclerView.Adapter<UserReplyAdapter.ViewHolder> {

    private final List<UserReply> mUserReplies;
    private final Context mContext;

    private OnItemClickListener mOnItemClickListener;

    @Inject
    public UserReplyAdapter(Context context) {
        this.mContext = context;
        mUserReplies = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_user_reply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserReply reply = mUserReplies.get(position);
        Glide.with(mContext).load(reply.user.avatarUrl).into(holder.avatar);
        holder.name.setText(reply.user.login);
        if (reply.updatedAt != null) {
            holder.date.setText(DateUtils.computePastTime(reply.updatedAt));
        } else if (reply.createdAt != null) {
            holder.date.setText(DateUtils.computePastTime(reply.createdAt));
        } else {
            holder.date.setText(DateUtils.computePastTime(new Date()));
        }

        holder.splitPoint.setVisibility(View.GONE);
        holder.node.setVisibility(View.GONE);

        holder.title.setText(reply.topicTitle);
        holder.body.setText(Html.fromHtml(reply.body));
    }

    @Override
    public int getItemCount() {
        return mUserReplies.size();
    }

    public void addTopics(List<UserReply> replies) {
        this.mUserReplies.addAll(replies);
    }

    public void clear() {
        mUserReplies.clear();
    }

    public interface OnItemClickListener {

        void clickItem(Topic topic, int position);

        void clickThumbUp(Topic topic, int position);

        void clickFavorite(Topic topic, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
