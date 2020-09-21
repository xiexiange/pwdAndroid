package com.autox.module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.autox.base.PrefUtil;
import com.autox.module.localdata.sharedprefs.SharedPrefKeys;
import com.autox.pwd_module.R;
import com.autox.views.StatusBarUtil;

public class PwdVerifyActivity extends AppCompatActivity {
    private EditText mInput;
    private TextView mConfirmBtn;
    public static final int RESULT_OK = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_pwd_verify);
        initViews();
        bindEvents();
        mInput.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
    public static void startForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, PwdVerifyActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
    public static void startForResult(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), PwdVerifyActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    private void initViews(){
        mInput = findViewById(R.id.pwd_input);
        mConfirmBtn = findViewById(R.id.verify_confirm_btn);
    }

    private void bindEvents() {
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String realPwd = PrefUtil.getString(SharedPrefKeys.KEY_PWD, "");
                if (TextUtils.equals(realPwd, mInput.getText().toString())) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(PwdVerifyActivity.this, "密码错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
