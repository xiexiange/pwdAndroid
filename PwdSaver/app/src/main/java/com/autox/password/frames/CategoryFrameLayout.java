package com.autox.password.frames;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.autox.password.R;
import com.autox.password.views.RefreshScrollView;
import com.autox.password.views.recyclerviews.entities.ListEntity;

import java.util.ArrayList;

public class CategoryFrameLayout extends Fragment implements RefreshScrollView.RefreshListener
{

    private View mRoot;
    private RecyclerView mRecyclerView;
    ArrayList<ListEntity> mEntities = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.frame_category, null);
        mRecyclerView = mRoot.findViewById(R.id.category_recycler_view);
        initEntities();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, true));
        RecyclerView.Adapter adapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(adapter);
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

    private class RecyclerViewAdapter extends RecyclerView.Adapter {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_category_view, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((RecyclerViewHolder)holder).titleTV.setText(mEntities.get(position).name());
            ((RecyclerViewHolder)holder).iconIV.setImageResource(mEntities.get(position).drawableId());
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
            }
        }
    }

    private void initEntities() {
        mEntities.add(new ListEntity("工作", R.drawable.icon_work));
        mEntities.add(new ListEntity("视频", R.drawable.icon_video));
        mEntities.add(new ListEntity("邮箱", R.drawable.icon_mail));
        mEntities.add(new ListEntity("金融", R.drawable.icon_wallet));
        mEntities.add(new ListEntity("游戏", R.drawable.icon_game));
        mEntities.add(new ListEntity("网址", R.drawable.icon_web));
    }
}
