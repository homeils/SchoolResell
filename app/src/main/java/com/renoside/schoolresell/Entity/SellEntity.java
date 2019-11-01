package com.renoside.schoolresell.Entity;

public class SellEntity {

    public static final int SELL_SMALL_ITEM = 6001;
    public static final int SELL_BIG_ITEM = 6002;
    public static final int SELL_PIC_ITEM = 6003;
    public static final int SELL_TYPE_ITEM = 6004;

    public int itemType;

    private String sellHint;

    public SellEntity(int itemType) {
        this.itemType = itemType;
    }

    public String getSellHint() {
        return sellHint;
    }

    public void setSellHint(String sellHint) {
        this.sellHint = sellHint;
    }

}
