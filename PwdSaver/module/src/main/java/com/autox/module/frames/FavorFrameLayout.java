package com.autox.module.frames;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autox.base.PrefUtil;
import com.autox.module.AddActivity;
import com.autox.module.PlatformListActivity;
import com.autox.module.entities.DbChanged;
import com.autox.module.localdata.database.DbHelper;
import com.autox.module.localdata.database.items.PwdItem;
import com.autox.module.localdata.sharedprefs.SharedPrefKeys;
import com.autox.module.util.MaskUtil;
import com.autox.module.view.EventTextView;
import com.autox.pwd_module.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FavorFrameLayout extends Fragment
{
    private View mRoot;
    private RecyclerView mRecyclerView;
    private List<PwdItem> mPwdList = new ArrayList<>();
    private FavorAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.frame_favor, null);
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        bindEvents();
    }

    private void initViews() {
        mRecyclerView = mRoot.findViewById(R.id.favor_recycler);
        mPwdList = DbHelper.getInstance().getFavorList();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new FavorAdapter();
        mRecyclerView.setAdapter(mAdapter);
        if (mPwdList.size() == 0) {
            mRoot.findViewById(R.id.empty_wrapper).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void bindEvents() {
    }

    private class FavorAdapter extends RecyclerView.Adapter<FavorHolder> {

        @NonNull
        @Override
        public FavorHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_favor_list, viewGroup, false);
            return new FavorHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FavorHolder holder, int position) {

            final PwdItem tmpItem = mPwdList.get(position);
            final String platform = tmpItem.platform();
            final String fullAccount = tmpItem.account();
            String account = fullAccount;
            if (PrefUtil.getBoolean(SharedPrefKeys.KEY_ENABLE_ACCOUNT_MASK, false)) {
                account = MaskUtil.mask(fullAccount);
            }
            holder.platformTv.setText(platform);
            holder.accountTv.setText(account);
            holder.icon.setImageResource(PlatformListActivity.getDrawableIdByName(platform));

            holder.mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddActivity.start(getActivity(), tmpItem);
                }
            });
            holder.favor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle("取消收藏")
                            .setMessage("确认将改条信息从\"收藏\"中移除？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DbHelper.getInstance().like(tmpItem, false);
                                    EventBus.getDefault().post(new DbChanged());
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

        @Override
        public int getItemCount() {
            return mPwdList.size();
        }
    }

    private class FavorHolder extends RecyclerView.ViewHolder {

        private View mRoot;
        private TextView platformTv;
        private TextView accountTv;
        private ImageView icon;
        private EventTextView delete;
        private TextView favor;
        public FavorHolder(@NonNull View itemView) {
            super(itemView);
            mRoot = itemView;
            platformTv = itemView.findViewById(R.id.item_category_platform);
            accountTv = itemView.findViewById(R.id.item_category_account);
            icon = itemView.findViewById(R.id.add_icon);
            delete = itemView.findViewById(R.id.item_category_delete);
            favor = itemView.findViewById(R.id.item_favor);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeData(DbChanged changed) {
        mPwdList = DbHelper.getInstance().getFavorList();
        mAdapter.notifyDataSetChanged();
        if (mPwdList.size() == 0) {
            mRoot.findViewById(R.id.empty_wrapper).setVisibility(View.VISIBLE);
        } else {
            mRoot.findViewById(R.id.empty_wrapper).setVisibility(View.GONE);
        }
    }
}
