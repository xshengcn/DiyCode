package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.news.News;
import com.xshengcn.diycode.data.model.news.NewsReply;
import com.xshengcn.diycode.ui.adapter.NewsDetailAdapter;
import com.xshengcn.diycode.ui.iview.INewsDetailView;
import com.xshengcn.diycode.ui.presenter.NewsDetailPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailActivity extends BaseActivity implements INewsDetailView {

    private static final String EXTRA_NEWS = "NewsDetailActivity.mNews";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.recycler_View)
    RecyclerView recyclerView;

    @Inject
    NewsDetailPresenter presenter;
    @Inject
    NewsDetailAdapter adapter;

    private News mNews;

    public static void start(Activity activity, News news) {
        Intent intent = new Intent(activity, NewsDetailActivity.class);
        intent.putExtra(EXTRA_NEWS, news);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_recycler);
        getComponent().inject(this);
        ButterKnife.bind(this);

        if (savedInstanceState == null) mNews = getIntent().getParcelableExtra(EXTRA_NEWS);

        toolbar.setTitle(mNews.title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter.addNews(mNews);
        recyclerView.setAdapter(adapter);

        presenter.onAttach(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getNewsId() {
        return mNews.id;
    }

    @Override
    public void showReplies(List<NewsReply> replies) {
        adapter.addReplies(replies);
        adapter.notifyDataSetChanged();
    }
}
