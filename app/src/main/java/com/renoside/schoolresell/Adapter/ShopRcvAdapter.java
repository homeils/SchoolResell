package com.renoside.schoolresell.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.renoside.banner.Banner;
import com.renoside.banner.ImageLoader;
import com.renoside.schoolresell.Entity.ShopEntity;
import com.renoside.schoolresell.R;

import java.util.ArrayList;
import java.util.List;

public class ShopRcvAdapter extends BaseQuickAdapter<ShopEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ShopRcvAdapter(List<ShopEntity> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<ShopEntity>() {
            @Override
            protected int getItemType(ShopEntity shopEntity) {
                return shopEntity.itemType;
            }
        });
        getMultiTypeDelegate()
                .registerItemType(ShopEntity.SHOP_BANNER, R.layout.shop_banner)
                .registerItemType(ShopEntity.SHOP_CHANNEL, R.layout.shop_channel)
                .registerItemType(ShopEntity.SHOP_RECOMMEND_HINT, R.layout.shop_hint)
                .registerItemType(ShopEntity.SHOP_RECOMMEND, R.layout.shop_recommend)
                .registerItemType(ShopEntity.SHOP_GOODS_HINT, R.layout.shop_hint)
                .registerItemType(ShopEntity.SHOP_GOODS, R.layout.shop_goods);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShopEntity item) {
        switch (helper.getItemViewType()) {
            case ShopEntity.SHOP_BANNER:
                Banner banner = helper.getView(R.id.shop_banner);
                List<Integer> images = new ArrayList<>();
                List<String> titles = new ArrayList<>();
                images.add(R.drawable.banner_1);
                images.add(R.drawable.banner_2);
                images.add(R.drawable.banner_3);
                images.add(R.drawable.banner_4);
                images.add(R.drawable.banner_5);
                for (int i = 0; i < 5; i++) {
                    titles.add("这是第" + (i + 1) + "个页面");
                }
                banner.setImageLoader(new ImageLoader() {
                    @Override
                    public void conImageLoader(Context context, Object path, ImageView imageView) {
                        Glide.with(context).load(path).into(imageView);
                    }
                });
                banner.setPointSize(20);
                banner.setRelay(2000);
                banner.setTitleColor(0xFFFFFFFF);
                banner.setImages(images);
                banner.setTitles(titles);
                banner.start();
                break;
            case ShopEntity.SHOP_CHANNEL:
                ImageView shopChannelIco = helper.getView(R.id.shop_channel_ico);
                shopChannelIco.setBackgroundResource((Integer) item.getShopImg());
                TextView shopChannelTitle = helper.getView(R.id.shop_channel_title);
                shopChannelTitle.setText(item.getShopTitle());
                break;
            case ShopEntity.SHOP_RECOMMEND_HINT:
                TextView shopRecommendHintLeft = helper.getView(R.id.shop_hint_left);
                shopRecommendHintLeft.setTextColor(mContext.getResources().getColor(R.color.black));
                shopRecommendHintLeft.setText("为您精选");
                TextView shopRecommendHintRight = helper.getView(R.id.shop_hint_right);
                shopRecommendHintRight.setText("换一批");
                break;
            case ShopEntity.SHOP_RECOMMEND:
                Glide.with(mContext).load(item.getShopImg()).into((ImageView) helper.getView(R.id.shop_recommend_img));
                TextView shopRecommendTitle = helper.getView(R.id.shop_recommend_title);
                shopRecommendTitle.setText(item.getShopTitle());
                TextView shopRecommendLikes = helper.getView(R.id.shop_recommend_likes);
                shopRecommendLikes.setText(item.getShopLikes());
                break;
            case ShopEntity.SHOP_GOODS_HINT:
                TextView shopGoodsHintLeft = helper.getView(R.id.shop_hint_left);
                shopGoodsHintLeft.setTextColor(mContext.getResources().getColor(R.color.black));
                shopGoodsHintLeft.setText("全部商品");
                break;
            case ShopEntity.SHOP_GOODS:
                Glide.with(mContext).load(item.getShopImg()).into((ImageView) helper.getView(R.id.shop_goods_img));
                TextView shopGoodsTitle = helper.getView(R.id.shop_goods_title);
                shopGoodsTitle.setText(item.getShopTitle());
                TextView shopGoodsDescription = helper.getView(R.id.shop_goods_description);
                shopGoodsDescription.setText(item.getShopDescription());
                TextView shopGoodsPrice = helper.getView(R.id.shop_goods_price);
                shopGoodsPrice.setText(item.getShopPrice());
                TextView shopGoodsLikes = helper.getView(R.id.shop_goods_likes);
                shopGoodsLikes.setText(item.getShopLikes());
                break;
            default:
                break;
        }
    }

}
