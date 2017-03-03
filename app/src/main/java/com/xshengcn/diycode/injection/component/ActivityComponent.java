package com.xshengcn.diycode.injection.component;

import com.xshengcn.diycode.injection.PreActivity;
import com.xshengcn.diycode.injection.module.ActivityModule;
import com.xshengcn.diycode.injection.module.FragmentModule;
import com.xshengcn.diycode.ui.login.LoginActivity;
import com.xshengcn.diycode.ui.main.MainActivity;
import com.xshengcn.diycode.ui.newsdetail.NewsDetailActivity;
import com.xshengcn.diycode.ui.notification.NotificationActivity;
import com.xshengcn.diycode.ui.reply.ReplyActivity;
import com.xshengcn.diycode.ui.topicdetail.TopicDetailActivity;
import com.xshengcn.diycode.ui.userfavorite.UserFavoriteActivity;
import com.xshengcn.diycode.ui.userreply.UserReplyActivity;
import com.xshengcn.diycode.ui.usertopic.UserTopicActivity;
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
}
