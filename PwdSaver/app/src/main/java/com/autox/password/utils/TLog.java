package com.autox.password.utils;

import android.util.Log;

import com.autox.password.BuildConfig;


public class TLog {
    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }
    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }
}
