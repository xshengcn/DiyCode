package com.xshengcn.diycode.injection.component;

import com.xshengcn.diycode.injection.PreActivity;
import com.xshengcn.diycode.injection.module.ActivityModule;
import com.xshengcn.diycode.injection.module.FragmentModule;
import com.xshengcn.diycode.ui.activity.LoginActivity;
import com.xshengcn.diycode.ui.activity.MainActivity;
import com.xshengcn.diycode.ui.activity.NewsDetailActivity;
import com.xshengcn.diycode.ui.activity.NotificationActivity;
import com.xshengcn.diycode.ui.activity.ReplyActivity;
import com.xshengcn.diycode.ui.activity.TopicCreatorActivity;
import com.xshengcn.diycode.ui.activity.TopicDetailActivity;
import com.xshengcn.diycode.ui.activity.UserFavoriteActivity;
import com.xshengcn.diycode.ui.activity.UserReplyActivity;
import com.xshengcn.diycode.ui.activity.UserTopicActivity;
import dagger.Subcomponent;

@PreActivity @Subcomponent(modules = ActivityModule.class) public interface ActivityComponent {

  FragmentComponent plus(FragmentModule module);

  void inject(MainActivity activity);

  void inject(NewsDetailActivity activity);

  void inject(LoginActivity activity);

  void inject(UserTopicActivity activity);

  void inject(UserFavoriteActivity activity);

  void inject(TopicDetailActivity activity);

  void inject(NotificationActivity activity);

  void inject(UserReplyActivity activity);

  void inject(ReplyActivity activity);

  void inject(TopicCreatorActivity activity);
}
