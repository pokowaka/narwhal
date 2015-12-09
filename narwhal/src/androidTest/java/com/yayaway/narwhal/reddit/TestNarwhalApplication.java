package com.yayaway.narwhal.reddit;

import com.yayaway.narwhal.NarwhalApplication;
import com.yayaway.narwhal.injection.components.ApplicationComponent;
import com.yayaway.narwhal.reddit.injection.components.DaggerTestApplicationComponent;
import com.yayaway.narwhal.reddit.injection.modules.TestApplicationModule;

/**
 * Created by erwinj on 12/8/15.
 */
public class TestNarwhalApplication extends NarwhalApplication {
    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = DaggerTestApplicationComponent.builder()
                .testApplicationModule(new TestApplicationModule(this))
                .build();
        mComponent.inject(this);
    }
    @Override
    public ApplicationComponent getComponent() {
        return mComponent;
    }
}
