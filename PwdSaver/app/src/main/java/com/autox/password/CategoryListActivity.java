package com.autox.password;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autox.password.event.entity.DbChanged;
import com.autox.password.event.entity.EventEditClicked;
import com.autox.password.localdata.database.DbHelper;
import com.autox.password.localdata.database.items.PwdItem;
import com.autox.password.localdata.sharedprefs.SharedPrefKeys;
import com.autox.password.localdata.sharedprefs.SharedPrefUtils;
import com.autox.password.utils.Constant;
import com.autox.password.utils.MaskUtil;
import com.autox.password.views.EventTextView;
import com.autox.password.views.statusbar.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private RecyclerViewAdapter mAdatper;

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

    @Override
    public void onBackPressed() {
        if (mEditTV.getVisibility() == View.GONE) {
            mEditTV.setVisibility(View.VISIBLE);
            EventBus.getDefault().post(new EventEditClicked(false));
            return;
        }
        super.onBackPressed();
    }

    private void initViews() {
        mTitleTV = findViewById(R.id.list_page_title);
        mBackRL = findViewById(R.id.list_page_back_wrapper);
        mEditTV = findViewById(R.id.list_page_edit_btn);
        mRecyclerView = findViewById(R.id.list_rv_wrapper);
        mTitleTV.setText(mTitle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdatper = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdatper);
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
                mEditTV.setVisibility(View.GONE);
                EventBus.getDefault().post(new EventEditClicked(true));
            }
        });
        EventBus.getDefault().register(this);

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
            String fullAccount = tmpItem.account();
            String account = fullAccount;
            if (SharedPrefUtils.getBoolean(SharedPrefKeys.KEY_ENABLE_ACCOUNT_MASK, false)) {
                account = MaskUtil.mask(fullAccount);
            }
            ((RecyclerViewHolder)holder).platformTv.setText(platform);
            ((RecyclerViewHolder)holder).accountTv.setText(account);
            ((RecyclerViewHolder)holder).icon.setImageResource(PlatformListActivity.getDrawableIdByName(platform));

            ((RecyclerViewHolder)holder).mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEditTV.getVisibility() == View.GONE) {
                        return;
                    }
                    AddActivity.start(CategoryListActivity.this, mType, platform, fullAccount, tmpItem.pwd());
                }
            });
            ((RecyclerViewHolder)holder).delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showConfirmDialog(tmpItem);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPwdItemList.size();
        }

        private class RecyclerViewHolder extends RecyclerView.ViewHolder {
            private View mRoot;
            private TextView platformTv;
            private TextView accountTv;
            private ImageView icon;
            private EventTextView delete;
            public RecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                mRoot = itemView;
                platformTv = itemView.findViewById(R.id.item_category_platform);
                accountTv = itemView.findViewById(R.id.item_category_account);
                icon = itemView.findViewById(R.id.add_icon);
                delete = itemView.findViewById(R.id.item_category_delete);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void showKeyboard(View v) {
        InputMethodManager manager = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) manager.showSoftInput(v, 0);
    }

    private void showConfirmDialog(PwdItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).
                setTitle("确认删除").
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DbHelper.getInstance().setDeleted(item);
                EventBus.getDefault().post(new DbChanged());
            }
        }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();

    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void dbChanged(DbChanged changed) {
        mPwdItemList = DbHelper.getInstance().getPwdSizeByType(mTitle);
        mAdatper.notifyDataSetChanged();
        if (mEditTV.getVisibility() == View.GONE) {
            EventBus.getDefault().post(new EventEditClicked(true));
        }
    }
}
