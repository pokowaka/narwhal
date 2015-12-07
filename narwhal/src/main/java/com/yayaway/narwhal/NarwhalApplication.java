package com.yayaway.narwhal;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yayaway.narwhal.injection.ApplicationModule;
import com.yayaway.narwhal.injection.DaggerNarwhalApplicationComponent;
import com.yayaway.narwhal.injection.NarwhalApplicationComponent;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;


@ReportsCrashes(
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "http://crash.narwhal.me/acra-narwhal/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "narwhal",
        formUriBasicAuthPassword = "f13rc3safe"
)
public class NarwhalApplication extends Application {

    private NarwhalApplicationComponent mComponent;

    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        super.onCreate();
        ACRA.init(this);
        initImageLoader(this);
        mComponent = DaggerNarwhalApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mComponent.inject(this);

    }

    /**
     * Extracts application from support context types.
     *
     * @param context Source context.
     * @return Application instance or {@code null}.
     */
    public static NarwhalApplication from(@NonNull Context context) {
        return (NarwhalApplication) context.getApplicationContext();
    }

    private static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(512 * 1024 * 1024); // 512 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        if (BuildConfig.DEBUG) {
            config.writeDebugLogs();
        }

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public NarwhalApplicationComponent getComponent() {
        return mComponent;
    }
}