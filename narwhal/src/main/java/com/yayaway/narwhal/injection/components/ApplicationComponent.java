package com.yayaway.narwhal.injection.components;

import com.yayaway.narwhal.LoginActivity;
import com.yayaway.narwhal.MainActivity;
import com.yayaway.narwhal.NarwhalApplication;
import com.yayaway.narwhal.SubmissionListFragment;
import com.yayaway.narwhal.injection.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Component for {@link NarwhalApplication}.
 * <p/>
 * This acts as pairing between the module and injection targets, each of which
 * has to have a corresponding inject method in this component.
 * <p/>
 * Created by mgouline on 23/04/15.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(NarwhalApplication application);

    void inject(LoginActivity activity);

    void inject(MainActivity activity);

    void inject(SubmissionListFragment fragment);
}