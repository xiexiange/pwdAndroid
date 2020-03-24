package com.autox.password.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.autox.password.huawei.R;
import com.autox.password.event.entity.EventSelectTab;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TabView extends ConstraintLayout {

    TextView mTitleTv;
    ImageView mImageView;
    private Drawable drawableSelected;
    private Drawable drawableUnselected;
    public TabView(Context context) {
        super(context);
        initViews(context, null, -1);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs, -1);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs, defStyleAttr);
    }

    private void initViews(Context context, AttributeSet attrs, int defStyleAttr) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_tab, this);
        mTitleTv = root.findViewById(R.id.tab_title);
        mImageView = root.findViewById(R.id.tab_image);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabView);
            String title = typedArray.getString(R.styleable.TabView_title);
            drawableSelected = typedArray.getDrawable(R.styleable.TabView_image_selected);
            drawableUnselected = typedArray.getDrawable(R.styleable.TabView_image_default);
            mTitleTv.setText(title);
            mImageView.setBackground(drawableUnselected);
        }
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventSelectTab(mTitleTv.getText().toString()));
            }
        });
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onResetTab(EventSelectTab eventSelectTab) {
        setSelected(false);
        mTitleTv.setTextColor(Color.parseColor("#333333"));
        mImageView.setBackground(drawableUnselected);
        if (mTitleTv.getText().equals(eventSelectTab.getText())) {
            mTitleTv.setTextColor(Color.parseColor("#ffffff"));
            mImageView.setBackground(drawableSelected);
            setSelected(true);
        }
    }

}
