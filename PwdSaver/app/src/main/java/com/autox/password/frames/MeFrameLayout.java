package com.autox.password.frames;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.autox.password.R;
import com.autox.password.event.entity.DbChanged;
import com.autox.password.localdata.database.DbHelper;
import com.autox.password.localdata.database.items.PwdItem;
import com.autox.password.views.ItemViewList;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MeFrameLayout extends Fragment
{
    private View mRoot;
    private ItemViewList mBackupView;
    private ItemViewList mClearView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.frame_me, null);
        mBackupView = mRoot.findViewById(R.id.item_backup);
        mClearView = mRoot.findViewById(R.id.item_clear);
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindEvents();
    }

    private void bindEvents() {
        mBackupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "要备份", Toast.LENGTH_SHORT).show();
            }
        });
        mClearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).
                        setTitle("确认删除所有本地和线上数据？").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<PwdItem> lists = DbHelper.getInstance().getPwdList();
                                for (PwdItem item : lists) {
                                    DbHelper.getInstance().setDeleted(item);
                                }
                                EventBus.getDefault().post(new DbChanged());
                                Toast.makeText(getActivity(), "已删除所有数据", Toast.LENGTH_SHORT).show();
                                // todo 可以做个意见反馈
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });
    }
}
