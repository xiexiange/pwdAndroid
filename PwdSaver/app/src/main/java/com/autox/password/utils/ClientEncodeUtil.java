package com.autox.password.utils;

import com.autox.password.EApplication;
import com.autox.password.TestHttpEncrypt;
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


    //测试 	APP解密服务器的响应内容
    public String decode() throws Exception{
        //APP端公钥和私钥从配置文件读取，不能写死在代码里
        Properties prop = new Properties();
        InputStream in = TestHttpEncrypt.class.getClassLoader().getResourceAsStream("client.properties");
        prop.load(in);
        String appPrivateKey = prop.getProperty("app.private.key");
        String content = "{\"ak\":\"mRBa005mea+6QIaFhTHrfCTBBFL+sy1uHI1iSN6LUK5/VQK/Bt9JZ+5/e2TQYMiD8U6KXBzZgHOl4RL8AErno9K4bbC+4Ke5Bl/IIGZ6kPJB4OjzbqBwxmmA+zJrcS3TlzIsVGpuIzGMQzIT0rlJl+BsQj6N9F3jfCeXBXH+JoTPEaTZqzQ9odgfPooP8jvuBOneqAiTmIgNzcVJwr7EB1tB65FjYPWFJqC0xrmLlrvev0KrD/XnKkzL1wGHc/eXeYzRXHuz4tbTHQV0mrZNz+tITXPVorRb0Tl0mglUafiqTkUBsXUv4abUvz2JImlF1nSAmQfKWfMNd7Fwag480g==\",\"ct\":\"DPMIYZaJL5e7Jvs2Vsy6jgnEPWBYFgjb1K1yf7gcWUCVyAfBPkLGK93onQkvLl8urp2yTwEsxzP6o1om0mqjkEU4oPpYf4NJC+QPQRQ2YTo=\"}";
        return HttpEncryptUtil.appDecrypt(appPrivateKey, content);
    }
}
