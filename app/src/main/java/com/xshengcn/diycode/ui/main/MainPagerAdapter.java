package com.xshengcn.diycode.ui.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.ui.main.news.NewsFragment;
import com.xshengcn.diycode.ui.main.topic.TopicFragment;
import javax.inject.Inject;

public class MainPagerAdapter extends FragmentPagerAdapter {

  private final String[] titles;

  @Inject public MainPagerAdapter(FragmentManager fm, Context context) {
    super(fm);
    titles = context.getResources().getStringArray(R.array.main_tabs);
  }

  @Override public Fragment getItem(int position) {
    if (position == 0) {
      return TopicFragment.newInstance();
    }
    return NewsFragment.newInstance();
  }

  @Override public int getCount() {
    return titles.length;
  }

  @Override public CharSequence getPageTitle(int position) {
    return titles[position];
  }
}
