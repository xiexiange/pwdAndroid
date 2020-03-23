package com.autox.password.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.autox.password.pro.R;

public class ItemViewList extends ConstraintLayout {
    private TextView mTitleTv;
    private ImageView mImageView;
    public ItemViewList(Context context) {
        super(context);
        init(context, null, -1);
    }

    public ItemViewList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public ItemViewList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_item, this);
        mTitleTv = rootView.findViewById(R.id.item_text);
        mImageView = rootView.findViewById(R.id.item_icon);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemViewList);
            String title = typedArray.getString(R.styleable.ItemViewList_item_title);
            Drawable drawable = typedArray.getDrawable(R.styleable.ItemViewList_image);
            mTitleTv.setText(title);
            mImageView.setBackground(drawable);
        }
    }
}
