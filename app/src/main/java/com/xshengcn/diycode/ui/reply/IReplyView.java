package com.xshengcn.diycode.ui.reply;

public interface IReplyView {
  void insertImage(String format);

  void showUploadDialog();

  void hideUploadDialog();

  void showUploadImageFailed();

  int getId();

  String getBody();
}
