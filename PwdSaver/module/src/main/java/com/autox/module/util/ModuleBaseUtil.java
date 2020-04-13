package com.autox.module.util;

import com.autox.base.BaseUtil;

public class ModuleBaseUtil {
    public static void recordUsage(String key, String value) {
        BaseUtil.getInstance().getImp().recordUsage(key, value);
    }
}
