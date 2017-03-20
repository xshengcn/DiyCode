package com.xshengcn.diycode.ui.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;

import com.orhanobut.logger.Logger;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.util.ImeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, SearchActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.onActionViewExpanded();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        ImeUtils.showImeForced(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Logger.d("onQueryTextSubmit: %s", query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        ImeUtils.hideIme(searchView);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ImeUtils.hideIme(searchView);
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
