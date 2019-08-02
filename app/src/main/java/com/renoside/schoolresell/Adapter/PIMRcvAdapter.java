package com.renoside.schoolresell.Adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.renoside.schoolresell.Entity.PIMEntity;
import com.renoside.schoolresell.R;

import java.util.List;

public class PIMRcvAdapter extends BaseQuickAdapter<PIMEntity, BaseViewHolder> {

    public PIMRcvAdapter(@Nullable List<PIMEntity> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<PIMEntity>() {
            @Override
            protected int getItemType(PIMEntity pimEntity) {
                return pimEntity.itemType;
            }
        });
        getMultiTypeDelegate()
                .registerItemType(PIMEntity.PIM_ITEM, R.layout.pim_item)
                .registerItemType(PIMEntity.PIM_SPAN, R.layout.pim_span)
                .registerItemType(PIMEntity.PIM_LAST, R.layout.pim_last)
                .registerItemType(PIMEntity.PIM_QUIT, R.layout.pim_quit);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PIMEntity item) {
        switch (helper.getItemViewType()) {
            case PIMEntity.PIM_ITEM:
                ImageView pimItemIco = helper.getView(R.id.pim_item_ico);
                pimItemIco.setBackgroundResource((Integer) item.getPimImg());
                helper.setText(R.id.pim_item_title, item.getPimTitle());
                break;
            case PIMEntity.PIM_SPAN:
                break;
            case PIMEntity.PIM_LAST:
                ImageView pimLastIco = helper.getView(R.id.pim_last_ico);
                pimLastIco.setBackgroundResource((Integer) item.getPimImg());
                helper.setText(R.id.pim_last_title, item.getPimTitle());
                break;
            case PIMEntity.PIM_QUIT:
                break;
            default:
                break;
        }
    }
}
