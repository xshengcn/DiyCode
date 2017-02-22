package com.xshengcn.diycode.injector.component;

import com.xshengcn.diycode.injector.PreFragment;
import com.xshengcn.diycode.injector.module.FragmentModule;
import com.xshengcn.diycode.ui.main.news.NewsFragment;
import com.xshengcn.diycode.ui.main.topic.TopicFragment;
import dagger.Subcomponent;

@PreFragment @Subcomponent(modules = FragmentModule.class) public interface FragmentComponent {

  void inject(NewsFragment newsFragment);

  void inject(TopicFragment topicFragment);
}
