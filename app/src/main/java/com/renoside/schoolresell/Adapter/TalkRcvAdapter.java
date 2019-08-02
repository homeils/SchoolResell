package com.renoside.schoolresell.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.renoside.schoolresell.Entity.TalkEntity;
import com.renoside.schoolresell.R;

import java.util.List;

public class TalkRcvAdapter extends BaseQuickAdapter<TalkEntity, BaseViewHolder> {

    public TalkRcvAdapter(@Nullable List<TalkEntity> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<TalkEntity>() {
            @Override
            protected int getItemType(TalkEntity talkEntity) {
                return talkEntity.itemType;
            }
        });
        getMultiTypeDelegate()
                .registerItemType(TalkEntity.TALK_RECEIVED, R.layout.talk_item_left)
                .registerItemType(TalkEntity.TALK_SEND, R.layout.talk_item_right);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TalkEntity item) {
        switch (helper.getItemViewType()) {
            case TalkEntity.TALK_RECEIVED:
                helper.setText(R.id.talk_left_msg, item.getTalkMsg());
                break;
            case TalkEntity.TALK_SEND:
                helper.setText(R.id.talk_right_msg, item.getTalkMsg());
                break;
            default:
                break;
        }
    }

}
