package com.xshengcn.diycode.ui.iview;

import com.xshengcn.diycode.data.model.ImageResult;

public interface ITopicReplyView {

    void showUploadDialog();

    void hideUploadDialog();

    void showUploadImageFailed();

    int getId();

    String getBody();

    void insertImage(ImageResult result);

    void showCommentDialog();

    void hideCommentDialog();

    void closeActivity();

    void showCommentFailed();
}
