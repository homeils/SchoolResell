package com.renoside.schoolresell.Bean;

import java.math.BigDecimal;
import java.util.List;

public class GoodsInfo {

    /**
     * goodsLikes : 0
     * sellerId : 30d23e08ed594aa
     * goodsAddress : 仪征
     * goodsId : a03e0175db074a9
     * goodsPrice : 768
     * goodsPhone : 15050770063
     * goodsImgs : [{"goodsImg":"sm.ms"},{"goodsImg":"renoside.cn"}]
     * goodsName : 小喵咪
     * goodsDescription : 这是一只测试喵咪...
     */

    private int goodsLikes;
    private String sellerId;
    private String goodsAddress;
    private String goodsId;
    private BigDecimal goodsPrice;
    private String goodsPhone;
    private String goodsName;
    private String goodsDescription;
    private List<GoodsImgsBean> goodsImgs;

    public int getGoodsLikes() {
        return goodsLikes;
    }

    public void setGoodsLikes(int goodsLikes) {
        this.goodsLikes = goodsLikes;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getGoodsAddress() {
        return goodsAddress;
    }

    public void setGoodsAddress(String goodsAddress) {
        this.goodsAddress = goodsAddress;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsPhone() {
        return goodsPhone;
    }

    public void setGoodsPhone(String goodsPhone) {
        this.goodsPhone = goodsPhone;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public List<GoodsImgsBean> getGoodsImgs() {
        return goodsImgs;
    }

    public void setGoodsImgs(List<GoodsImgsBean> goodsImgs) {
        this.goodsImgs = goodsImgs;
    }

    public static class GoodsImgsBean {
        /**
         * goodsImg : sm.ms
         */

        private String goodsImg;

        public String getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(String goodsImg) {
            this.goodsImg = goodsImg;
        }
    }

    @Override
    public String toString() {
        return "GoodsInfo{" +
                "goodsLikes=" + goodsLikes +
                ", sellerId='" + sellerId + '\'' +
                ", goodsAddress='" + goodsAddress + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsPhone='" + goodsPhone + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsDescription='" + goodsDescription + '\'' +
                ", goodsImgs=" + goodsImgs +
                '}';
    }
}
