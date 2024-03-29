package com.renoside.schoolresell.Adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.renoside.schoolresell.Entity.GoodsEntity;
import com.renoside.schoolresell.R;

import java.util.ArrayList;
import java.util.List;

public class GoodsRcvAdapter extends BaseQuickAdapter<GoodsEntity, BaseViewHolder> {

    public GoodsRcvAdapter(@Nullable List<GoodsEntity> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<GoodsEntity>() {
            @Override
            protected int getItemType(GoodsEntity goodsEntity) {
                return goodsEntity.itemType;
            }
        });
        getMultiTypeDelegate()
                .registerItemType(GoodsEntity.GOODS_NAME_PRICE, R.layout.goods_name_price)
                .registerItemType(GoodsEntity.GOODS_DESCRIPTION, R.layout.goods_description)
                .registerItemType(GoodsEntity.GOODS_IMG, R.layout.goods_img)
                .registerItemType(GoodsEntity.GOODS_LIST, R.layout.goods_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GoodsEntity item) {
        switch (helper.getItemViewType()) {
            case GoodsEntity.GOODS_NAME_PRICE:
                helper.setText(R.id.goods_item_name, item.getGoodsName());
                helper.setText(R.id.goods_item_price, item.getGoodsPrice());
                break;
            case GoodsEntity.GOODS_DESCRIPTION:
                helper.setText(R.id.goods_item_description, item.getGoodsDescription());
                break;
            case GoodsEntity.GOODS_IMG:
                Glide.with(mContext).load(item.getGoodsImg()).into((ImageView) helper.getView(R.id.goods_img));
                break;
            case GoodsEntity.GOODS_LIST:
                Glide.with(mContext).load(item.getGoodsImg()).into((ImageView) helper.getView(R.id.goods_list_img));
                helper.setText(R.id.goods_list_title, item.getGoodsName());
                helper.setText(R.id.goods_list_description, item.getGoodsDescription());
                helper.setText(R.id.goods_list_price, item.getGoodsPrice());
                helper.setText(R.id.goods_list_likes, item.getGoodsLikes());
                break;
            default:
                break;
        }
    }

}
