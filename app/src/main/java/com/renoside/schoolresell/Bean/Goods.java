package com.renoside.schoolresell.Bean;

import java.math.BigDecimal;
import java.util.List;

public class Goods {

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
         * sellerId : 30d23e08ed594aa
         * goodsStatus : 1001
         * goodsId : a03e0175db074a9
         * goodsPrice : 768
         * goodsImgs : [{"goodsImg":"sm.ms"},{"goodsImg":"renoside.cn"}]
         * goodsName : 小喵咪
         * goodsDescription : 这是一只测试喵咪...
         */

        private int goodsLikes;
        private String sellerId;
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
             * goodsImg : sm.ms
             */

            private String goodsImg;

            public String getGoodsImg() {
                return goodsImg;
            }

            public void setGoodsImg(String goodsImg) {
                this.goodsImg = goodsImg;
            }

            @Override
            public String toString() {
                return "GoodsImgsBean{" +
                        "goodsImg='" + goodsImg + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "GoodsBean{" +
                    "goodsLikes=" + goodsLikes +
                    ", sellerId='" + sellerId + '\'' +
                    ", goodsStatus=" + goodsStatus +
                    ", goodsId='" + goodsId + '\'' +
                    ", goodsPrice=" + goodsPrice +
                    ", goodsName='" + goodsName + '\'' +
                    ", goodsDescription='" + goodsDescription + '\'' +
                    ", goodsImgs=" + goodsImgs +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goods=" + goods +
                '}';
    }
}
