package com.xshengcn.diycode.injection.component;

import com.xshengcn.diycode.injection.PreFragment;
import com.xshengcn.diycode.injection.module.FragmentModule;
import com.xshengcn.diycode.ui.fragment.NewsFragment;
import com.xshengcn.diycode.ui.fragment.ProjectFragment;
import com.xshengcn.diycode.ui.fragment.SiteFragment;
import com.xshengcn.diycode.ui.fragment.TopicFragment;

import dagger.Subcomponent;

@PreFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(NewsFragment newsFragment);

    void inject(TopicFragment topicFragment);

    void inject(ProjectFragment projectFragment);

    void inject(SiteFragment siteFragment);
}
