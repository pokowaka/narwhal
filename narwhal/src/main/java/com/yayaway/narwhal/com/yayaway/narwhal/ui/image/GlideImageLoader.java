package com.yayaway.narwhal.com.yayaway.narwhal.ui.image;

import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by erwinj on 12/7/15.
 */
public class GlideImageLoader implements ImageLoader {

    private static final Logger logger = LoggerFactory.getLogger(GlideImageLoader.class);
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

    @Override
    public void displayImage(String url, ImageView view, int width, int height) {
        if (url == null)
            return;
        if (url.endsWith("gifv")) {
            url = url.substring(0, url.length() - 1);
        } else if (!(url.endsWith("gif") || url.endsWith("jpg") || url.endsWith("png"))) {
            url += ".gif";
        }

        logger.info("displayImage() called with: " +  "url = [" + url + "], view = [" + view + "], width = [" + width + "], height = [" + height + "]");
        Glide.with(mFragment)
                .load(url)
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                //.centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                //.crossFade()
           //     .signature(new StringSignature("" + mFragment.hashCode()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable>
                            target, boolean isFirstResource) {
                        logger.error("onException() called with: " + "e = [" + e + "], model = ["
                                + model + "], target = [" + target + "], isFirstResource = [" +
                                isFirstResource + "]");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target, boolean
                                                           isFromMemoryCache, boolean
                                                           isFirstResource) {
                        logger.info("onResourceReady() called with: " + "resource = [" + resource
                                + "], model = [" + model + "], target = [" + target + "], " +
                                "isFromMemoryCache = [" + isFromMemoryCache + "], isFirstResource" +
                                " = [" + isFirstResource + "]");
                        return false;
                    }
                })
                .into(view);
    }
}
