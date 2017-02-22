package com.xshengcn.diycode.ui.usertopic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.entity.topic.Topic;
import com.xshengcn.diycode.ui.BaseActivity;
import com.xshengcn.diycode.ui.main.topic.TopicAdapter;
import com.xshengcn.diycode.widget.itemdecoration.OffsetDecoration;
import java.util.List;
import javax.inject.Inject;

public class UserTopicActivity extends BaseActivity implements IUserTopicView {

  private static final String EXTRA_USER_LOGIN = "UserTopicActivity.userLogin";
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;
  @BindView(R.id.recycler_View) RecyclerView recyclerView;

  @BindDimen(R.dimen.spacing_xsmall) int space;

  @Inject UserTopicPresenter presenter;
  @Inject TopicAdapter adapter;

  private String userLogin;

  public static void start(Activity activity) {
    activity.startActivity(new Intent(activity, UserTopicActivity.class));
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_common_recycler);
    ButterKnife.bind(this);
    getComponent().inject(this);

    userLogin = getIntent().getParcelableExtra(EXTRA_USER_LOGIN);

    recyclerView.setPadding(0, space, 0, space);
    recyclerView.addItemDecoration(new OffsetDecoration(space, space, 0, 0));

    recyclerView.setAdapter(adapter);

    presenter.onAttach(this);
  }

  @Override public String getUserLogin() {
    //return userLogin;
    return "jixiaohua";
  }

  @Override public void showTopics(List<Topic> topics) {
    adapter.addTopics(topics);
    adapter.notifyDataSetChanged();
  }

  @Override public int getLoadOffset() {
    return adapter.getItemCount();
  }
}
