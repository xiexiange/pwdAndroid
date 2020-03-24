package com.autox.password;

import android.content.Context;
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

import com.autox.password.huawei.R;
import com.autox.password.utils.Constant;
import com.autox.password.views.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlatformListActivity extends AppCompatActivity {
    private static final String EXTRA_TYPE = "type";

    private List<PlatformListItem> mPlatformList = new ArrayList<>();
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
        setContentView(R.layout.activity_platform_list);
        mType = (Constant.CATEGORY_TYPE) getIntent().getSerializableExtra(EXTRA_TYPE);
        initData();
        initViews();
        bindEvents();
    }

    public static void start(Context context, Constant.CATEGORY_TYPE type) {
        Intent intent = new Intent(context, PlatformListActivity.class);
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
        start(context, type);
//        Intent intent = new Intent(context, PlatformListActivity.class);
//        intent.putExtra(EXTRA_TYPE, type);
//        context.startActivity(intent);
    }

    private void initData() {
        List<PlatformListItem> result = new ArrayList<>();
        switch (mType) {
            case WORK:
                mTitle = "工作";
                result.add(new PlatformListItem("邮箱", R.drawable.platform_icon_mail));
                result.add(new PlatformListItem("电脑开机", R.drawable.platform_icon_shutdown));
                break;
            case VIDEO:
                mTitle = "视频";
                result.add(new PlatformListItem("爱奇艺视频", R.drawable.platform_icon_iqiyi));
                result.add(new PlatformListItem("优酷视频", R.drawable.platform_icon_youku));
                result.add(new PlatformListItem("搜狐视频", R.drawable.platform_icon_souhu));
                result.add(new PlatformListItem("土豆视频", R.drawable.platform_icon_tudou));
                result.add(new PlatformListItem("乐视TV", R.drawable.platform_icon_letv));
                result.add(new PlatformListItem("芒果", R.drawable.platform_icon_mango));
                result.add(new PlatformListItem("快手", R.drawable.platform_icon_kuaishou));
                result.add(new PlatformListItem("抖音", R.drawable.platform_icon_yin));
                result.add(new PlatformListItem("斗鱼", R.drawable.platform_icon_douyu));
                result.add(new PlatformListItem("虎牙", R.drawable.platform_icon_huya));
                break;
            case MAIL:
                mTitle = "邮箱";
                result.add(new PlatformListItem("QQ邮箱", R.drawable.platform_icon_qqmail));
                result.add(new PlatformListItem("谷歌邮箱", R.drawable.platform_icon_googlemail));
                result.add(new PlatformListItem("微软邮箱", R.drawable.platform_icon_micro));
                result.add(new PlatformListItem("雅虎邮箱", R.drawable.platform_icon_yahu));
                result.add(new PlatformListItem("新浪邮箱", R.drawable.platform_icon_sinamail));
                result.add(new PlatformListItem("网易邮箱", R.drawable.platform_icon_163));
                result.add(new PlatformListItem("139邮箱", R.drawable.platform_icon_139));
                break;
            case MONEY:
                mTitle = "金融";
                result.add(new PlatformListItem("招商银行(需要密码保护)", R.drawable.platform_icon_zhaoshang));
                result.add(new PlatformListItem("建设银行(需要密码保护)", R.drawable.platform_icon_jianshe));
                result.add(new PlatformListItem("农业银行(需要密码保护)", R.drawable.platform_icon_nongye));
                result.add(new PlatformListItem("中国银行(需要密码保护)", R.drawable.platform_icon_zhongguo));
                result.add(new PlatformListItem("工商银行(需要密码保护)", R.drawable.platform_icon_gongshang));
                result.add(new PlatformListItem("浦发银行(需要密码保护)", R.drawable.platform_icon_pufa));
                result.add(new PlatformListItem("广发银行(需要密码保护)", R.drawable.platform_icon_guangfa));
                result.add(new PlatformListItem("信用社(需要密码保护)", R.drawable.platform_icon_xinyongshe));
                result.add(new PlatformListItem("邮政储蓄(需要密码保护)", R.drawable.platform_icon_youzheng));
                result.add(new PlatformListItem("本地银行(需要密码保护)", R.drawable.platform_icon_localbank));
                break;
            case GAME:
                mTitle = "游戏";
                result.add(new PlatformListItem("手机游戏", R.drawable.platform_icon_mobilegame));
                result.add(new PlatformListItem("电脑游戏", R.drawable.platform_icon_pcgame));
                result.add(new PlatformListItem("PS游戏", R.drawable.platform_icon_psgame));
                result.add(new PlatformListItem("VR游戏", R.drawable.platform_icon_vrgame));
                break;
            case WEB:
                mTitle = "网址";
                result.add(new PlatformListItem("谷歌", R.drawable.platform_icon_google));
                result.add(new PlatformListItem("百度", R.drawable.platform_icon_baidu));
                result.add(new PlatformListItem("Facebook", R.drawable.platform_icon_facebook));
                result.add(new PlatformListItem("Twitter", R.drawable.platform_icon_twitter));
                result.add(new PlatformListItem("新浪", R.drawable.platform_icon_sina));
                result.add(new PlatformListItem("美团", R.drawable.platform_icon_meituan));
                result.add(new PlatformListItem("饿了么", R.drawable.platform_icon_eleme));
                result.add(new PlatformListItem("58同城", R.drawable.platform_icon_58));
                break;
            case OTHER:
            default:
                mTitle = "其它";
                result.add(new PlatformListItem("其它", R.drawable.platform_icon_other));
                break;
        }
        mPlatformList = result;
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
            View itemView = LayoutInflater.from(PlatformListActivity.this).inflate(R.layout.item_platform_list, parent, false);
            return new RecyclerViewAdapter.RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            PlatformListItem tmpItem = mPlatformList.get(position);
            String platform = tmpItem.name();
            int drawableId = tmpItem.drawableId();
            ((RecyclerViewHolder)holder).platformTv.setText(platform);
            ((RecyclerViewHolder)holder).icon.setImageResource(drawableId);
            ((RecyclerViewHolder)holder).mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("result", platform);
                    setResult(1001, intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPlatformList.size();
        }

        private class RecyclerViewHolder extends RecyclerView.ViewHolder {
            private View mRoot;
            private TextView platformTv;
            private ImageView icon;
            public RecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                mRoot = itemView;
                platformTv = itemView.findViewById(R.id.item_platform_name);
                icon = itemView.findViewById(R.id.platform_icon);
            }
        }
    }


    class PlatformListItem {
        private String name;
        private int drawableId;
        public PlatformListItem(String name, int id) {
            this.name = name;
            this.drawableId = id;
        }

        public String name() {
            return name;
        }

        public int drawableId() {
            return drawableId;
        }
    }

    public static int getDrawableIdByName(String name) {
        Map<String, Integer> result = new HashMap<>();

        result.put("邮箱", R.drawable.platform_icon_mail);
        result.put("电脑开机", R.drawable.platform_icon_shutdown);
        result.put("爱奇艺视频", R.drawable.platform_icon_iqiyi);
        result.put("优酷视频", R.drawable.platform_icon_youku);
        result.put("搜狐视频", R.drawable.platform_icon_souhu);
        result.put("土豆视频", R.drawable.platform_icon_tudou);
        result.put("乐视TV", R.drawable.platform_icon_letv);
        result.put("芒果", R.drawable.platform_icon_mango);
        result.put("快手", R.drawable.platform_icon_kuaishou);
        result.put("抖音", R.drawable.platform_icon_yin);
        result.put("斗鱼", R.drawable.platform_icon_douyu);
        result.put("虎牙", R.drawable.platform_icon_huya);
        result.put("QQ邮箱", R.drawable.platform_icon_qqmail);
        result.put("谷歌邮箱", R.drawable.platform_icon_googlemail);
        result.put("微软邮箱", R.drawable.platform_icon_micro);
        result.put("雅虎邮箱", R.drawable.platform_icon_yahu);
        result.put("新浪邮箱", R.drawable.platform_icon_sinamail);
        result.put("网易邮箱", R.drawable.platform_icon_163);
        result.put("139邮箱", R.drawable.platform_icon_139);
        result.put("招商银行(需要密码保护)", R.drawable.platform_icon_zhaoshang);
        result.put("建设银行(需要密码保护)", R.drawable.platform_icon_jianshe);
        result.put("农业银行(需要密码保护)", R.drawable.platform_icon_nongye);
        result.put("中国银行(需要密码保护)", R.drawable.platform_icon_zhongguo);
        result.put("工商银行(需要密码保护)", R.drawable.platform_icon_gongshang);
        result.put("浦发银行(需要密码保护)", R.drawable.platform_icon_pufa);
        result.put("广发银行(需要密码保护)", R.drawable.platform_icon_guangfa);
        result.put("信用社(需要密码保护)", R.drawable.platform_icon_xinyongshe);
        result.put("邮政储蓄(需要密码保护)", R.drawable.platform_icon_youzheng);
        result.put("本地银行(需要密码保护)", R.drawable.platform_icon_localbank);
        result.put("手机游戏", R.drawable.platform_icon_mobilegame);
        result.put("电脑游戏", R.drawable.platform_icon_pcgame);
        result.put("PS游戏", R.drawable.platform_icon_psgame);
        result.put("VR游戏", R.drawable.platform_icon_vrgame);
        result.put("谷歌", R.drawable.platform_icon_google);
        result.put("百度", R.drawable.platform_icon_baidu);
        result.put("Facebook", R.drawable.platform_icon_facebook);
        result.put("Twitter", R.drawable.platform_icon_twitter);
        result.put("新浪", R.drawable.platform_icon_sina);
        result.put("美团", R.drawable.platform_icon_meituan);
        result.put("饿了么", R.drawable.platform_icon_eleme);
        result.put("58同城", R.drawable.platform_icon_58);
        result.put("其它", R.drawable.platform_icon_other);
        return result.get(name);
    }
}
