package com.renoside.schoolresell.Adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.renoside.schoolresell.Entity.OrderEntity;
import com.renoside.schoolresell.R;

import java.util.List;

public class OrderRcvAdapter extends BaseQuickAdapter<OrderEntity, BaseViewHolder> {

    public OrderRcvAdapter(int layoutResId, @Nullable List<OrderEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OrderEntity item) {
        Glide.with(mContext).load(item.getOrderImg()).into((ImageView) helper.getView(R.id.order_item_img));
        helper.setText(R.id.order_item_title, item.getOrderTitle());
        helper.setText(R.id.order_item_description, item.getOrderDescription());
        helper.setText(R.id.order_item_status, item.getOrderStatus());
        helper.addOnClickListener(R.id.order_item_operation);
    }

}
