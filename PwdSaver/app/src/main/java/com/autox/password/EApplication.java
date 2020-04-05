package com.autox.password;

import android.app.Application;
import android.content.Context;

import com.autox.base.BaseUtil;
import com.autox.base.PrefUtil;
import com.autox.module.localdata.sharedprefs.SharedPrefKeys;

public class EApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        PrefUtil.getInstance().init(this);
        if (PrefUtil.getLong(SharedPrefKeys.KEY_FIRST_INSTALL_TIME, -1L) == -1L) {
            PrefUtil.setLong(SharedPrefKeys.KEY_FIRST_INSTALL_TIME, System.currentTimeMillis());
        }
        BaseUtil.getInstance().setImpl(new BaseUtil.IImpl() {
            @Override
            public Context getContext() {
                return EApplication.this;
            }

            @Override
            public String getApplicationID() {
                return BuildConfig.APPLICATION_ID;
            }
        });
    }

    public static Context getContext() {
        return mContext;
    }
}
