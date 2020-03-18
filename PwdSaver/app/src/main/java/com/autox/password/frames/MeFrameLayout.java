package com.autox.password.frames;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.autox.password.R;
import com.autox.password.views.ItemViewList;

public class MeFrameLayout extends Fragment
{
    private View mRoot;
    private ItemViewList mLoginView;
    private ItemViewList mClearView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.frame_me, null);
        mLoginView = mRoot.findViewById(R.id.item_login);
        mClearView = mRoot.findViewById(R.id.item_clear);
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindEvents();
    }

    private void bindEvents() {
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "要登录", Toast.LENGTH_SHORT).show();
            }
        });
        mClearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "已删除", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
