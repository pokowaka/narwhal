package com.yayaway.narwhal.com.yayaway.narwhal.ui.image;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by erwinj on 12/8/15.
 */

public class PicassoImageLoader implements ImageLoader {
    //    private Context mContext;

    public PicassoImageLoader(Context context) {
        //        this.mContext = context;
    }

    @Override
    public void displayThumbnail(String url, ImageView view) {
        //        Picasso.with(mContext)
        //                .load(url)
        //                .resize(50, 50)
        //                .centerCrop()
        //                .into(view);
    }
}
