package com.autox.module;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.autox.base.PrefUtil;
import com.autox.module.localdata.sharedprefs.SharedPrefKeys;
import com.autox.pwd_module.R;
import com.autox.views.StatusBarUtil;

public class PwdSetActivity extends AppCompatActivity {
    private ImageView mBackIv;
    private TextView mConfirmTv;
    private EditText mFirstInput;
    private EditText mConfirmInput;
    public static final int RESULT_OK = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_set_pwd);
        initView();
        bindEvents();
    }

    public static void startForResult(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), PwdSetActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    private void initView() {
        mBackIv = findViewById(R.id.pwd_set_back_iv);
        mConfirmTv = findViewById(R.id.pwd_confirm_btn);
        mFirstInput = findViewById(R.id.first_pwd_input);
        mConfirmInput = findViewById(R.id.confirm_pwd_input);
    }

    private void bindEvents() {
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mConfirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstText = mFirstInput.getText().toString();
                String confirmText = mConfirmInput.getText().toString();
                if (!TextUtils.equals(firstText,confirmText)) {
                    Toast.makeText(PwdSetActivity.this, "两次密码不一致，请确认输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                PrefUtil.setString(SharedPrefKeys.KEY_PWD, firstText);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

}
