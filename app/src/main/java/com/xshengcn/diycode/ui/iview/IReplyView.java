package com.xshengcn.diycode.ui.iview;

import com.xshengcn.diycode.data.model.ImageResult;

public interface IReplyView {

    void showUploadDialog();

    void hideUploadDialog();

    void showUploadImageFailed();

    int getId();

    String getBody();

    void insertImage(ImageResult result);
}
