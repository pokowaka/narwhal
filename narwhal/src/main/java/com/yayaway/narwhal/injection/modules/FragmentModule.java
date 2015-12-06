package com.yayaway.narwhal.injection.modules;


/**
 * Created by erwinj on 12/7/15.
 */

import android.support.v4.app.Fragment;

import com.yayaway.narwhal.com.yayaway.narwhal.ui.image.GlideImageLoader;
import com.yayaway.narwhal.com.yayaway.narwhal.ui.image.ImageLoader;
import com.yayaway.narwhal.injection.PerFragment;

import dagger.Module;
import dagger.Provides;


@Module
public class FragmentModule {
    private final ImageLoader mImageLoader;

    public FragmentModule(Fragment fragment) {
        //mImageLoader = new UniversalImageLoader(NarwhalApplication.from(fragment.getContext()));
       // mImageLoader = new PicassoImageLoader(fragment.getContext());
        mImageLoader = new GlideImageLoader(fragment);
    }

    @PerFragment
    @Provides
    ImageLoader providesImageLoader() {
        return this.mImageLoader;
    }
}
