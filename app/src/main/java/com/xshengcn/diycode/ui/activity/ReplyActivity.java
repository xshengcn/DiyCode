package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import com.xshengcn.diycode.R;
import com.xshengcn.diycode.ui.iview.IReplyView;
import com.xshengcn.diycode.ui.presenter.ReplyPresenter;
import com.xshengcn.diycode.util.TextWatcherAdapter;
import javax.inject.Inject;

public class ReplyActivity extends BaseActivity
    implements IReplyView, PopupMenu.OnMenuItemClickListener {
  private static final String EXTRA_ID = "ReplyActivity.id";
  private static final String EXTRA_TITLE = "ReplyActivity.title";
  private static final int PICK_IMAGE = 0;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;
  @BindView(R.id.title) TextView titleView;
  @BindView(R.id.edit_text) EditText editText;
  @BindView(R.id.image) ImageView image;
  @BindView(R.id.pre_view) ImageView preView;
  @BindView(R.id.code) ImageView code;
  @BindView(R.id.link) ImageView link;

  @BindView(R.id.edit_link_title_wrapper) @Nullable TextInputLayout linkTitleWrapper;
  @BindView(R.id.edit_link_wrapper) @Nullable TextInputLayout linkWrapper;
  @BindView(R.id.edit_link_title) @Nullable EditText editLinkTitle;
  @BindView(R.id.edit_link) @Nullable EditText editLink;

  @BindString(R.string.link_can_not_be_empty) String linkNotEmpty;
  @BindString(R.string.link_title_can_not_be_empty) String linkTitleNotEmpty;

  @Inject ReplyPresenter presenter;

  private AlertDialog linkDialog;
  private View dialogView;
  private ProgressDialog uploadImageDialog;
  private PopupMenu codeCategoryPopup;

  private int id;
  private String title;

  private boolean menuEnable = false;

  public static void start(Activity activity, String title, int id) {
    Intent intent = new Intent(activity, ReplyActivity.class);
    intent.putExtra(EXTRA_TITLE, title);
    intent.putExtra(EXTRA_ID, id);
    activity.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reply);
    ButterKnife.bind(this);
    getComponent().inject(this);

    id = getIntent().getIntExtra(EXTRA_ID, -1);
    title = getIntent().getStringExtra(EXTRA_TITLE);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

    titleView.setText(title);
    presenter.onAttach(this);
  }

  @Override protected void onDestroy() {
    if (codeCategoryPopup != null) {
      codeCategoryPopup.dismiss();
    }
    super.onDestroy();
  }

  @OnClick(R.id.image) public void selectImage(View v) {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
  }

  @OnClick(R.id.code) public void showCodePopup(View v) {
    if (codeCategoryPopup == null) {
      codeCategoryPopup = new PopupMenu(this, v);
      codeCategoryPopup.setOnMenuItemClickListener(this);
      codeCategoryPopup.inflate(R.menu.menu_code_category_popup);
    }

    codeCategoryPopup.show();
  }

  @OnClick(R.id.link) public void showLinkDialog() {
    if (linkDialog == null) {
      dialogView = getLayoutInflater().inflate(R.layout.dialog_insert_link, null);
      linkTitleWrapper = ButterKnife.findById(dialogView, R.id.edit_link_title_wrapper);
      linkWrapper = ButterKnife.findById(dialogView, R.id.edit_link_wrapper);
      editLinkTitle = ButterKnife.findById(dialogView, R.id.edit_link_title);
      editLink = ButterKnife.findById(dialogView, R.id.edit_link);
      editLinkTitle.addTextChangedListener(new TextWatcherAdapter() {
        @Override public void afterTextChanged(Editable s) {
          if (TextUtils.getTrimmedLength(s) == 0) {
            linkTitleWrapper.setError(linkTitleNotEmpty);
          } else {
            linkTitleWrapper.setErrorEnabled(false);
          }
        }
      });

      editLink.addTextChangedListener(new TextWatcherAdapter() {
        @Override public void afterTextChanged(Editable s) {
          if (TextUtils.getTrimmedLength(s) == 0) {
            linkWrapper.setError(linkNotEmpty);
          } else {
            linkWrapper.setErrorEnabled(false);
          }
        }
      });
      linkDialog = new AlertDialog.Builder(this).setTitle(R.string.insert_link)
          .setView(dialogView)
          .setNegativeButton(android.R.string.cancel, null)
          .setPositiveButton(android.R.string.ok, null)
          .create();
      linkDialog.setOnShowListener(dialog -> {
        Button positive = linkDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positive.setOnClickListener(ReplyActivity.this::insertLink);
      });
    }

    editLinkTitle.getText().clear();
    editLink.getText().clear();
    linkTitleWrapper.setErrorEnabled(false);
    linkWrapper.setErrorEnabled(false);
    editLinkTitle.requestFocus();
    editLinkTitle.requestFocusFromTouch();
    linkDialog.show();
  }

  private void insertLink(View view) {
    if (TextUtils.getTrimmedLength(editLinkTitle.getText()) == 0) {
      linkTitleWrapper.setError(linkTitleNotEmpty);
    } else if (TextUtils.getTrimmedLength(editLink.getText()) == 0) {
      linkWrapper.setError(linkNotEmpty);
    } else {
      linkDialog.dismiss();
      String linkHolder = "[%s](%s)";
      int index = editText.getSelectionStart();
      String insert = String.format(linkHolder, editLinkTitle.getText().toString().trim(),
          editLink.getText().toString().trim());
      Editable editable = editText.getText();
      editable.insert(index, insert);
      editText.setSelection(index + insert.length());
    }
  }

  @OnClick(R.id.pre_view) public void preView(View v) {
    MarkdownPreviewActivity.start(ReplyActivity.this, editText.getText().toString());
  }

  @OnTextChanged(R.id.edit_text)
  void onEditTextTextChanged(CharSequence sequence) {
    boolean enable = TextUtils.getTrimmedLength(sequence) > 0;
    if (menuEnable != enable) {
      menuEnable = enable;
      invalidateOptionsMenu();
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case PICK_IMAGE:
        if (resultCode == RESULT_OK && data != null) {
          presenter.handlerImagePick(data.getData());
        }
        break;
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_send, menu);
    MenuItem send =  menu.findItem(R.id.action_send);
    send.setEnabled(menuEnable);
    if (menuEnable) {
      send.getIcon().setAlpha(255);
    }else {
      send.getIcon().setAlpha(55);
    }

    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        super.onBackPressed();
        break;
      case R.id.action_send:
        presenter.sendReply();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void insertImage(String format) {
    editText.append(format);
  }

  @Override public void showUploadDialog() {
    if (uploadImageDialog == null) {
      uploadImageDialog = new ProgressDialog(this);
      uploadImageDialog.setCanceledOnTouchOutside(false);
      uploadImageDialog.setMessage("正在上传图片");
    }
    uploadImageDialog.show();
  }

  @Override public void hideUploadDialog() {
    uploadImageDialog.dismiss();
  }

  @Override public void showUploadImageFailed() {
    Toast.makeText(this, "上传图片失败", Toast.LENGTH_SHORT).show();
  }

  @Override public int getId() {
    return id;
  }

  @Override public String getBody() {
    return editText.getText().toString().trim();
  }

  String codeHolder = "\n```%s\n\n```\n";

  @Override public boolean onMenuItemClick(MenuItem item) {
    String s;
    switch (item.getItemId()) {
      case R.id.action_ruby:
        s = "ruby";
        break;
      case R.id.action_html_erb:
        s = "erb";
        break;
      case R.id.action_css_scss:
        s = "scss";
        break;
      case R.id.action_javascript:
        s = "js";
        break;
      case R.id.action_yaml:
        s = "yml";
        break;
      case R.id.action_coffeescript:
        s = "coffee";
        break;
      case R.id.action_nginx_redis:
        s = "conf";
        break;
      case R.id.action_python:
        s = "python";
        break;
      case R.id.action_php:
        s = "php";
        break;
      case R.id.action_java:
        s = "java";
        break;
      case R.id.action_erlang:
        s = "erlang";
        break;
      case R.id.action_shell_bash:
        s = "shell";
        break;
      default:
        s = "java";
        break;
    }
    // 随便定位一下光标， 懒
    int index = editText.getSelectionStart();
    Editable editable = editText.getText();
    editable.insert(index, String.format(codeHolder, s));
    editText.setSelection(index + s.length() + 5);
    //ImeUtils.showIme(editText);
    return false;
  }
}
