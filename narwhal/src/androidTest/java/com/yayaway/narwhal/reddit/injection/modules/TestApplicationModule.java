package com.yayaway.narwhal.reddit.injection.modules;

import android.content.Context;

import com.yayaway.narwhal.NarwhalApplication;
import com.yayaway.narwhal.reddit.AccountManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by erwinj on 12/8/15.
 */
@Module
public class TestApplicationModule {
    private final NarwhalApplication mApplication;
    private final AccountManager mAccountManager;
    private final Executor mExecutor;

    public TestApplicationModule(NarwhalApplication application) {
        mApplication = application;
        mAccountManager = new TestAccountManager();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    Executor provideExecutor() {
        return mExecutor;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    AccountManager provideAccountManager() {
        return mAccountManager;
    }
}
