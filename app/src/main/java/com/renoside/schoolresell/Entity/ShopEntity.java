package com.renoside.schoolresell.Entity;

import java.util.List;

public class ShopEntity {

    public static final int SHOP_BANNER = 1001;
    public static final int SHOP_RECOMMEND_HINT = 1002;
    public static final int SHOP_RECOMMEND = 1003;
    public static final int SHOP_GOODS_HINT = 1004;
    public static final int SHOP_GOODS = 1005;
    public static final int SHOP_CHANNEL = 1006;
    public static final int SHOP_ADVERTISEMENT  = 1007;

    /**
     * 布局区分标志
     */
    public int itemType;
    /**
     * 商品ID
     */
    private String shopId;
    /**
     * 轮播图片地址集合
     */
    private List<Integer> shopImgs;
    /**
     * 图片地址
     */
    private Object shopImg;
    /**
     * 轮播标题集合
     */
    private String shopTitles;
    /**
     * 布局标题
     */
    private String shopTitle;
    /**
     * 布局描述
     */
    private String shopDescription;
    /**
     * 布局价格
     */
    private String shopPrice;
    /**
     * 布局收藏人数
     */
    private String shopLikes;

    public ShopEntity(int itemType) {
        this.itemType = itemType;
    }

    public int getSpanSize() {
        switch (itemType) {
            case SHOP_BANNER:
                return 10;
            case SHOP_CHANNEL:
                return 2;
            case SHOP_ADVERTISEMENT:
                return 10;
            case SHOP_RECOMMEND_HINT:
                return 10;
            case SHOP_RECOMMEND:
                return 5;
            case SHOP_GOODS_HINT:
                return 10;
            case SHOP_GOODS:
                return 5;
            default:
                return -1;
        }
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<Integer> getShopImgs() {
        return shopImgs;
    }

    public void setShopImgs(List<Integer> shopImgs) {
        this.shopImgs = shopImgs;
    }

    public Object getShopImg() {
        return shopImg;
    }

    public void setShopImg(Object shopImg) {
        this.shopImg = shopImg;
    }

    public String getShopTitles() {
        return shopTitles;
    }

    public void setShopTitles(String shopTitles) {
        this.shopTitles = shopTitles;
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getShopLikes() {
        return shopLikes;
    }

    public void setShopLikes(String shopLikes) {
        this.shopLikes = shopLikes;
    }
}
