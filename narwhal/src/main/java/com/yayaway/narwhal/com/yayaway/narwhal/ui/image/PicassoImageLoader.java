package com.yayaway.narwhal.com.yayaway.narwhal.ui.image;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by erwinj on 12/8/15.
 */

public  class PicassoImageLoader implements ImageLoader {
        private Context mContext;

    public PicassoImageLoader(Context context) {
               this.mContext = context;
    }

    @Override
    public void displayThumbnail(String url, ImageView view) {
                Picasso.with(mContext)
                        .load(url)
                        .into(view);
    }

    @Override
    public void displayImage(String url, ImageView view, int width, int height) {
        Picasso.with(mContext)
                .load(url)
                .into(view);

    }
}
