package com.xshengcn.diycode.injection.component;

import com.xshengcn.diycode.injection.PreFragment;
import com.xshengcn.diycode.injection.module.FragmentModule;
import com.xshengcn.diycode.ui.main.news.NewsFragment;
import com.xshengcn.diycode.ui.main.topic.TopicFragment;
import dagger.Subcomponent;

@PreFragment @Subcomponent(modules = FragmentModule.class) public interface FragmentComponent {

  void inject(NewsFragment newsFragment);

  void inject(TopicFragment topicFragment);
}
