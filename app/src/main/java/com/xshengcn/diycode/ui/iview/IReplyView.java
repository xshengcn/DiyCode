package com.xshengcn.diycode.ui.iview;

public interface IReplyView {
  void insertImage(String format);

  void showUploadDialog();

  void hideUploadDialog();

  void showUploadImageFailed();

  int getId();

  String getBody();
}
