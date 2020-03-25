package com.autox.module.frames;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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


import com.autox.base.BaseUtil;
import com.autox.base.PrefUtil;
import com.autox.os.OSUtils;
import com.autox.module.PwdSetActivity;
import com.autox.module.PwdVerifyActivity;
import com.autox.module.entities.DbChanged;
import com.autox.module.localdata.database.DbHelper;
import com.autox.module.localdata.database.items.PwdItem;
import com.autox.module.localdata.sharedprefs.SharedPrefKeys;
import com.autox.module.view.ItemViewList;
import com.autox.pwd_module.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MeFrameLayout extends Fragment
{
    private View mRoot;
    private ItemViewList mClearView;
    private ItemViewList mCommitView;
    private ItemViewList mFeedbackView;
    private ItemViewList mShareView;
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
        mCommitView = mRoot.findViewById(R.id.item_commit);
        mFeedbackView = mRoot.findViewById(R.id.item_feedback);
        mShareView = mRoot.findViewById(R.id.item_share);
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (OSUtils.isMiui() || OSUtils.isEmui()) {
            mCommitView.setVisibility(View.VISIBLE);
            mShareView.setVisibility(View.VISIBLE);
        }
        bindEvents();
    }

    private void bindEvents() {

        mClearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.equals(PrefUtil.getString(SharedPrefKeys.KEY_PWD, ""), "")){
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
                    String localPwd = PrefUtil.getString(SharedPrefKeys.KEY_PWD, "");
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
                PrefUtil.setBoolean(SharedPrefKeys.KEY_ENABLE_ACCOUNT_MASK, !isChecked);
            }
        });
        mCommitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * Google Play com.android.vending
                应 用 宝 com.tencent.android.qqdownloader
                360手机助手com.qihoo.appstore
                百度手机助手com.baidu.appsearch
                小米应用商店com.xiaomi.market
                豌 豆 荚com.wandoujia.phoenix2
                华为应用市场com.huawei.appmarket
                淘宝手机助手com.taobao.appcenter
                安卓市场com.hiapk.marketpho
                安智市场cn.goapk.market
                * */
                String marketPkg = "";
                if (OSUtils.isEmui()) {
                    marketPkg = "com.huawei.appmarket";
                } else if (OSUtils.isMiui()) {
                    marketPkg = "com.xiaomi.market";
                }
                launchAppDetail(BaseUtil.getInstance().getImp().getApplicationID(), marketPkg);
            }
        });
        mFeedbackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:xiexiange@yeah.net"));
                data.putExtra(Intent.EXTRA_SUBJECT, "意见反馈");
                data.putExtra(Intent.EXTRA_TEXT, "建议与意见:");
                if (data.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(data);
                } else {
                    Toast.makeText(getActivity(), "未找到可发送邮件的应用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mShareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "找到一款很好用的密码记忆软件，快来和我一起分享喜悦吧：";
                if (OSUtils.isEmui()) {
                    url += "https://appstore.huawei.com/app/C101826205";
                } else if (OSUtils.isMiui()) {
                    url += "http://app.mi.com/details?id=com.autox.password&ref=search";
                }
                Intent data=new Intent(Intent.ACTION_SEND);
                data.setData(Uri.parse("mailto:xiexiange@yeah.net"));
                data.setType("text/plain");
                data.putExtra(Intent.EXTRA_SUBJECT, "分享好应用");
                data.putExtra(Intent.EXTRA_TEXT, url);
                if (data.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(data);
                } else {
                    Toast.makeText(getActivity(), "未找到可分享的软件", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mClearPwdSwitch.setChecked(!PrefUtil.getBoolean(SharedPrefKeys.KEY_ENABLE_ACCOUNT_MASK, false));
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
                        PrefUtil.setString(SharedPrefKeys.KEY_PWD, "");
                        mClearPwdSwitch.setChecked(true);
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

    private void showConfirmRemovePwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).
                setTitle("确认明文显示所有账号？")
                .setMessage("打开开关后，验证密码将被清空，再次关闭时可重新设置")
                .setPositiveButton("明文所有账号", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mClearPwdSwitch.setChecked(true);
                        PrefUtil.setString(SharedPrefKeys.KEY_PWD, "");
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
                    showConfirmRemovePwdDialog();
                }
                break;
        }
    }

    /**
     * 跳转到应用市场app详情界面
     * @param appPkg App的包名
     * @param marketPkg 应用市场包名
     */
    public void launchAppDetail(String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "未找到应用商店", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
