package com.xshengcn.diycode.ui.main.topic;

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
import com.xshengcn.diycode.entity.topic.Topic;
import com.xshengcn.diycode.util.DateUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

  private final List<Topic> topics;
  private final Context context;

  private OnItemClickListener onItemClickListener;

  @Inject public TopicAdapter(Context context) {
    this.context = context;
    topics = new ArrayList<>();
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_topic, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Topic topic = topics.get(position);

    Glide.with(context).load(topic.user.avatarUrl).into(holder.avatar);
    holder.name.setText(topic.user.login);
    holder.node.setText(topic.nodeName);
    holder.title.setText(topic.title);
    if (topic.repliedAt != null) {
      holder.date.setText(DateUtils.computePastTime(topic.repliedAt));
    } else if (topic.createdAt != null) {
      holder.date.setText(DateUtils.computePastTime(topic.createdAt));
    }

    holder.itemView.setOnClickListener(v -> {
      if (onItemClickListener != null) onItemClickListener.clickItem(topic, position);
    });

    holder.thumbUp.setOnClickListener(v -> {
      if (onItemClickListener != null) onItemClickListener.clickThumbUp(topic, position);
    });

    holder.favorite.setOnClickListener(v -> {
      if (onItemClickListener != null) onItemClickListener.clickFavorite(topic, position);
    });
  }

  @Override public int getItemCount() {
    return topics.size();
  }

  public void addTopics(List<Topic> topics) {
    this.topics.addAll(topics);
  }

  public void clear() {
    topics.clear();
  }

  public interface OnItemClickListener {

    void clickItem(Topic topic, int position);

    void clickThumbUp(Topic topic, int position);

    void clickFavorite(Topic topic, int position);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.split_point) View splitPoint;
    @BindView(R.id.node) TextView node;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.item_header) RelativeLayout itemHeader;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.favorite) ImageView favorite;
    @BindView(R.id.thumb_up) ImageView thumbUp;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
