package com.renoside.schoolresell.Entity;

import java.util.List;

public class GoodsEntity {

    public static final int GOODS_BANNER = 5001;
    public static final int GOODS_NAME_PRICE = 5002;
    public static final int GOODS_DESCRIPTION = 5003;
    public static final int GOODS_LIST = 5004;
    public static final int GOODS_IMG = 5005;

    public int itemType;
    private String goodsId;
    private String sellerId;
    private List<Object> goodsImgs;
    private Object goodsImg;
    private String goodsName;
    private String goodsPrice;
    private String goodsDescription;
    private String goodsLikes;

    public GoodsEntity(int itemType) {
        this.itemType = itemType;
    }

    public int getSpanSize() {
        switch (itemType) {
            case GOODS_BANNER:
                return 2;
            case GOODS_NAME_PRICE:
                return 2;
            case GOODS_DESCRIPTION:
                return 2;
            case GOODS_LIST:
                return 1;
            case GOODS_IMG:
                return 2;
            default:
                return -1;
        }
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public List<Object> getGoodsImgs() {
        return goodsImgs;
    }

    public void setGoodsImgs(List<Object> goodsImgs) {
        this.goodsImgs = goodsImgs;
    }

    public Object getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(Object goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public String getGoodsLikes() {
        return goodsLikes;
    }

    public void setGoodsLikes(String goodsLikes) {
        this.goodsLikes = goodsLikes;
    }

}
