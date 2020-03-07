package com.autox.password;

import android.app.Application;
import android.content.Context;

import com.autox.password.localdata.sharedprefs.SharedPrefUtils;
import com.tencent.bugly.crashreport.CrashReport;

public class EApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        SharedPrefUtils.getInstance().init(this);
        CrashReport.initCrashReport(getApplicationContext(), "b57fe81771", BuildConfig.DEBUG);
    }

    public static Context getContext() {
        return mContext;
    }
}
