package com.autox.password.frames;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


import com.autox.password.PwdSetActivity;
import com.autox.password.PwdVerifyActivity;
import com.autox.password.R;
import com.autox.password.event.entity.DbChanged;
import com.autox.password.localdata.database.DbHelper;
import com.autox.password.localdata.database.items.PwdItem;
import com.autox.password.localdata.sharedprefs.SharedPrefKeys;
import com.autox.password.localdata.sharedprefs.SharedPrefUtils;
import com.autox.password.views.ItemViewList;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MeFrameLayout extends Fragment
{
    private View mRoot;
    private ItemViewList mClearView;
    private Switch mClearPwdSwitch;
    private static final int REQUEST_SET_PWD_CODE = 1000;
    private static final int REQUEST_VERIFY_PWD_CODE_DELETE = 1001;
    private static final int REQUEST_VERIFY_PWD_CODE_OPEN_CLEARACCOUNT = 1002;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.frame_me, null);
        mClearView = mRoot.findViewById(R.id.item_clear);
        mClearPwdSwitch = mRoot.findViewById(R.id.pwd_switch);
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindEvents();
    }

    private void bindEvents() {

        mClearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.equals(SharedPrefUtils.getString(SharedPrefKeys.KEY_PWD, ""), "")){
                    PwdVerifyActivity.startForResult(MeFrameLayout.this, REQUEST_VERIFY_PWD_CODE_DELETE);
                    return;
                }
                showConfirmDeleteDialog();
            }
        });
        mClearPwdSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean currentChecked = mClearPwdSwitch.isChecked();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String localPwd = SharedPrefUtils.getString(SharedPrefKeys.KEY_PWD, "");
                    if (currentChecked && TextUtils.isEmpty(localPwd)) {
                        PwdSetActivity.startForResult(MeFrameLayout.this, REQUEST_SET_PWD_CODE);
                        return true;
                    }
                    if (!currentChecked && !TextUtils.isEmpty(localPwd)) {
                        PwdVerifyActivity.startForResult(MeFrameLayout.this, REQUEST_VERIFY_PWD_CODE_OPEN_CLEARACCOUNT);
                        return true;
                    }
                }
                return false;
            }
        });
        mClearPwdSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPrefUtils.setBoolean(SharedPrefKeys.KEY_ENABLE_ACCOUNT_MASK, !isChecked);
            }
        });
        mClearPwdSwitch.setChecked(!SharedPrefUtils.getBoolean(SharedPrefKeys.KEY_ENABLE_ACCOUNT_MASK, false));
    }

    private void showConfirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).
                setTitle("确认删除所有数据？").
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SET_PWD_CODE:
                if (resultCode == PwdSetActivity.RESULT_OK) {
                    mClearPwdSwitch.setChecked(false);
                }
                break;
            case REQUEST_VERIFY_PWD_CODE_DELETE:
                if (resultCode == PwdVerifyActivity.RESULT_OK) {
                    showConfirmDeleteDialog();
                }
                break;
            case REQUEST_VERIFY_PWD_CODE_OPEN_CLEARACCOUNT:
                if (resultCode == PwdVerifyActivity.RESULT_OK) {
                    mClearPwdSwitch.setChecked(true);
                }
                break;
        }
    }
}
