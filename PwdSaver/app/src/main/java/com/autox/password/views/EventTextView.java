package com.autox.password.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.autox.password.event.entity.EventEditClicked;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventTextView extends TextView {
    public EventTextView(Context context) {
        super(context);
    }

    public EventTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EventTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toggle(EventEditClicked clicked) {
        int visible = getVisibility();
        setVisibility(visible == View.VISIBLE ? View.GONE : View.VISIBLE);
    }
}
