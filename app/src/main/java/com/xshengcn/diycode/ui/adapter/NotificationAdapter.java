package com.xshengcn.diycode.ui.adapter;

import android.content.Context;
import android.support.annotation.ColorInt;
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

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_HEADER = 0;
    private static final int ITEM_NOTIFICATION = 1;
    private final List<Notification> mNotifications;
    private final Context mContext;
    @ColorInt
    private final int mColorRead;
    @ColorInt
    private final int mColorUnread;

    private OnItemClickListener mOnItemClickListener;

    @Inject
    public NotificationAdapter(Context context) {
        this.mContext = context;
        mNotifications = new ArrayList<>();
        mColorRead = context.getResources().getColor(R.color.colorTextQuaternary);
        mColorUnread = context.getResources().getColor(R.color.colorTextPrimary);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_HEADER;
        } else {
            return ITEM_NOTIFICATION;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_notification_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_notification, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Notification notification = mNotifications.get(position - 1);
            bindNotification((ViewHolder) holder, notification);
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.unreadCount.setText(MessageFormat.format(mContext.getString(
                    R.string.unread_count), 1));
            headerViewHolder.cleanUnread.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.clickClearUnread();
                }
            });
        }

    }

    private void bindNotification(ViewHolder holder, Notification notification) {

        Glide.with(mContext)
                .load(notification.actor.avatarUrl.replace("large_avatar", "avatar"))
                .centerCrop()
                .into(holder.avatar);
        switch (notification.type) {
            case "TopicReply":
                holder.title.setText(
                        MessageFormat.format(mContext.getString(R.string.notification_topic_reply),
                                notification.actor.login,
                                notification.reply.topicTitle));
                holder.body.setText(
                        HtmlUtils.getSimpleHtmlText(mContext, notification.reply.bodyHtml));
                holder.itemContent.setOnClickListener(v -> {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.clickTopicNotification(notification.reply.id);
                    }
                });
                break;
            case "Follow":
                holder.title.setText(notification.actor.login);
                holder.body.setText(R.string.notification_follow);
                holder.itemContent.setOnClickListener(v -> {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.clickUserNotification(notification.actor.login);
                    }
                });
                break;
            case "Mention":
                holder.title.setText(MessageFormat
                        .format(mContext.getString(R.string.notification_mention),
                                notification.actor.login));
                if (notification.mention != null) {
                    holder.body.setText(
                            HtmlUtils.getSimpleHtmlText(mContext, notification.mention.bodyHtml));
                } else {
                    holder.body.setText(R.string.notification_empty_mention);
                }
                holder.itemContent.setOnClickListener(v -> {
                    if (mOnItemClickListener != null && notification.mention != null) {
                        mOnItemClickListener
                                .clickTopicNotification(notification.mention.topicId);
                    }
                });
                break;
        }

        holder.body.setTextColor(notification.read ? mColorRead : mColorUnread);
    }

    @Override
    public int getItemCount() {
        return mNotifications.size() == 0 ? 0 : mNotifications.size() + 1;
    }

    public void addTopics(List<Notification> notifications) {
        this.mNotifications.addAll(notifications);
    }

    public void clear() {
        mNotifications.clear();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_content)
        View itemContent;
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

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.unread_count)
        TextView unreadCount;
        @BindView(R.id.clean_unread)
        ImageView cleanUnread;

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {

        void clickClearUnread();

        void clickTopicNotification(int id);

        void clickUserNotification(String login);

        void clickNewsNotification(int id);
    }
}
