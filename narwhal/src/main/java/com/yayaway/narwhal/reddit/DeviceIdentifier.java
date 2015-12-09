package com.yayaway.narwhal.reddit;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import java.util.UUID;

/**
 * Created by erwinj on 12/3/15.
 */
public class DeviceIdentifier {

    private static final String DEVICE_ID = "UNIQUE_DEVICE_ID";

    public static UUID getDeviceId(Context ctx) {
        String uuid = "";

        // First see if we have a defined android id, this might not be there because
        // Android OEM's don't know what they are doing and the device might have been rooted.
        if (Settings.Secure.ANDROID_ID != null
                && !"android_id".equals(Settings.Secure.ANDROID_ID)) {
            uuid = Settings.Secure.ANDROID_ID;
        }

        if (uuid.isEmpty()) {
            // Well then I guess we will just create a GUID..
            SharedPreferences settings = ctx.getSharedPreferences(DEVICE_ID, 0);
            uuid = settings.getString(DEVICE_ID, null);
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(DEVICE_ID, uuid);
                editor.apply();
            }
        }

        return UUID.fromString(uuid);
    }
}
