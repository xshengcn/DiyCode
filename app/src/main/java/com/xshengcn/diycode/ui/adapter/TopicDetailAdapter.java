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
import com.xshengcn.diycode.entity.topic.TopicAndReplies;
import com.xshengcn.diycode.entity.topic.TopicContent;
import com.xshengcn.diycode.entity.topic.TopicReply;
import com.xshengcn.diycode.util.DateUtils;
import com.xshengcn.diycode.util.DensityUtil;
import com.xshengcn.diycode.util.HtmlUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class TopicDetailAdapter extends RecyclerView.Adapter {

  private static final int TYPE_TOPIC_HEADER = 0x01;
  private static final int TYPE_TOPIC_ITEM = 0x02;
  private final Context context;
  private final int imgMaxWidth;
  private final TopicAndReplies topicAndReplies;
  private HtmlUtils.ClickCallback callBack;
  private OnHeaderClickListener onHeaderClickListener;

  @Inject public TopicDetailAdapter(Context context) {
    this.context = context;
    int margin = DensityUtil.dp2px(context, 16 * 2);
    imgMaxWidth = DensityUtil.getScreenWidth(context) - margin;
    topicAndReplies = new TopicAndReplies();
    topicAndReplies.replies = new ArrayList<>();
  }

  public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
    this.onHeaderClickListener = onHeaderClickListener;
  }

  public void setContentCallBack(HtmlUtils.ClickCallback callBack) {
    this.callBack = callBack;
  }

  public TopicAndReplies getTopicAndReplies() {
    return topicAndReplies;
  }

  public TopicContent getTopicContent() {
    return topicAndReplies.content;
  }

  public void setTopicContent(TopicContent content) {
    this.topicAndReplies.content = content;
  }

  public void addReplies(List<TopicReply> replies) {
    topicAndReplies.replies.addAll(replies);
  }

  public List<TopicReply> getTopicReplies() {
    return topicAndReplies.replies;
  }

  @Override public int getItemViewType(int position) {
    if (topicAndReplies.content != null && position == 0) {
      return TYPE_TOPIC_HEADER;
    } else {
      return TYPE_TOPIC_ITEM;
    }
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == TYPE_TOPIC_HEADER) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.view_item_topic_reply_header, parent, false);
      return new HeaderViewHolder(view);
    } else {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.view_item_topic_reply, parent, false);
      return new ViewHolder(view);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (getItemViewType(position) == TYPE_TOPIC_HEADER) {
      bindHeaderViewHolder((HeaderViewHolder) holder, topicAndReplies.content);
    } else if (getItemViewType(position) == TYPE_TOPIC_ITEM) {
      TopicReply reply =
          topicAndReplies.replies.get(topicAndReplies.content != null ? position - 1 : position);
      bindItemViewHolder((ViewHolder) holder, reply, position);
    }
  }

  private void bindItemViewHolder(ViewHolder holder, TopicReply reply, int position) {
    Glide.with(context).load(reply.user.avatarUrl).into(holder.avatar);
    holder.name.setText(reply.user.login);
    holder.node.setText("Android");
    holder.floor.setText(position + "楼");
    if (reply.updatedAt != null) {
      holder.date.setText(DateUtils.computePastTime(reply.updatedAt));
    } else if (reply.createdAt != null) {
      holder.date.setText(DateUtils.computePastTime(reply.createdAt));
    }

    holder.avatar.setOnClickListener(v -> {
      if (onHeaderClickListener != null) {
        onHeaderClickListener.clickHead(reply.user.login);
      }
    });

    HtmlUtils.parseHtmlAndSetText(context, reply.bodyHtml, holder.body, imgMaxWidth, callBack);
  }

  private void bindHeaderViewHolder(HeaderViewHolder holder, TopicContent detail) {
    Glide.with(context).load(detail.user.avatarUrl).into(holder.avatar);
    holder.avatar.setOnClickListener(v -> {
      if (onHeaderClickListener != null) {
        onHeaderClickListener.clickHead(detail.user.login);
      }
    });
    holder.name.setText(detail.user.login);
    holder.node.setText(detail.nodeName);
    if (detail.repliedAt != null) {
      holder.date.setText(DateUtils.computePastTime(detail.repliedAt));
    } else if (detail.createdAt != null) {
      holder.date.setText(DateUtils.computePastTime(detail.createdAt));
    }
    holder.title.setText(detail.title);

    HtmlUtils.parseHtmlAndSetText(context, detail.bodyHtml, holder.body, imgMaxWidth, callBack);

    holder.replyCount.setVisibility(detail.repliesCount == 0 ? View.GONE : View.VISIBLE);
    holder.replyCount.setText("共收到" + detail.repliesCount + "条回复");

    holder.thumbUp.setImageResource(
        detail.liked ? R.drawable.ic_thumb_up_selected : R.drawable.ic_thumb_up_normal);
    holder.favorite.setImageResource(
        detail.favorited ? R.drawable.ic_favorite_selected : R.drawable.ic_favorite_normal);
  }

  @Override public int getItemCount() {
    if (topicAndReplies == null) {
      return 0;
    } else if (topicAndReplies.content == null) {
      return topicAndReplies.replies == null ? 0 : topicAndReplies.replies.size();
    } else {
      return topicAndReplies.replies == null ? 1 : 1 + topicAndReplies.replies.size();
    }
  }

  public void clear() {

  }

  static class HeaderViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.detail_content) RelativeLayout detailContent;
    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.split_point) View splitPoint;
    @BindView(R.id.node) TextView node;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.item_header) RelativeLayout itemHeader;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.body) TextView body;
    @BindView(R.id.favorite) ImageView favorite;
    @BindView(R.id.thumb_up) ImageView thumbUp;
    @BindView(R.id.reply_count) TextView replyCount;

    HeaderViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.split_point) View splitPoint;
    @BindView(R.id.node) TextView node;
    @BindView(R.id.name_group) RelativeLayout nameGroup;
    @BindView(R.id.floor) TextView floor;
    @BindView(R.id.split_point_small) View splitPointSmall;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.item_header) RelativeLayout itemHeader;
    @BindView(R.id.body) TextView body;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public interface OnHeaderClickListener {
    void clickHead(String user);
  }
}
