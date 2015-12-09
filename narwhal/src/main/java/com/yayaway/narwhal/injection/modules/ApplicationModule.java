package com.yayaway.narwhal.injection.modules;

import android.content.Context;

import com.yayaway.narwhal.NarwhalApplication;
import com.yayaway.narwhal.reddit.AccountManager;
import com.yayaway.narwhal.reddit.DefaultAccountManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    private final Executor mExecutor;


    /**
     * Provides all the singletons to the application.
     *
     * @param application The application to inject this module into.
     */
    public ApplicationModule(NarwhalApplication application) {
        mApplication = application;
        mAccountManager = new DefaultAccountManager(application);
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
