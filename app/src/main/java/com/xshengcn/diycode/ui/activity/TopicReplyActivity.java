package com.xshengcn.diycode.ui.activity;

import android.annotation.SuppressLint;
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

import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.ImageResult;
import com.xshengcn.diycode.ui.ActivityNavigator;
import com.xshengcn.diycode.ui.iview.ITopicReplyView;
import com.xshengcn.diycode.ui.presenter.TopicReplyPresenter;
import com.xshengcn.diycode.util.MarkdownUtils;
import com.xshengcn.diycode.util.MarkdownUtils.CategoryCallback;
import com.xshengcn.diycode.util.TextWatcherAdapter;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class TopicReplyActivity extends BaseActivity
        implements ITopicReplyView, CategoryCallback {

    private static final String EXTRA_ID = "TopicCommentActivity.mId";
    private static final String EXTRA_TITLE = "TopicCommentActivity.title";
    private static final int PICK_IMAGE = 0;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.pre_view)
    ImageView preView;
    @BindView(R.id.code)
    ImageView code;
    @BindView(R.id.link)
    ImageView link;

    @BindView(R.id.edit_link_title_wrapper)
    @Nullable
    TextInputLayout linkTitleWrapper;
    @BindView(R.id.edit_link_wrapper)
    @Nullable
    TextInputLayout linkWrapper;
    @BindView(R.id.edit_link_title)
    @Nullable
    EditText editLinkTitle;
    @BindView(R.id.edit_link)
    @Nullable
    EditText editLink;

    @BindString(R.string.link_can_not_be_empty)
    String linkNotEmpty;
    @BindString(R.string.link_title_can_not_be_empty)
    String linkTitleNotEmpty;

    @Inject
    TopicReplyPresenter mPresenter;
    @Inject
    ActivityNavigator mNavigator;

    private AlertDialog mLinkDialog;
    private View mDialogView;
    private ProgressDialog mUploadImageDialog;
    private ProgressDialog mCommentDialog;
    private PopupMenu mCodeCategoryPopup;
    private int mId;
    private String mTitle;
    private boolean mMenuEnable = false;

    public static void start(Activity activity, String title, int id) {
        Intent intent = new Intent(activity, TopicReplyActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_ID, id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        ButterKnife.bind(this);
        getComponent().inject(this);

        mId = getIntent().getIntExtra(EXTRA_ID, -1);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        toolbar.setTitle(R.string.comment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        titleView.setText(mTitle);
        mPresenter.onAttach(this);
    }

    @Override
    protected void onDestroy() {
        if (mCodeCategoryPopup != null) {
            mCodeCategoryPopup.dismiss();
        }
        super.onDestroy();
    }

    @OnClick(R.id.image)
    public void selectImage(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @OnClick(R.id.code)
    public void showCodePopup(View v) {
        if (mCodeCategoryPopup == null) {
            mCodeCategoryPopup = MarkdownUtils.createCodePopupMenu(this, v, this);
        }
        mCodeCategoryPopup.show();
    }

    @Override
    public void clickCategory(String codeCategory) {
        MarkdownUtils.addCode(editText, codeCategory);
    }

    @SuppressLint("InflateParams")
    @OnClick(R.id.link)
    public void showLinkDialog() {
        if (mLinkDialog == null) {
            mDialogView = getLayoutInflater().inflate(R.layout.dialog_insert_link, null);
            linkTitleWrapper = ButterKnife.findById(mDialogView, R.id.edit_link_title_wrapper);
            linkWrapper = ButterKnife.findById(mDialogView, R.id.edit_link_wrapper);
            editLinkTitle = ButterKnife.findById(mDialogView, R.id.edit_link_title);
            editLink = ButterKnife.findById(mDialogView, R.id.edit_link);
            editLinkTitle.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.getTrimmedLength(s) == 0) {
                        linkTitleWrapper.setError(linkTitleNotEmpty);
                    } else {
                        linkTitleWrapper.setErrorEnabled(false);
                    }
                }
            });

            editLink.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.getTrimmedLength(s) == 0) {
                        linkWrapper.setError(linkNotEmpty);
                    } else {
                        linkWrapper.setErrorEnabled(false);
                    }
                }
            });
            mLinkDialog = new AlertDialog.Builder(this).setTitle(R.string.insert_link)
                    .setView(mDialogView)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
            mLinkDialog.setOnShowListener(dialog -> {
                Button positive = mLinkDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(TopicReplyActivity.this::insertLink);
            });
        }

        editLinkTitle.getText().clear();
        editLink.getText().clear();
        linkTitleWrapper.setErrorEnabled(false);
        linkWrapper.setErrorEnabled(false);
        editLinkTitle.requestFocus();
        editLinkTitle.requestFocusFromTouch();
        mLinkDialog.show();
    }

    private void insertLink(View view) {
        if (TextUtils.getTrimmedLength(editLinkTitle.getText()) == 0) {
            linkTitleWrapper.setError(linkTitleNotEmpty);
        } else if (TextUtils.getTrimmedLength(editLink.getText()) == 0) {
            linkWrapper.setError(linkNotEmpty);
        } else {
            mLinkDialog.dismiss();
            MarkdownUtils.addLink(editText, editLinkTitle.getText().toString().trim(),
                    editLink.getText().toString().trim());
        }
    }

    @OnClick(R.id.pre_view)
    public void clickPreView(View v) {
        mNavigator.showPreView(getBody());
    }

    @OnTextChanged(R.id.edit_text)
    void onEditTextTextChanged(CharSequence sequence) {
        boolean enable = TextUtils.getTrimmedLength(sequence) > 0;
        if (mMenuEnable != enable) {
            mMenuEnable = enable;
            invalidateOptionsMenu();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK && data != null) {
                    mPresenter.handlerImagePick(data.getData());
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        MenuItem send = menu.findItem(R.id.action_send);
        send.setEnabled(mMenuEnable);
        if (mMenuEnable) {
            send.getIcon().setAlpha(255);
        } else {
            send.getIcon().setAlpha(55);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.action_send:
                mPresenter.publishComment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showUploadDialog() {
        if (mUploadImageDialog == null) {
            mUploadImageDialog = new ProgressDialog(this);
            mUploadImageDialog.setCanceledOnTouchOutside(false);
            mUploadImageDialog.setMessage("正在上传图片");
            mUploadImageDialog.setOnCancelListener(dialog -> mPresenter.cancelUploadImage());
        }
        mUploadImageDialog.show();
    }

    @Override
    public void hideUploadDialog() {
        if (mUploadImageDialog != null) {
            mUploadImageDialog.dismiss();
        }
    }

    @Override
    public void showUploadImageFailed() {
        Toast.makeText(this, "上传图片失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public String getBody() {
        return editText.getText().toString().trim();
    }

    @Override
    public void insertImage(ImageResult result) {
        MarkdownUtils.addImage(editText, "", result.imageUrl);
    }

    @Override
    public void showCommentDialog() {
        if (mCommentDialog == null) {
            mCommentDialog = new ProgressDialog(this);
            mCommentDialog.setCanceledOnTouchOutside(false);
            mCommentDialog.setMessage("正在发表评论");
            mCommentDialog.setOnCancelListener(dialog -> mPresenter.cancelComment());
        }
        mCommentDialog.show();
    }

    @Override
    public void hideCommentDialog() {
        if (mCommentDialog != null) {
            mCommentDialog.dismiss();
        }
    }

    @Override
    public void closeActivity() {
        this.finish();
    }

    @Override
    public void showCommentFailed() {
        Toast.makeText(this, "发表评论失败", Toast.LENGTH_SHORT).show();
    }
}
