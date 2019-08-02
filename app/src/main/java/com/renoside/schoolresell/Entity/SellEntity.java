package com.renoside.schoolresell.Entity;

public class SellEntity {

    public static final int SELL_SMALL_ITEM = 6001;
    public static final int SELL_BIG_ITEM = 6002;
    public static final int SELL_PIC_ITEM = 6003;
    public int itemType;

    private String sellhint;

    public SellEntity(int itemType) {
        this.itemType = itemType;
    }

    public String getSellhint() {
        return sellhint;
    }

    public void setSellhint(String sellhint) {
        this.sellhint = sellhint;
    }

}
