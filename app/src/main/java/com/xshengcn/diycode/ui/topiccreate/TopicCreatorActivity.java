package com.xshengcn.diycode.ui.topiccreate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.ui.BaseActivity;

public class TopicCreatorActivity extends BaseActivity {

  @BindView(R.id.topic_node_category) AppCompatSpinner topicNodeCategory;
  @BindView(R.id.topic_node) AppCompatSpinner topicNode;
  @BindView(R.id.title) EditText title;
  @BindView(R.id.body) EditText body;
  @BindView(R.id.image) ImageView image;
  @BindView(R.id.code) ImageView code;
  @BindView(R.id.link) ImageView link;
  @BindView(R.id.pre_view) ImageView preView;
  @BindView(R.id.edit_toolbar) LinearLayout editToolbar;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_topic_creator);
    ButterKnife.bind(this);

    String[] datas = { "张三1", "张三2", "张三3", "张三4", "张三5" };
    ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
    topicNodeCategory.setAdapter(adapter);
    topicNode.setAdapter(adapter);

    body.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override public void onFocusChange(View v, boolean hasFocus) {
        //Toast.makeText(TopicCreatorActivity.this, String.valueOf(hasFocus), Toast.LENGTH_SHORT).show();
        editToolbar.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
      }
    });
  }

  public static void start(Activity activity) {
    Intent intent = new Intent(activity, TopicCreatorActivity.class);
    activity.startActivity(intent);
  }
}
