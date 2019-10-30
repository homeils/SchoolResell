package com.renoside.schoolresell.Adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.renoside.schoolresell.Entity.LikesEntity;
import com.renoside.schoolresell.R;

import java.util.List;

public class LikesRcvAdapter extends BaseQuickAdapter<LikesEntity, BaseViewHolder> {

    public LikesRcvAdapter(int layoutResId, @Nullable List<LikesEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LikesEntity item) {
        Glide.with(mContext).load(item.getLikesImg()).into((ImageView) helper.getView(R.id.likes_item_img));
        helper.setText(R.id.likes_item_title, item.getLikesName());
        helper.setText(R.id.likes_item_description, item.getLikesInfo());
        helper.addOnClickListener(R.id.likes_item_button);
    }
}
