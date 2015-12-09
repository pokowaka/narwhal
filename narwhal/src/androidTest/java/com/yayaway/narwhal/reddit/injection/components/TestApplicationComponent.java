package com.yayaway.narwhal.reddit.injection.components;

import com.yayaway.narwhal.NarwhalApplication;
import com.yayaway.narwhal.injection.components.ApplicationComponent;
import com.yayaway.narwhal.reddit.injection.modules.TestApplicationModule;

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
@Component(modules = TestApplicationModule.class)
public interface TestApplicationComponent extends ApplicationComponent {

}