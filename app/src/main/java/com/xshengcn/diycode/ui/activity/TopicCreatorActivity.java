package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.entity.topic.TopicNode;
import com.xshengcn.diycode.entity.topic.TopicNodeCategory;
import com.xshengcn.diycode.ui.iview.ITopicCreatorView;
import com.xshengcn.diycode.ui.presenter.TopicCreatorPresenter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class TopicCreatorActivity extends BaseActivity implements ITopicCreatorView {

  @BindView(R.id.topic_node_category) AppCompatSpinner topicNodeCategory;
  @BindView(R.id.topic_node) AppCompatSpinner topicNode;
  @BindView(R.id.title) EditText title;
  @BindView(R.id.body) EditText body;
  @BindView(R.id.image) ImageView image;
  @BindView(R.id.code) ImageView code;
  @BindView(R.id.link) ImageView link;
  @BindView(R.id.pre_view) ImageView preView;
  @BindView(R.id.edit_toolbar) LinearLayout editToolbar;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;

  @Inject TopicCreatorPresenter presenter;

  private boolean menuEnable = false;
  private ProgressDialog dialog;
  private Map<TopicNodeCategory, List<TopicNode>> nodeMap;
  private ArrayAdapter<TopicNodeCategory> categoryAdapter;
  private ArrayAdapter<TopicNode> nodeAdapter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_topic_creator);
    ButterKnife.bind(this);
    getComponent().inject(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    categoryAdapter = new ArrayAdapter<>(this, R.layout.view_item_node, new ArrayList<>());
    nodeAdapter = new ArrayAdapter<>(this, R.layout.view_item_node, new ArrayList<>());
    topicNodeCategory.setAdapter(categoryAdapter);
    topicNode.setAdapter(nodeAdapter);

    topicNodeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Logger.d(position);
        TopicNodeCategory category = categoryAdapter.getItem(position);
        if (category != null && nodeMap.containsKey(category)) {
          topicNode.setSelection(0);
          nodeAdapter.clear();
          nodeAdapter.addAll(nodeMap.get(category));
        }
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    presenter.onAttach(this);
  }

  @OnTextChanged({ R.id.title, R.id.body }) void onEditTextTextChanged(CharSequence sequence) {
    boolean enable = TextUtils.getTrimmedLength(title.getText()) > 0
        && TextUtils.getTrimmedLength(body.getText()) > 0;
    if (menuEnable != enable) {
      menuEnable = enable;
      invalidateOptionsMenu();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_send, menu);
    MenuItem send = menu.findItem(R.id.action_send);
    send.setEnabled(menuEnable);
    if (menuEnable) {
      send.getIcon().setAlpha(255);
    } else {
      send.getIcon().setAlpha(55);
    }
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        break;
      case R.id.action_send:
        presenter.createTopic();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  public static void start(Activity activity) {
    Intent intent = new Intent(activity, TopicCreatorActivity.class);
    activity.startActivity(intent);
  }

  @Override public void showNodes(Map<TopicNodeCategory, List<TopicNode>> map) {
    nodeMap = map;
    categoryAdapter.addAll(map.keySet());
  }

  @Override public int getNodeId() {
    int position = topicNode.getSelectedItemPosition();
    if (nodeAdapter.getItem(position) != null) {
      return nodeAdapter.getItem(position).id;
    }
    return 0;
  }

  @Override public String getTopicTitle() {
    return title.getEditableText().toString();
  }

  @Override public String getTopicBody() {
    return body.getEditableText().toString();
  }

  @Override public void showProgressDialog() {
    if (dialog == null) {
      dialog = new ProgressDialog(this);
      dialog.setMessage("正在创建话题...");
    }
  }

  @Override public void hideProgressDialog() {
    if (dialog != null) {
      dialog.dismiss();
    }
  }
}
