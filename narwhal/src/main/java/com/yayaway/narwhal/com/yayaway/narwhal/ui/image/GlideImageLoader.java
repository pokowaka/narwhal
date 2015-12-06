package com.yayaway.narwhal.com.yayaway.narwhal.ui.image;

import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by erwinj on 12/7/15.
 */
public class GlideImageLoader implements ImageLoader {

    private Fragment mFragment;

    public GlideImageLoader(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public void displayThumbnail(String url, ImageView view) {
        Glide.with(mFragment)
                .load(url)
                .crossFade()
                .into(view);
    }
}
