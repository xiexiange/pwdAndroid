package com.autox.password;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.autox.password.http.ServiceInstance;
import com.autox.password.http.entity.GetPlatMsgReception;
import com.autox.password.http.entity.UploadPlatMsgReception;
import com.autox.password.utils.ClientEncodeUtil;
import com.autox.password.utils.encode.HttpEncryptUtil;

import java.net.URLEncoder;
import java.security.Security;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
//        Security.addProvider(new SunJCE());
        try {
            String s = HttpEncryptUtil.serverDecrypt(ClientEncodeUtil.encode("345678"));

            Log.e("Echo", "s: " + s);
            String clientCode = ClientEncodeUtil.encode("123456");
            Log.e("Echo", "msg: " + clientCode);
            ServiceInstance.getInstance().uploadPlatMsg(
                    ClientEncodeUtil.encodeMD5("15201933576"),
                    ClientEncodeUtil.encodeMD5("爱奇艺"),
                    "",
                    URLEncoder.encode(clientCode, "utf-8"),
                    new ServiceInstance.Callback() {
                        @Override
                        public void onSuccess(UploadPlatMsgReception reception) {
                            uploadSuccess(reception);
                        }

                        @Override
                        public void onFailed() {

                        }
                    });
//            ServiceInstance.getInstance().getPlatMsg(
//                    ClientEncodeUtil.encodeMD5("15201933576"),
//                    ClientEncodeUtil.encodeMD5("爱奇艺"),
//                    "",
//                    new ServiceInstance.GetPlatCallback() {
//                        @Override
//                        public void onSuccess(GetPlatMsgReception reception) {
//                            getMsgSuccess(reception);
//                        }
//
//                        @Override
//                        public void onFailed() {
//
//                        }
//                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMsgSuccess(GetPlatMsgReception reception) {
        textView.setText(reception.code);
    }

    @UiThread
    public void uploadSuccess(UploadPlatMsgReception reception) {
        textView.setText(reception.code);
    }
}
