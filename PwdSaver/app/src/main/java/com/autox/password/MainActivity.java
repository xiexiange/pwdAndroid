package com.autox.password;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.autox.password.event.entity.EventTabClicked;
import com.autox.password.frames.AddFrameLayout;
import com.autox.password.frames.CategoryFrameLayout;
import com.autox.password.frames.MeFrameLayout;
import com.autox.password.views.TabView;
import com.autox.password.views.statusbar.StatusBarUtil;

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTabChanged(EventTabClicked tabClicked) {
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


}
