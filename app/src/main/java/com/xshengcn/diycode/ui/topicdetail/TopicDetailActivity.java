package com.xshengcn.diycode.ui.topicdetail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.customtabs.CustomTabActivityHelper;
import com.xshengcn.diycode.entity.topic.Topic;
import com.xshengcn.diycode.entity.topic.TopicAndReplies;
import com.xshengcn.diycode.entity.topic.TopicReply;
import com.xshengcn.diycode.ui.BaseActivity;
import com.xshengcn.diycode.ui.photo.PhotoViewerActivity;
import com.xshengcn.diycode.util.HtmlUtils;
import com.xshengcn.diycode.widget.itemdecoration.InsetDividerDecoration;
import com.xshengcn.diycode.widget.recyclerview.LoadMoreHandler;
import com.xshengcn.diycode.widget.recyclerview.LoadMoreWrapper;
import java.util.List;
import javax.inject.Inject;

public class TopicDetailActivity extends BaseActivity
    implements ITopicDetailView, HtmlUtils.ClickCallback, LoadMoreHandler {

  private static final String EXTRA_TOPIC = "TopicDetailActivity.topic";
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;
  @BindView(R.id.recycler_View) RecyclerView recyclerView;
  @BindColor(R.color.colorDivider) int colorDivider;
  @BindDimen(R.dimen.divider) int divider;

  @Inject TopicDetailPresenter presenter;
  @Inject TopicDetailAdapter adapter;
  @BindColor(android.R.color.white) int color;
  private LoadMoreWrapper wrapper;
  private Topic topic;

  public static void start(Activity activity, Topic topic) {
    Intent intent = new Intent(activity, TopicDetailActivity.class);
    intent.putExtra(EXTRA_TOPIC, topic);
    activity.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_common_recycler);
    ButterKnife.bind(this);
    getComponent().inject(this);

    topic = getIntent().getParcelableExtra(EXTRA_TOPIC);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    recyclerView.addItemDecoration(
        new InsetDividerDecoration(TopicDetailAdapter.ViewHolder.class, divider, 0, colorDivider));
    adapter.setContentCallBack(this);
    wrapper = new LoadMoreWrapper(this, adapter);
    recyclerView.setAdapter(wrapper);

    presenter.onAttach(this);
  }

  @Override public int getTopicId() {
    return topic.id;
  }

  @Override public void showTopicAndReplies(TopicAndReplies topicAndReplies) {
    int start = adapter.getItemCount();
    adapter.setTopicContent(topicAndReplies.content);
    adapter.addReplies(topicAndReplies.replies);
    wrapper.notifyItemRangeInserted(start, adapter.getItemCount());

    if (adapter.getTopicAndReplies().replies.size()
        == adapter.getTopicAndReplies().content.repliesCount) {
      wrapper.showNoMore();
    }
  }

  @Override public int getReplyOffset() {
    return adapter.getTopicAndReplies().replies.size();
  }

  @Override public void addReplies(List<TopicReply> topicReplies) {
    wrapper.hideFooter();
    int start = adapter.getItemCount();
    adapter.addReplies(topicReplies);
    wrapper.notifyItemRangeInserted(start, topicReplies.size());

    if (adapter.getTopicAndReplies().replies.size()
        == adapter.getTopicAndReplies().content.repliesCount) {
      wrapper.showNoMore();
    }
  }

  @Override public void clickUrl(String url) {
    if (url.startsWith("#reply")) {
      String floorStr = url.substring(6);
      try {
        int floor = Integer.parseInt(floorStr);
        recyclerView.smoothScrollToPosition(floor);
      } catch (NumberFormatException e) {

      }
    } else if (url.startsWith("/")) {

    } else {
      CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
      builder.setToolbarColor(color);
      builder.setShowTitle(true);

      CustomTabActivityHelper.openCustomTab(this, builder.build(), Uri.parse(url),
          (activity, uri) -> openUri(activity, uri));
    }
  }

  private void openUri(Activity activity, Uri uri) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(uri);
    activity.startActivity(intent);
  }

  @Override public void clickImage(String source) {
    PhotoViewerActivity.start(this, source);
  }

  @Override public boolean canLoadMore() {
    TopicAndReplies details = adapter.getTopicAndReplies();
    return details.content.repliesCount > details.replies.size();
  }

  @Override public void loadMore() {
    presenter.loadMoreReplies();
  }
}
