package com.renoside.schoolresell.Adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.renoside.schoolresell.Entity.PutEntity;
import com.renoside.schoolresell.R;

import java.util.List;

public class PutRcvAdapter extends BaseQuickAdapter<PutEntity, BaseViewHolder> {

    public PutRcvAdapter(int layoutResId, @Nullable List<PutEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PutEntity item) {
        Glide.with(mContext).load(item.getPutImg()).into((ImageView) helper.getView(R.id.put_item_img));
        helper.setText(R.id.put_item_title, item.getPutTitle());
        helper.setText(R.id.put_item_description, item.getPutDescription());
        helper.setText(R.id.put_item_time, item.getPutTime());
    }

}
