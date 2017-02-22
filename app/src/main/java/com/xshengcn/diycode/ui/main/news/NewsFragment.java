package com.xshengcn.diycode.ui.main.news;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.api.DiyCodeClient;
import com.xshengcn.diycode.entity.news.News;
import com.xshengcn.diycode.ui.BaseFragment;
import com.xshengcn.diycode.ui.newsdetail.NewsDetailActivity;
import com.xshengcn.diycode.widget.itemdecoration.OffsetDecoration;
import javax.inject.Inject;

public class NewsFragment extends BaseFragment implements NewsAdapter.OnItemClickListener {

  @BindView(R.id.recycler_View) RecyclerView recyclerView;
  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindDimen(R.dimen.spacing_xsmall) int space;
  @Inject NewsAdapter adapter;
  @Inject DiyCodeClient client;

  public static NewsFragment newInstance() {

    Bundle args = new Bundle();

    NewsFragment fragment = new NewsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_common_recycler, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    recyclerView.setPadding(0, space, 0, space);
    recyclerView.addItemDecoration(new OffsetDecoration(space, space, 0, 0));
    recyclerView.setAdapter(adapter);
    adapter.setOnItemClickListener(this);

    //client.getAllNewses(0)
    //    .observeOn(AndroidSchedulers.mainThread())
    //    .subscribe(new Subscriber<List<News>>() {
    //      @Override public void onCompleted() {
    //
    //      }
    //
    //      @Override public void onError(Throwable e) {
    //
    //      }
    //
    //      @Override public void onNext(List<News> newses) {
    //        adapter.addNewses(newses);
    //      }
    //    });
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    getComponent().inject(this);
  }

  @Override public void clickItem(News news, int position) {
    NewsDetailActivity.start(getActivity(), news);
  }

  @Override public void clickThumbUp(News news, int position) {

  }

  @Override public void clickFavorite(News news, int position) {

  }
}
