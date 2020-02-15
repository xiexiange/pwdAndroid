package com.autox.password.utils;

import com.autox.password.EApplication;
import com.autox.password.utils.encode.HttpEncryptUtil;
import com.autox.password.utils.encode.MD5;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientEncodeUtil {
    public static String encodeMD5(String content) {
        return MD5.encode("NOT_NOW", content, 16);
    }

    public static String encode(String msg) {
        Properties prop = new Properties();
        String result = "";
        try {
            InputStream in = EApplication.getContext().getAssets().open("client.properties");
            prop.load(in);
            String appPublicKey = prop.getProperty("app.public.key");
            result = HttpEncryptUtil.appEncrypt(appPublicKey, msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
