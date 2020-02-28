package com.autox.password.localdata.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {
    private static volatile SharedPrefUtils sInstance;
    private Context mContext;
    private SharedPreferences preferences;

    public static SharedPrefUtils getInstance() {
        if (sInstance == null) {
            synchronized (SharedPrefUtils.class) {
                if (sInstance == null) {
                    sInstance = new SharedPrefUtils();
                }
            }
        }
        return sInstance;
    }


    private SharedPrefUtils() {
    }

    public void init(Context context) {
        mContext = context;
        preferences = mContext.getSharedPreferences("pwd", Context.MODE_PRIVATE);
    }


    public static void setBoolean(String key, Boolean value) {
        SharedPreferences preference = getInstance().preferences;
        SharedPreferences.Editor editor =  preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, Boolean defaultValue) {
        SharedPreferences preference = getInstance().preferences;
        return preference.getBoolean(key, defaultValue);
    }


    public static void setInteger(String key, Integer value) {
        SharedPreferences preference = getInstance().preferences;
        SharedPreferences.Editor editor =  preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInteger(String key, Integer defaultValue) {
        SharedPreferences preference = getInstance().preferences;
        return preference.getInt(key, defaultValue);
    }


    public static void setLong(String key, Long value) {
        SharedPreferences preference = getInstance().preferences;
        SharedPreferences.Editor editor =  preference.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(String key, Long defaultValue) {
        SharedPreferences preference = getInstance().preferences;
        return preference.getLong(key, defaultValue);
    }


    public static void setString(String key, String value) {
        SharedPreferences preference = getInstance().preferences;
        SharedPreferences.Editor editor =  preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key, String defaultVaule) {
        SharedPreferences preference = getInstance().preferences;
        return preference.getString(key, defaultVaule);
    }
}
