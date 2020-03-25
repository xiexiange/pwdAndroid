package com.autox.module.util;

public class MaskUtil {
    public static String mask(String input) {
        String result;
        int length = input.length();
        int showAccount = 0;
        if (length >= 9) {
            showAccount = 3;
        } else if (length >= 5) {
            showAccount = 2;
        } else {
            return input;
        }
        String pre = input.substring(0, showAccount);
        String last = input.substring(length - showAccount, length);
        for (int i = showAccount; i < length - showAccount; i++) {
            pre += "*";
        }
        result = pre + last;
        return result;
    }
}
