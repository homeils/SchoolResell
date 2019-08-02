package com.renoside.schoolresell.Entity;

public class TalkEntity {

    public static final int TALK_RECEIVED = 7001;
    public static final int TALK_SEND = 7002;
    private String talkMsg;
    public int itemType;

    public TalkEntity(int itemType) {
        this.itemType =itemType;
    }

    public String getTalkMsg() {
        return talkMsg;
    }

    public void setTalkMsg(String talkMsg) {
        this.talkMsg = talkMsg;
    }

}
