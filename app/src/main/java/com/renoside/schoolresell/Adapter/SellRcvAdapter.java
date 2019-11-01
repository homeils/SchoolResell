package com.renoside.schoolresell.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.renoside.schoolresell.Entity.SellEntity;
import com.renoside.schoolresell.R;

import java.util.List;

public class SellRcvAdapter extends BaseQuickAdapter<SellEntity, BaseViewHolder> {


    public SellRcvAdapter(@Nullable List<SellEntity> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<SellEntity>() {
            @Override
            protected int getItemType(SellEntity sellEntity) {
                return sellEntity.itemType;
            }
        });
        getMultiTypeDelegate()
                .registerItemType(SellEntity.SELL_SMALL_ITEM, R.layout.sell_small_item)
                .registerItemType(SellEntity.SELL_BIG_ITEM, R.layout.sell_big_item)
                .registerItemType(SellEntity.SELL_PIC_ITEM, R.layout.sell_pic_item)
                .registerItemType(SellEntity.SELL_TYPE_ITEM, R.layout.sell_type_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SellEntity item) {
        switch (helper.getItemViewType()) {
            case SellEntity.SELL_SMALL_ITEM:
                helper.setText(R.id.sell_small_hint, item.getSellHint());
                break;
            case SellEntity.SELL_BIG_ITEM:
                helper.setText(R.id.sell_big_hint, item.getSellHint());
                break;
            default:
                break;
        }
    }

}
