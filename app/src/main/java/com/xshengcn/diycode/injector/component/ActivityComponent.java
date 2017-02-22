package com.xshengcn.diycode.injector.component;

import com.xshengcn.diycode.injector.PreActivity;
import com.xshengcn.diycode.injector.module.ActivityModule;
import com.xshengcn.diycode.injector.module.FragmentModule;
import com.xshengcn.diycode.ui.login.LoginActivity;
import com.xshengcn.diycode.ui.main.MainActivity;
import com.xshengcn.diycode.ui.newsdetail.NewsDetailActivity;
import com.xshengcn.diycode.ui.topicdetail.TopicDetailActivity;
import com.xshengcn.diycode.ui.userfavorite.usertopic.UserFavoriteActivity;
import com.xshengcn.diycode.ui.usertopic.UserTopicActivity;
import dagger.Subcomponent;

@PreActivity @Subcomponent(modules = ActivityModule.class) public interface ActivityComponent {

  void inject(MainActivity activity);

  void inject(NewsDetailActivity activity);

  void inject(LoginActivity activity);

  void inject(UserTopicActivity activity);

  void inject(UserFavoriteActivity activity);

  void inject(TopicDetailActivity activity);

  FragmentComponent plus(FragmentModule module);
}
