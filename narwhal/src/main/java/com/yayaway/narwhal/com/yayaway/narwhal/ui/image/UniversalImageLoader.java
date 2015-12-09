package com.yayaway.narwhal.com.yayaway.narwhal.ui.image;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by erwinj on 12/7/15.
 */
public class UniversalImageLoader implements ImageLoader {

    public UniversalImageLoader(Context context) {
        initImageLoader(context);
    }

    private static void initImageLoader(Context context) {
        //        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder
        // (context);
        //        config.threadPriority(Thread.NORM_PRIORITY - 2);
        //        config.denyCacheImageMultipleSizesInMemory();
        //        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        //        config.diskCacheSize(512 * 1024 * 1024); // 512 MiB
        //        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //        if (BuildConfig.DEBUG) {
        //            //    config.writeDebugLogs();
        //        }
        //
        //        // Initialize ImageLoader with configuration.
        //        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config
        // .build());
    }

    @Override
    public void displayThumbnail(String url, ImageView view) {
        //        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
        // .displayThumbnail(url,
        // view);
    }
}
