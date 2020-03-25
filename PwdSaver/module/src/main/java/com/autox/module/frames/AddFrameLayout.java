package com.autox.module.frames;

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

import com.autox.module.AddActivity;
import com.autox.module.Constant;
import com.autox.module.view.recyclerviews.entity.ListEntity;
import com.autox.pwd_module.R;

import java.util.ArrayList;

public class AddFrameLayout extends Fragment
{
    private View mRoot;

    private RecyclerView mRecyclerView;
    ArrayList<ListEntity> mEntities = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.frame_add, null);
        mRecyclerView = mRoot.findViewById(R.id.category_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        RecyclerView.Adapter adapter = new AddFrameLayout.RecyclerViewAdapter();
        mRecyclerView.setAdapter(adapter);

        return mRoot;
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_add_view, parent, false);
            return new AddFrameLayout.RecyclerViewAdapter.RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((AddFrameLayout.RecyclerViewAdapter.RecyclerViewHolder)holder).titleTV.setText(mEntities.get(position).name());
            ((AddFrameLayout.RecyclerViewAdapter.RecyclerViewHolder)holder).iconIV.setImageResource(mEntities.get(position).drawableId());
        }

        @Override
        public int getItemCount() {
            return mEntities.size();
        }

        private class RecyclerViewHolder extends RecyclerView.ViewHolder {
            private ImageView iconIV;
            private TextView titleTV;
            public RecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                iconIV = itemView.findViewById(R.id.item_icon);
                titleTV = itemView.findViewById(R.id.item_title);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constant.CATEGORY_TYPE type = Constant.CATEGORY_TYPE.GAME;
                        switch (titleTV.getText().toString()) {
                            case "工作":
                                type = Constant.CATEGORY_TYPE.WORK;
                                break;
                            case "视频":
                                type = Constant.CATEGORY_TYPE.VIDEO;
                                break;
                            case "邮箱":
                                type = Constant.CATEGORY_TYPE.MAIL;
                                break;
                            case "金融":
                                type = Constant.CATEGORY_TYPE.MONEY;
                                break;
                            case "网址":
                                type = Constant.CATEGORY_TYPE.WEB;
                                break;
                            case "其它":
                                type = Constant.CATEGORY_TYPE.OTHER;
                                break;
                        }
                        AddActivity.start(getActivity(), type, "","", "");
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
}
