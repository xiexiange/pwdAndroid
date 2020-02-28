package com.autox.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.autox.password.localdata.database.DbHelper;
import com.autox.password.localdata.database.items.PwdItem;
import com.autox.password.utils.Constant;
import com.autox.password.views.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class CategoryListActivity extends AppCompatActivity {
    private static final String EXTRA_TYPE = "type";

    private List<PwdItem> mPwdItemList = new ArrayList<>();
    private Constant.CATEGORY_TYPE mType;
    private TextView mTitleTV;
    String mTitle = "其它";
    private RelativeLayout mBackRL;
    private TextView mEditTV;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_category_list);
        mType = (Constant.CATEGORY_TYPE) getIntent().getSerializableExtra(EXTRA_TYPE);
        initData();
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

    private void initData() {

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
        mPwdItemList = DbHelper.getInstance().getPwdSizeByType(mTitle);
    }

    private void initViews() {
        mTitleTV = findViewById(R.id.list_page_title);
        mBackRL = findViewById(R.id.list_page_back_wrapper);
        mEditTV = findViewById(R.id.list_page_edit_btn);
        mRecyclerView = findViewById(R.id.list_rv_wrapper);
        mTitleTV.setText(mTitle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new RecyclerViewAdapter());
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


    private class RecyclerViewAdapter extends RecyclerView.Adapter {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(CategoryListActivity.this).inflate(R.layout.item_category_list, parent, false);
            return new RecyclerViewAdapter.RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            PwdItem tmpItem = mPwdItemList.get(position);
            String platform = tmpItem.platform();
            String account = tmpItem.account();
            ((RecyclerViewHolder)holder).platformTv.setText(platform);
            ((RecyclerViewHolder)holder).accountTv.setText(account);
        }

        @Override
        public int getItemCount() {
            return mPwdItemList.size();
        }

        private class RecyclerViewHolder extends RecyclerView.ViewHolder {
            private TextView platformTv;
            private TextView accountTv;
            public RecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                platformTv = itemView.findViewById(R.id.item_category_platform);
                accountTv = itemView.findViewById(R.id.item_category_account);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        CategoryListActivity.start(getActivity(), titleTV.getText().toString());
                    }
                });
            }
        }
    }

    private void showKeyboard(View v) {
        InputMethodManager manager = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) manager.showSoftInput(v, 0);
    }

}
