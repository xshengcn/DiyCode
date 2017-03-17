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
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.model.topic.Topic;
import com.xshengcn.diycode.model.user.Notification;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

  private final List<Notification> notifications;
  private final Context context;

  private OnItemClickListener onItemClickListener;

  @Inject public NotificationAdapter(Context context) {
    this.context = context;
    notifications = new ArrayList<>();
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


  }

  @Override public int getItemCount() {
    return notifications.size();
  }

  public void addTopics(List<Notification> notifications) {
    this.notifications.addAll(notifications);
  }

  public void clear() {
    notifications.clear();
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
