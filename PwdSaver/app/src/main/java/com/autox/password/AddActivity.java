package com.autox.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.autox.password.localdata.database.DbHelper;
import com.autox.password.localdata.database.items.PwdItem;
import com.autox.password.utils.Constant;
import com.autox.password.views.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    private static final String EXTRA_TYPE = "type";
    private static final String EXTRA_PLATFORM = "platform";
    private static final String EXTRA_ACCOUNT = "account";
    private static final String EXTRA_PWD = "pwd";
    private Constant.CATEGORY_TYPE mType;
    private String mPlatFormPassIn;
    private String mAccountPassIn;
    private String mPwdPassIn;
    private ImageView mIconIV;
    private TextView mTitleTV;
    private EditText mAccountET;
    private ImageView mAccountCloseIV;
    private EditText mPlatformET;
    private ImageView mPlatformCloseIV;
    private EditText mPwdET;
    private ImageView mPwdCloseIV;
    String mTitle = "其它";
    private RelativeLayout mBackRL;
    private TextView mSaveTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_add);
        mType = (Constant.CATEGORY_TYPE) getIntent().getSerializableExtra(EXTRA_TYPE);
        mAccountPassIn = getIntent().getStringExtra(EXTRA_ACCOUNT);
        mPlatFormPassIn = getIntent().getStringExtra(EXTRA_PLATFORM);
        mPwdPassIn = getIntent().getStringExtra(EXTRA_PWD);
        initViews();
        bindEvents();
    }

    public static void start(Context context, Constant.CATEGORY_TYPE type, @NonNull String platform, @NonNull String account, @NonNull String Pwd) {
        Intent intent = new Intent(context, AddActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_PLATFORM, platform);
        intent.putExtra(EXTRA_ACCOUNT, account);
        intent.putExtra(EXTRA_PWD, Pwd);
        context.startActivity(intent);
    }

    private void initViews() {
        mIconIV = findViewById(R.id.add_page_detail_icon);
        mTitleTV = findViewById(R.id.add_page_detail_title);
        mBackRL = findViewById(R.id.add_page_back_wrapper);
        mSaveTV = findViewById(R.id.add_page_save_btn);
        mPlatformET = findViewById(R.id.add_page_platform_content);
        mPlatformCloseIV = findViewById(R.id.add_page_clear_platform);
        mAccountET = findViewById(R.id.add_page_account_content);
        mAccountCloseIV = findViewById(R.id.add_page_clear_account);
        mPwdET = findViewById(R.id.add_page_pwd_content);
        mPwdCloseIV = findViewById(R.id.add_page_clear_pwd);
        if (!TextUtils.isEmpty(mAccountPassIn)) {
            mAccountET.setText(mAccountPassIn);
            mPlatformET.setText(mPlatFormPassIn);
            mPlatformET.setEnabled(false);
            mAccountET.setEnabled(false);
            mPwdET.setText(mPwdPassIn);
            mPlatformET.requestFocus();
            mPlatformET.clearFocus();
        }
        int imageId;
        switch (mType) {
            case WORK:
                mTitle = "工作";
                imageId = R.drawable.icon_work;
                break;
            case VIDEO:
                mTitle = "视频";
                imageId = R.drawable.icon_video;
                break;
            case MAIL:
                mTitle = "邮箱";
                imageId = R.drawable.icon_mail;
                break;
            case MONEY:
                mTitle = "金融";
                imageId = R.drawable.icon_wallet;
                break;
            case GAME:
                mTitle = "游戏";
                imageId = R.drawable.icon_game;
                break;
            case WEB:
                mTitle = "网址";
                imageId = R.drawable.icon_web;
                break;
            case OTHER:
            default:
                mTitle = "其它";
                imageId = R.drawable.icon_other;
                break;
        }
        mIconIV.setImageResource(imageId);
        mTitleTV.setText(mTitle);

    }

    private void bindEvents() {
        mBackRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSaveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = mTitle;
                String platform = mPlatformET.getText().toString();
                String account = mAccountET.getText().toString();
                String pwd = mPwdET.getText().toString();
                if (TextUtils.isEmpty(platform)) {
                    Toast.makeText(AddActivity.this, "平台名称不能为空，可填入\"谷歌\"等", Toast.LENGTH_SHORT).show();
                    mPlatformET.requestFocus();
                    showKeyboard(v);
                    return;
                }
                if (TextUtils.isEmpty(account)) {
                    Toast.makeText(AddActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                    mAccountET.requestFocus();
                    showKeyboard(v);
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(AddActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    mPwdET.requestFocus();
                    showKeyboard(v);
                    return;
                }
                DbHelper.getInstance().insert(new PwdItem(type, platform, account, pwd, System.currentTimeMillis()), false);
                Toast.makeText(AddActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        mAccountCloseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccountET.setText("");
                mAccountET.requestFocus();
                showKeyboard(v);
            }
        });

        mPlatformCloseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlatformET.setText("");
                mPlatformET.requestFocus();
                showKeyboard(v);
            }
        });

        mPlatformET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable edt) {
                int length = mPlatformET.getText().toString().length();
                if (length == 0) {
                    mPlatformCloseIV.setVisibility(View.GONE);
                } else {
                    mPlatformCloseIV.setVisibility(View.VISIBLE);
                }
            }
        });

        InputFilter contentFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                return !(c >=7 && c <= 32);
            }
        };
        mAccountET.setFilters(new InputFilter[]{contentFilter});
        mAccountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable edt) {
                int length = mAccountET.getText().toString().length();
                if (length == 0) {
                    mAccountCloseIV.setVisibility(View.GONE);
                } else {
                    mAccountCloseIV.setVisibility(View.VISIBLE);
                }
            }
        });
        mPwdCloseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPwdET.setText("");
                mPwdET.requestFocus();
                showKeyboard(v);

            }
        });

        InputFilter pwdFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                return c >= 33 && c <= 122;
            }
        };

        mPwdET.setFilters(new InputFilter[]{pwdFilter});

        mPwdET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                //https://blog.csdn.net/qq_30054961/article/details/82463748
//                if(edt.toString().getBytes().length != edt.length()){
//                    edt.delete(temp.length()-1, temp.length());
//                }
//                try {
//                    temp = edt.toString();
//                    String tem = temp.substring(temp.length()-1, temp.length());
//                    char[] temC = tem.toCharArray();
//                    int mid = temC[0];
//                    boolean needDelete = true;
//                    if(mid >= 33 && mid <= 122){
//                        needDelete = false;
//                    }
//                    if (needDelete) {
//                        edt.clear();
//                    }
//
//                } catch (Exception e) {
//                }


                int length = mPwdET.getText().toString().length();
                if (length == 0) {
                    mPwdCloseIV.setVisibility(View.GONE);
                } else {
                    mPwdCloseIV.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void showKeyboard(View v) {
        InputMethodManager manager = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) manager.showSoftInput(v, 0);
    }

}
