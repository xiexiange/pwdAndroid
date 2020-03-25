package com.autox.module.util;

import com.autox.module.util.encode.HttpEncryptUtil;
import com.autox.module.util.encode.KeyUtil;
import com.autox.module.util.encode.MD5;

import java.io.IOException;
import java.util.Properties;

public class ClientEncodeUtil {
    public static String encodeMD5(String content) {
        return MD5.encode("NOT_NOW", content, 16);
    }

    public static String encode(String msg) {
        Properties prop = new Properties();
        String result = "";
        try {
            // 这里应该是客户端的，但暂时没有服务器端，直接统一
            result = HttpEncryptUtil.appEncrypt(KeyUtil.SERVER_PUBLIC_KEY, msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    //测试 	APP解密服务器的响应内容
    public static String decode(String encodedStr) throws Exception{
        //APP端公钥和私钥从配置文件读取，不能写死在代码里
//        Properties prop = new Properties();
//        InputStream in = EApplication.getContext().getAssets().open("");
//        prop.load(in);
//        String appPrivateKey = prop.getProperty("app.private.key");

        // 这里应该是客户端的，但暂时没有服务器端，直接统一
        return HttpEncryptUtil.appDecrypt(KeyUtil.SERVER_PRIVATE_KEY, encodedStr);
    }
}
