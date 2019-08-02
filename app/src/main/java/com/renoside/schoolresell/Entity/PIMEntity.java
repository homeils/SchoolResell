package com.renoside.schoolresell.Entity;

public class PIMEntity {

    public static final int PIM_ITEM = 4001;
    public static final int PIM_SPAN = 4002;
    public static final int PIM_LAST = 4003;
    public static final int PIM_QUIT = 4004;

    public int itemType;
    private Object pimImg;
    private String pimTitle;

    public PIMEntity(int itemType) {
        this.itemType = itemType;
    }

    public Object getPimImg() {
        return pimImg;
    }

    public void setPimImg(Object pimImg) {
        this.pimImg = pimImg;
    }

    public String getPimTitle() {
        return pimTitle;
    }

    public void setPimTitle(String pimTitle) {
        this.pimTitle = pimTitle;
    }

}
