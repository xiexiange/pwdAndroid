package com.autox.password.frames;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.autox.password.R;
import com.autox.password.views.RefreshScrollView;

public class CategoryFrameLayout extends Fragment implements RefreshScrollView.RefreshListener
{
    private View mRoot;
    private RefreshScrollView sv;
    private RelativeLayout headView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.frame_category, null);
        sv = (RefreshScrollView) mRoot.findViewById(R.id.refresh_sv);
        headView = (RelativeLayout) mRoot.findViewById(R.id.head_view);
        sv.setListsner(this);
        sv.setHeadView(headView);
        return mRoot;
    }

    @Override
    public void startRefresh() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void hintChange(String hint) {

    }

    @Override
    public void setWidthX(int x) {

    }
}
