package com.yayaway.narwhal.injection.modules;

import android.support.v4.app.Fragment;

import com.yayaway.narwhal.com.yayaway.narwhal.ui.image.FrescoImageLoader;
import com.yayaway.narwhal.com.yayaway.narwhal.ui.image.ImageLoader;
import com.yayaway.narwhal.injection.PerFragment;

import dagger.Module;
import dagger.Provides;


@Module
public class FragmentModule {
    private final ImageLoader mImageLoader;

    /**
     * The module that provides dependency for the lifetime of a fragment.
     *
     * @param fragment The fragment for which we want to inject dependencies.
     */
    public FragmentModule(Fragment fragment) {
        // mImageLoader = new UniversalImageLoader(NarwhalApplication.from(fragment.getContext()));
        // mImageLoader = new PicassoImageLoader(fragment.getContext());
        //mImageLoader = new GlideImageLoader(fragment);
        mImageLoader = new FrescoImageLoader(fragment);
    }

    @PerFragment
    @Provides
    ImageLoader providesImageLoader() {
        return this.mImageLoader;
    }
}
