package com.yayaway.narwhal.injection;

import com.yayaway.narwhal.LoginActivity;
import com.yayaway.narwhal.NarwhalApplication;

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
    public interface NarwhalApplicationComponent {
    void inject(NarwhalApplication application);

    void inject(LoginActivity activity);
}