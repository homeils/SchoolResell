package com.renoside.schoolresell.Adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.renoside.schoolresell.Entity.MsgEntity;
import com.renoside.schoolresell.R;

import java.util.List;

public class MsgRcvAdapter extends BaseQuickAdapter<MsgEntity, BaseViewHolder> {

    public MsgRcvAdapter(int layoutResId, @Nullable List<MsgEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MsgEntity item) {
        Glide.with(mContext).load(item.getMsgImg()).into((ImageView) helper.getView(R.id.msg_item_img));
        helper.setText(R.id.msg_item_title, item.getMsgTitle());
        helper.setText(R.id.msg_item_description, item.getMsgInfo());
        helper.setText(R.id.msg_item_time, item.getMsgTime());
        if (item.getMsgNum().equals("0")) {
            helper.getView(R.id.msg_item_status).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.msg_item_status).setVisibility(View.VISIBLE);
            helper.setText(R.id.msg_item_status, item.getMsgNum());
        }
    }

}
