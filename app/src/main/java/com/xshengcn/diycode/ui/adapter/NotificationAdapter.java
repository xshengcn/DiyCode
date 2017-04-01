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
import com.xshengcn.diycode.data.model.user.Notification;
import com.xshengcn.diycode.util.HtmlUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<Notification> mNotifications;
    private final Context mContext;

    @Inject
    public NotificationAdapter(Context context) {
        this.mContext = context;
        mNotifications = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notification notification = mNotifications.get(position);
        Glide.with(mContext)
                .load(notification.actor.avatarUrl.replace("large_avatar", "avatar"))
                .into(holder.avatar);
        switch (notification.type) {
            case "TopicReply":
                holder.title.setText(
                        MessageFormat.format("{0} 在帖子{1}回复了:", notification.actor.login,
                                notification.reply.topicTitle));
                HtmlUtils.parseHtmlAndSetText(notification.reply.bodyHtml, holder.body, null);
                break;
            case "Follow":
                holder.title.setText(notification.actor.login);
                holder.body.setText("开始关注你了。");
                break;
            case "Mention":
                holder.title.setText(notification.actor.login + " 提及你:");
                if (notification.mention != null) {
                    HtmlUtils.parseHtmlAndSetText(notification.mention.bodyHtml, holder.body, null);
                } else {
                    holder.body.setText("服务器返回了空的Mention。");
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public void addTopics(List<Notification> notifications) {
        this.mNotifications.addAll(notifications);
    }

    public void clear() {
        mNotifications.clear();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        ImageView avatar;
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
