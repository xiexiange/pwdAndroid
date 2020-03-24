package com.autox.password;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.autox.password.event.entity.EventGoMainPage;
import com.autox.password.event.entity.EventSelectTab;
import com.autox.password.frames.AddFrameLayout;
import com.autox.password.frames.CategoryFrameLayout;
import com.autox.password.frames.MeFrameLayout;
import com.autox.password.views.TabView;
import com.autox.views.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
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


    private long mFirstBackTime = -1;
    @Override
    public void onBackPressed() {

        // 需要确认的退出
        if (mFirstBackTime == 0 || (System.currentTimeMillis() - mFirstBackTime) > 2000) {
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            mFirstBackTime = System.currentTimeMillis();
            return;
        }

        // 正常退出，重置退出时间
        mFirstBackTime = 0;
        super.onBackPressed();
    }
}
