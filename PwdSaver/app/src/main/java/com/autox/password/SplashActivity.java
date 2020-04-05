package com.autox.password;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.autox.module.MainActivity;
import com.autox.views.StatusBarUtil;

public class SplashActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatusBarUtil.setTranslucentStatus(this);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }


}
