package com.xshengcn.diycode.ui.topicdetail;

import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.entity.topic.TopicAndReplies;
import com.xshengcn.diycode.entity.topic.TopicContent;
import com.xshengcn.diycode.entity.topic.TopicReply;
import com.xshengcn.diycode.ui.BasePresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.List;
import javax.inject.Inject;
import org.reactivestreams.Subscriber;

public class TopicDetailPresenter extends BasePresenter<ITopicDetailView> {

  private final DiyCodeClient client;

  @Inject public TopicDetailPresenter(DiyCodeClient client) {
    this.client = client;
  }

  @Override public void onAttach(ITopicDetailView view) {
    super.onAttach(view);
    loadTopicWithReplies();
  }

  private void loadTopicWithReplies() {
    Observable<TopicContent> detailObservable = client.getTopicDetail(getView().getTopicId());
    Observable<List<TopicReply>> reliesObservable =
        client.getTopicReplies(getView().getTopicId(), 0);

    Disposable disposable = Observable.zip(detailObservable, reliesObservable,
        (topicDetail, topicReplies) -> new TopicAndReplies(topicDetail, topicReplies))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(topicAndReplies -> getView().showTopicAndReplies(topicAndReplies), throwable -> {});
    addDisposable(disposable);
  }

  public void loadMoreReplies() {
    Disposable disposable =
        client.getTopicReplies(getView().getTopicId(), getView().getReplyOffset())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(replies -> getView().addReplies(replies),
                throwable -> {});
    addDisposable(disposable);
  }
}
