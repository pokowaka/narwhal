package com.yayaway.narwhal;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.yayaway.narwhal.injection.HasComponent;
import com.yayaway.narwhal.injection.components.DaggerApplicationComponent;
import com.yayaway.narwhal.injection.components.ApplicationComponent;
import com.yayaway.narwhal.injection.modules.ApplicationModule;

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
public class NarwhalApplication extends Application implements HasComponent<ApplicationComponent> {

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        super.onCreate();
        ACRA.init(this);
        mComponent = DaggerApplicationComponent.builder()
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

    @Override
    public ApplicationComponent getComponent() {
        return mComponent;
    }
}