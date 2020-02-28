package com.autox.password;

import android.app.Application;
import android.content.Context;

import com.autox.password.localdata.sharedprefs.SharedPrefUtils;

public class EApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        SharedPrefUtils.getInstance().init(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
