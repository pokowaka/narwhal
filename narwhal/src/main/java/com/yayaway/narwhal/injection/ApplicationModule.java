package com.yayaway.narwhal.injection;

import android.content.Context;

import com.yayaway.narwhal.NarwhalApplication;
import com.yayaway.narwhal.reddit.AccountManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by erwinj on 12/6/15.
 */
@Module
public class ApplicationModule {
    private final NarwhalApplication mApplication;
    private final AccountManager mAccountManager;

    public ApplicationModule(NarwhalApplication application) {
        mApplication = application;
        mAccountManager = new AccountManager(application);
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
