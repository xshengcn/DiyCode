package com.xshengcn.diycode.ui.presenter;

import android.content.Context;
import android.net.Uri;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.entity.ImageResult;
import com.xshengcn.diycode.ui.iview.IReplyView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.inject.Inject;

public class ReplyPresenter extends BasePresenter<IReplyView> {

  private final DiyCodeClient client;
  private final Context context;

  @Inject public ReplyPresenter(DiyCodeClient client, Context context) {
    this.client = client;
    this.context = context;
  }

  public void sendReply() {
    final IReplyView view = getView();
    client.sendReply(view.getId(), view.getBody()).subscribe(new Observer<Object>() {
      @Override public void onSubscribe(Disposable d) {

      }

      @Override public void onNext(Object o) {

      }

      @Override public void onError(Throwable e) {

      }

      @Override public void onComplete() {

      }
    });
  }

  public void uploadImage() {

  }

  public void handlerImagePick(Uri data) {
    final IReplyView view = getView();
    view.showUploadDialog();
    Disposable d = cacheImageFromContentResolver(data).flatMap(
        new Function<String, ObservableSource<ImageResult>>() {
          @Override public ObservableSource<ImageResult> apply(@NonNull String s) throws Exception {
            return client.uploadPhoto(s);
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnComplete(view::hideUploadDialog)
        .subscribe(this::handleImageUpload, this::handleImageUploadError);

    addDisposable(d);
  }

  public void cancelUploadImage() {
    getDisposables().clear();
  }

  private void handleImageUpload(ImageResult result) {
    getView().insertImage(String.format("![](%s)", result.imageUrl));
  }

  private void handleImageUploadError(Throwable throwable) {
    getView().showUploadImageFailed();
    getView().hideUploadDialog();
  }

  public Observable<String> cacheImageFromContentResolver(Uri data) {
    return Observable.just(data).flatMap(new Function<Uri, ObservableSource<String>>() {
      @Override public ObservableSource<String> apply(@NonNull Uri uri) throws Exception {
        InputStream is = context.getContentResolver().openInputStream(uri);

        File bkFile = new File(
            context.getCacheDir().getPath() + File.separator + System.currentTimeMillis() + ".png");
        if (!bkFile.exists()) {
          bkFile.createNewFile();
          FileOutputStream out = new FileOutputStream(bkFile);
          byte[] b = new byte[1024 * 4]; // 5KB
          int len;
          while ((len = is.read(b)) != -1) {
            out.write(b, 0, len);
          }
          out.flush();
          is.close();
          out.close();
        }
        return Observable.just(bkFile.getAbsolutePath());
      }
    });
  }
}
