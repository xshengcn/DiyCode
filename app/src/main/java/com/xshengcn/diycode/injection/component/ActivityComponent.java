package com.xshengcn.diycode.injection.component;

import com.xshengcn.diycode.injection.PreActivity;
import com.xshengcn.diycode.injection.module.ActivityModule;
import com.xshengcn.diycode.injection.module.FragmentModule;
import com.xshengcn.diycode.ui.activity.LoginActivity;
import com.xshengcn.diycode.ui.activity.MainActivity;
import com.xshengcn.diycode.ui.activity.NewsDetailActivity;
import com.xshengcn.diycode.ui.activity.NotificationActivity;
import com.xshengcn.diycode.ui.activity.SiteActivity;
import com.xshengcn.diycode.ui.activity.TopicCreatorActivity;
import com.xshengcn.diycode.ui.activity.TopicDetailActivity;
import com.xshengcn.diycode.ui.activity.TopicReplyActivity;
import com.xshengcn.diycode.ui.activity.UserFavoriteActivity;
import com.xshengcn.diycode.ui.activity.UserReplyActivity;
import com.xshengcn.diycode.ui.activity.UserTopicActivity;

import dagger.Subcomponent;

@PreActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    FragmentComponent plus(FragmentModule module);

    void inject(MainActivity mainActivity);

    void inject(NewsDetailActivity newsDetailActivity);

    void inject(LoginActivity loginActivity);

    void inject(UserTopicActivity userTopicActivity);

    void inject(UserFavoriteActivity userFavoriteActivity);

    void inject(TopicDetailActivity topicDetailActivity);

    void inject(NotificationActivity notificationActivity);

    void inject(UserReplyActivity userReplyActivity);

    void inject(TopicReplyActivity topicCommentActivity);

    void inject(TopicCreatorActivity topicCreatorActivity);

    void inject(SiteActivity siteActivity);
}
