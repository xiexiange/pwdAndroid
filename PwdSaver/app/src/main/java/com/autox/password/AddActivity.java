package com.autox.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.autox.password.event.entity.EventTabClicked;
import com.autox.password.frames.AddFrameLayout;
import com.autox.password.frames.CategoryFrameLayout;
import com.autox.password.frames.MeFrameLayout;
import com.autox.password.views.TabView;
import com.autox.password.views.statusbar.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AddActivity extends AppCompatActivity {
    private static final String EXTRA_TYPE = "type";
    public enum TYPE {WORK, VIDEO, MAIL, MONEY, GAME, WEB, OTHER}
    private TYPE mType;
    private ImageView mIconIV;
    private TextView mTitleTV;
    private EditText mAccountET;
    private ImageView mAccountCloseIV;
    private EditText mPwdET;
    private ImageView mPwdCloseIV;
    String mContentRegular = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    private RelativeLayout mBackRL;
    private TextView mSaveTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_add);
        mType = (TYPE) getIntent().getSerializableExtra(EXTRA_TYPE);
        initViews();
        bindEvents();
    }

    public static void start(Context context, TYPE type) {
        Intent intent = new Intent(context, AddActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    private void initViews() {
        mIconIV = findViewById(R.id.add_page_detail_icon);
        mTitleTV = findViewById(R.id.add_page_detail_title);
        mBackRL = findViewById(R.id.add_page_back_wrapper);
        mSaveTV = findViewById(R.id.add_page_save_btn);
        mAccountET = findViewById(R.id.add_page_account_content);
        mAccountCloseIV = findViewById(R.id.add_page_clear_account);
        mPwdET = findViewById(R.id.add_page_pwd_content);
        mPwdCloseIV = findViewById(R.id.add_page_clear_pwd);
        String title;
        int imageId;
        switch (mType) {
            case WORK:
                title = "工作";
                imageId = R.drawable.icon_work;
                break;
            case VIDEO:
                title = "视频";
                imageId = R.drawable.icon_video;
                break;
            case MAIL:
                title = "邮箱";
                imageId = R.drawable.icon_mail;
                break;
            case MONEY:
                title = "金融";
                imageId = R.drawable.icon_wallet;
                break;
            case GAME:
                title = "游戏";
                imageId = R.drawable.icon_game;
                break;
            case WEB:
                title = "网址";
                imageId = R.drawable.icon_web;
                break;
            case OTHER:
            default:
                title = "其它";
                imageId = R.drawable.icon_other;
                break;
        }
        mIconIV.setImageResource(imageId);
        mTitleTV.setText(title);

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
                Toast.makeText(AddActivity.this, "保存", Toast.LENGTH_SHORT).show();
            }
        });
        mAccountCloseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccountET.setText("");
                mAccountET.requestFocus();
                InputMethodManager manager = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
                if (manager != null) manager.showSoftInput(v, 0);
            }
        });
        mAccountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
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
                InputMethodManager manager = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
                if (manager != null) manager.showSoftInput(v, 0);

            }
        });
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
                if(edt.toString().getBytes().length != edt.length()){
                    edt.delete(temp.length()-1, temp.length());
                }
                try {
                    temp = edt.toString();
                    String tem = temp.substring(temp.length()-1, temp.length());
                    char[] temC = tem.toCharArray();
                    int mid = temC[0];
                    boolean needDelete = true;
                    if(mid>=33&&mid<=122){
                        needDelete = false;
                    }
                    if (needDelete) {
                        edt.delete(temp.length() - 1, temp.length());
                    }

                } catch (Exception e) {
                }


                int length = mPwdET.getText().toString().length();
                if (length == 0) {
                    mPwdCloseIV.setVisibility(View.GONE);
                } else {
                    mPwdCloseIV.setVisibility(View.VISIBLE);
                }
            }
        });

    }

}
