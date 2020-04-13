package com.autox.module;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.autox.base.PrefUtil;
import com.autox.module.entities.DbChanged;
import com.autox.module.localdata.database.DbHelper;
import com.autox.module.localdata.database.items.PwdItem;
import com.autox.module.localdata.sharedprefs.SharedPrefKeys;
import com.autox.module.util.MaskUtil;
import com.autox.module.util.ModuleBaseUtil;
import com.autox.module.view.EventTextView;
import com.autox.pwd_module.R;
import com.autox.views.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class PwdSearchActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<PwdItem> mPwdList = new ArrayList<>();
    private SearchAdapter mAdapter;
    private EditText mInputEt;
    private ImageView mBackTv;

    private void initViews() {
        mRecyclerView = findViewById(R.id.search_recycler_view);
        mInputEt = findViewById(R.id.search_edit);
        mBackTv = findViewById(R.id.search_page_back_icon);
        mPwdList = DbHelper.getInstance().getUnDeletedPwdList();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PwdSearchActivity.this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new SearchAdapter();
        mRecyclerView.setAdapter(mAdapter);
        if (mPwdList.size() == 0) {
            findViewById(R.id.empty_wrapper).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_search);
        initViews();
        bindEvents();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void bindEvents() {
        mBackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = mInputEt.getText().toString();
                mPwdList = DbHelper.getInstance().getPwdListBySearch(text);
                EventBus.getDefault().post(new DbChanged());
            }

            @Override
            public void afterTextChanged(Editable s) {
                int start = mInputEt.getSelectionStart();
                int end = mInputEt.getSelectionEnd();
                if (mInputEt.getText().toString().contains("\r") || mInputEt.getText().toString().contains("\n")) {
                    s.delete(start - 1, end);
                    mInputEt.setText(s);
                    mInputEt.setSelection(end - 1);
                }
            }
        });
    }

    private class SearchAdapter extends RecyclerView.Adapter<PwdSearchActivity.FavorHolder> {

        @NonNull
        @Override
        public PwdSearchActivity.FavorHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_favor_list, viewGroup, false);
            return new PwdSearchActivity.FavorHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final PwdSearchActivity.FavorHolder holder, int position) {
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
                    AddActivity.start(PwdSearchActivity.this, tmpItem);
                    ModuleBaseUtil.recordUsage(Constant.USAGE_SEARCH_ITEM_CLICK, "1");
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
            favor.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeData(DbChanged changed) {
        mAdapter.notifyDataSetChanged();
        if (mPwdList.size() == 0) {
            findViewById(R.id.empty_wrapper).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.empty_wrapper).setVisibility(View.GONE);
        }
    }
}
