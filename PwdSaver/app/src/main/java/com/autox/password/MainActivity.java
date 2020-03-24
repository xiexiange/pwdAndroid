package com.autox.password;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.autox.password.event.entity.EventGoMainPage;
import com.autox.password.event.entity.EventSelectTab;
import com.autox.password.frames.AddFrameLayout;
import com.autox.password.frames.CategoryFrameLayout;
import com.autox.password.frames.MeFrameLayout;
import com.autox.password.huawei.R;
import com.autox.password.views.TabView;
import com.autox.password.views.statusbar.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends HuaweiMainActivity {
    private CategoryFrameLayout mCategoryFrame;
    private AddFrameLayout mAddFrame;
    private MeFrameLayout mMeFrame;
    private String mCurrentTabTitle = "";
    private TabView mCategoryTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_main);
        mCategoryTab = findViewById(R.id.tab_fenlei);
        mCategoryFrame = new CategoryFrameLayout();
        mAddFrame = new AddFrameLayout();
        mMeFrame = new MeFrameLayout();
        EventBus.getDefault().register(this);
        mCategoryTab.performClick();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTabChanged(EventSelectTab tabClicked) {
        Fragment fragment = new Fragment();
        if (mCurrentTabTitle.equals(tabClicked.getText())) {
            return;
        }
        switch (tabClicked.getText()) {
            case "分类":
                fragment = mCategoryFrame;
                break;
            case "添加":
                fragment = mAddFrame;
                break;
            case "我的":
                fragment = mMeFrame;
                break;
        }
        mCurrentTabTitle = tabClicked.getText();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                .commitAllowingStateLoss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goMainPage(EventGoMainPage goMainPage) {
        mCategoryTab.performClick();
    }


}
