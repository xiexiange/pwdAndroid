package com.autox.password.views.statusbar;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class StatusBarPlaceHolderView extends View {
    private static boolean INIT = false;
    private static int STATUS_BAR_HEIGHT = 50;
    private static final String STATUS_BAR_DEF_PACKAGE = "android";
    private static final String STATUS_BAR_DEF_TYPE = "dimen";
    private static final String STATUS_BAR_NAME = "status_bar_height";

    public StatusBarPlaceHolderView(Context context) {
        super(context);
    }

    public StatusBarPlaceHolderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusBarPlaceHolderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int statusBarHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarHeight = getStatusBarHeight(getContext());
        }
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), statusBarHeight);
    }


    public static synchronized int getStatusBarHeight(Context context) {
        if (!INIT) {
            int resourceId = context.getResources().getIdentifier(STATUS_BAR_NAME, STATUS_BAR_DEF_TYPE, STATUS_BAR_DEF_PACKAGE);
            if (resourceId > 0) {
                STATUS_BAR_HEIGHT = context.getResources().getDimensionPixelSize(resourceId);
                INIT = true;
            }
        }

        return STATUS_BAR_HEIGHT;
    }
}
