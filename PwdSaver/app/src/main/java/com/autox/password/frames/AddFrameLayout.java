package com.autox.password.frames;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.autox.password.R;
import com.autox.password.views.recyclerviews.entities.ListEntity;

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
                        Toast.makeText(getActivity(), "clicked: " + titleTV.getText(), Toast.LENGTH_SHORT).show();
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
