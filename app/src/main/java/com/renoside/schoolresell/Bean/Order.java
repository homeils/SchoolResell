package com.renoside.schoolresell.Bean;

import java.math.BigDecimal;
import java.util.List;

public class Order {

    private List<GoodsBean> goods;

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * goodsLikes : 0
         * goodsStatus : 1002
         * goodsId : b38e1137bd82480
         * goodsPrice : 668.12
         * goodsImgs : [{"goodsImg":"qq.com"},{"goodsImg":"renoside.cn"},{"goodsImg":"sm.ms"}]
         * goodsName : 测试商品
         * goodsDescription : 这是一只测试商品...
         */

        private int goodsLikes;
        private String sellerId;
        private String goodsPhone;
        private String goodsAddress;
        private int goodsStatus;
        private String goodsId;
        private BigDecimal goodsPrice;
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

        public String getGoodsPhone() {
            return goodsPhone;
        }

        public void setGoodsPhone(String goodsPhone) {
            this.goodsPhone = goodsPhone;
        }

        public String getGoodsAddress() {
            return goodsAddress;
        }

        public void setGoodsAddress(String goodsAddress) {
            this.goodsAddress = goodsAddress;
        }
        public int getGoodsStatus() {
            return goodsStatus;
        }

        public void setGoodsStatus(int goodsStatus) {
            this.goodsStatus = goodsStatus;
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
             * goodsImg : qq.com
             */

            private String goodsImg;

            public String getGoodsImg() {
                return goodsImg;
            }

            public void setGoodsImg(String goodsImg) {
                this.goodsImg = goodsImg;
            }
        }
    }
}
