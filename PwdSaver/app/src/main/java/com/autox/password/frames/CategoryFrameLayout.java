package com.autox.password.frames;

import android.app.Activity;
import android.content.Context;
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

import com.autox.password.CategoryListActivity;
import com.autox.password.R;
import com.autox.password.event.entity.DbChanged;
import com.autox.password.localdata.database.DbHelper;
import com.autox.password.localdata.database.items.PwdItem;
import com.autox.password.views.RefreshScrollView;
import com.autox.password.views.recyclerviews.entities.ListEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class CategoryFrameLayout extends Fragment implements RefreshScrollView.RefreshListener
{

    private View mRoot;
    private RecyclerView mRecyclerView;
    ArrayList<ListEntity> mEntities = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.frame_category, null);
        mRecyclerView = mRoot.findViewById(R.id.category_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
        return mRoot;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    private class RecyclerViewAdapter extends RecyclerView.Adapter {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_category_view, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            String name = mEntities.get(position).name();
            ((RecyclerViewHolder)holder).titleTV.setText(name);
            ((RecyclerViewHolder)holder).iconIV.setImageResource(mEntities.get(position).drawableId());
            List<PwdItem> items = DbHelper.getInstance().getPwdSizeByType(name);
            ((RecyclerViewHolder)holder).numberTV.setText("" + items.size());
        }

        @Override
        public int getItemCount() {
            return mEntities.size();
        }

        private class RecyclerViewHolder extends RecyclerView.ViewHolder {
            private ImageView iconIV;
            private TextView titleTV;
            private TextView numberTV;
            public RecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                iconIV = itemView.findViewById(R.id.item_icon);
                titleTV = itemView.findViewById(R.id.item_title);
                numberTV = itemView.findViewById(R.id.item_number);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CategoryListActivity.start(getActivity(), titleTV.getText().toString());
                    }
                });
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initEntities();
    }

    @Override
    public void onDetach() {
        mEntities.clear();
        super.onDetach();
    }

    private void initEntities() {
        mEntities.add(new ListEntity("工作", R.drawable.icon_work));
        mEntities.add(new ListEntity("视频", R.drawable.icon_video));
        mEntities.add(new ListEntity("邮箱", R.drawable.icon_mail));
        mEntities.add(new ListEntity("金融", R.drawable.icon_wallet));
        mEntities.add(new ListEntity("游戏", R.drawable.icon_game));
        mEntities.add(new ListEntity("网址", R.drawable.icon_web));
        mEntities.add(new ListEntity("其它", R.drawable.icon_other));
    }

    @Subscribe (threadMode=ThreadMode.MAIN)
    public void dbChanged(DbChanged changed) {
        mAdapter.notifyDataSetChanged();
    }
}
