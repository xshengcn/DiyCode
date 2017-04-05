package com.xshengcn.diycode.ui.presenter;

import android.content.Context;
import android.net.Uri;

import com.xshengcn.diycode.data.DataManager;
import com.xshengcn.diycode.data.event.TopicReplied;
import com.xshengcn.diycode.data.model.ImageResult;
import com.xshengcn.diycode.data.model.topic.TopicReply;
import com.xshengcn.diycode.ui.iview.ITopicReplyView;
import com.xshengcn.diycode.util.RxBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TopicReplyPresenter extends BasePresenter<ITopicReplyView> {

    private final DataManager mDataManager;
    private final Context mContext;
    private final RxBus mBus;

    @Inject
    public TopicReplyPresenter(DataManager dataManager, Context context, RxBus bus) {
        this.mDataManager = dataManager;
        this.mContext = context;
        mBus = bus;
    }

    public void publishComment() {
        final ITopicReplyView view = getView();
        view.showCommentDialog();
        mDataManager.publishComment(view.getId(), view.getBody())
                .doOnNext(new Consumer<TopicReply>() {
                    @Override
                    public void accept(@NonNull TopicReply topicReply) throws Exception {
                        mBus.send(new TopicReplied(topicReply));
                    }
                })
                .subscribe(this::commentSuccess, this::commentFailed);
    }

    private void commentSuccess(TopicReply topicReply) {
        final ITopicReplyView view = getView();
        view.hideCommentDialog();
        view.closeActivity();
    }

    private void commentFailed(Throwable throwable) {
        final ITopicReplyView view = getView();
        view.hideCommentDialog();
        view.showCommentFailed();

    }


    public void cancelComment() {
        getDisposable().clear();
    }


    public void handlerImagePick(Uri data) {
        final ITopicReplyView view = getView();
        view.showUploadDialog();
        Disposable d = cacheImageFromContentResolver(data).flatMap(
                new Function<String, ObservableSource<ImageResult>>() {
                    @Override
                    public ObservableSource<ImageResult> apply(@NonNull String s) throws Exception {
                        return mDataManager.uploadPhoto(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnComplete(view::hideUploadDialog)
                .subscribe(this::handleImageUpload, this::handleImageUploadError);

        addDisposable(d);
    }

    public void cancelUploadImage() {
        getDisposable().clear();
    }

    private void handleImageUpload(ImageResult result) {
        getView().insertImage(result);
    }

    private void handleImageUploadError(Throwable throwable) {
        getView().showUploadImageFailed();
        getView().hideUploadDialog();
    }

    public Observable<String> cacheImageFromContentResolver(Uri data) {
        return Observable.just(data).flatMap(new Function<Uri, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Uri uri) throws Exception {
                InputStream is = mContext.getContentResolver().openInputStream(uri);

                String path = mContext.getCacheDir().getPath()
                        + File.separator
                        + System.currentTimeMillis() + ".png";
                File bkFile = new File(path);
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
