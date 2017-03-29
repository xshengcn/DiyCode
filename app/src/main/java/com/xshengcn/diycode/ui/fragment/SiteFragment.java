package com.xshengcn.diycode.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.data.model.site.SiteListItem;
import com.xshengcn.diycode.ui.adapter.SiteAdapter;
import com.xshengcn.diycode.ui.iview.ISiteView;
import com.xshengcn.diycode.ui.presenter.SitePresenter;
import com.xshengcn.diycode.util.BrowserUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SiteFragment extends BaseFragment implements ISiteView {

    @BindView(R.id.recycler_View)
    RecyclerView mRecyclerView;
    @BindView(R.id.state_view)
    MultiStateView mStateView;

    @Inject
    SitePresenter presenter;
    @Inject
    SiteAdapter adapter;

    public static SiteFragment newInstance() {
        Bundle args = new Bundle();
        SiteFragment fragment = new SiteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_site, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(url -> BrowserUtil.openUrl(getActivity(), url));
        View errorState = mStateView.getView(MultiStateView.VIEW_STATE_ERROR);
        if (errorState != null) {
            ButterKnife.findById(errorState, R.id.no_connection_retry).setOnClickListener(
                    v -> presenter.loadSite());
        }
        presenter.onAttach(this);
        presenter.loadSite();
    }

    @Override
    public void showSites(List<SiteListItem> siteListItems) {
        mStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        adapter.addSiteListItems(siteListItems);
        Logger.d(siteListItems);
    }

    @Override
    public void showLoading() {
        mStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void showLoadSiteError() {
        mStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }
}
