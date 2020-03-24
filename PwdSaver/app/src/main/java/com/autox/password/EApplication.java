package com.autox.password;

import android.app.Application;
import android.content.Context;

import com.autox.base.PrefUtil;
import com.autox.password.localdata.sharedprefs.SharedPrefKeys;
import com.tencent.bugly.crashreport.CrashReport;

public class EApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        PrefUtil.getInstance().init(this);
        CrashReport.initCrashReport(getApplicationContext(), "b57fe81771", BuildConfig.DEBUG);
        if (PrefUtil.getLong(SharedPrefKeys.KEY_FIRST_INSTALL_TIME, -1L) == -1L) {
            PrefUtil.setLong(SharedPrefKeys.KEY_FIRST_INSTALL_TIME, System.currentTimeMillis());
        }
    }

    public static Context getContext() {
        return mContext;
    }
}
