package com.tspolice.echallan.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.tspolice.echallan.App;

public class DeviceUtils {

    @SuppressLint("HardwareIds")
    public static String getDeviceUid() {
        Context context = App.getInstance();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String uniqueDeviceId = telephonyManager.getDeviceId();
        if (TextUtils.isEmpty(uniqueDeviceId)) {
            // for tablets
            ContentResolver cr = context.getContentResolver();
            uniqueDeviceId = Settings.Secure.getString(cr, Settings.Secure.ANDROID_ID);
        }

        return uniqueDeviceId;
    }

}
