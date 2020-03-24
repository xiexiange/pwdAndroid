package com.autox.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.huawei.android.sdk.drm.Drm;
import com.huawei.android.sdk.drm.DrmCheckCallback;

public class HuaweiMainActivity extends AppCompatActivity {
    //版权保护id
    private static final String DRM_ID = "890086000300380288";
    //版权保护公钥
    private static final String DRM_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsXTgjpqHVHsKqpY4KoEN4jlI7o71Ilqwp11Dp2RM";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Drm.check(this, this.getPackageName(), DRM_ID, DRM_PUBLIC_KEY,new MyDrmCheckCallback());

    }
    private class MyDrmCheckCallback implements DrmCheckCallback {
        @Override
        public void onCheckSuccess() {
            //鉴权成功
            Toast.makeText(HuaweiMainActivity.this, "鉴权成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCheckFailed() {
            //鉴权失败
            Toast.makeText(HuaweiMainActivity.this, "鉴权失败", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
