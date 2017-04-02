package com.xshengcn.diycode.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
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
                        MessageFormat.format(mContext.getString(R.string.notification_topic_reply),
                                notification.actor.login,
                                notification.reply.topicTitle));
                HtmlUtils.parseHtmlAndSetText(notification.reply.bodyHtml, holder.body, null);
                break;
            case "Follow":
                holder.title.setText(notification.actor.login);
                holder.body.setText(R.string.notification_follow);
                break;
            case "Mention":
                holder.title.setText(MessageFormat
                        .format(mContext.getString(R.string.notification_mention),
                                notification.actor.login));
                if (notification.mention != null) {
                    HtmlUtils.parseHtmlAndSetText(notification.mention.bodyHtml, holder.body, null);
                } else {
                    holder.body.setText(R.string.notification_empty_mention);
                }
                break;
        }
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d(notification);
            }
        });
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
