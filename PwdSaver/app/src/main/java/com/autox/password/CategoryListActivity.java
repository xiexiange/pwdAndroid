package com.autox.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.autox.password.utils.Constant;
import com.autox.password.views.statusbar.StatusBarUtil;

public class CategoryListActivity extends AppCompatActivity {
    private static final String EXTRA_TYPE = "type";
    private Constant.CATEGORY_TYPE mType;
    private TextView mTitleTV;
    String mTitle = "其它";
    private RelativeLayout mBackRL;
    private TextView mEditTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_category_list);
        mType = (Constant.CATEGORY_TYPE) getIntent().getSerializableExtra(EXTRA_TYPE);
        initViews();
        bindEvents();
    }

    public static void start(Context context, Constant.CATEGORY_TYPE type) {
        Intent intent = new Intent(context, CategoryListActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    public static void start(Context context, String name) {
        Constant.CATEGORY_TYPE type = Constant.CATEGORY_TYPE.GAME;
        switch (name) {
            case "工作":
                type = Constant.CATEGORY_TYPE.WORK;
                break;
            case "视频":
                type = Constant.CATEGORY_TYPE.VIDEO;
                break;
            case "邮箱":
                type = Constant.CATEGORY_TYPE.MAIL;
                break;
            case "金融":
                type = Constant.CATEGORY_TYPE.MONEY;
                break;
            case "网址":
                type = Constant.CATEGORY_TYPE.WEB;
                break;
            case "其它":
                type = Constant.CATEGORY_TYPE.OTHER;
                break;
        }
        Intent intent = new Intent(context, CategoryListActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    private void initViews() {
        mTitleTV = findViewById(R.id.list_page_title);
        mBackRL = findViewById(R.id.list_page_back_wrapper);
        mEditTV = findViewById(R.id.list_page_edit_btn);
        switch (mType) {
            case WORK:
                mTitle = "工作";
                break;
            case VIDEO:
                mTitle = "视频";
                break;
            case MAIL:
                mTitle = "邮箱";
                break;
            case MONEY:
                mTitle = "金融";
                break;
            case GAME:
                mTitle = "游戏";
                break;
            case WEB:
                mTitle = "网址";
                break;
            case OTHER:
            default:
                mTitle = "其它";
                break;
        }
        mTitleTV.setText(mTitle);

    }

    private void bindEvents() {
        mBackRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mEditTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = mTitle;

            }
        });

    }

    private void showKeyboard(View v) {
        InputMethodManager manager = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) manager.showSoftInput(v, 0);
    }

}
