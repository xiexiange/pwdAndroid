package com.autox.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_add);
        mType = (TYPE) getIntent().getSerializableExtra(EXTRA_TYPE);
        initViews();
    }

    public static void start(Context context, TYPE type) {
        Intent intent = new Intent(context, AddActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    private void initViews() {
        mIconIV = findViewById(R.id.add_page_detail_icon);
        mTitleTV = findViewById(R.id.add_page_detail_title);
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

}
