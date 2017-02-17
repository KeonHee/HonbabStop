package com.landvibe.android.honbabstop.base.domain;

/**
 * Created by user on 2017-02-15.
 */

public class ChatMessage {

    public final static int TYPE_MY_MESSAGE=1000;
    public final static int TYPE_OTHER_MESSAGE=1001;
    public final static int TYPE_ENTER=1002;


    /* 메세지 정보 */
    private String message;
    private long sendTimeStamp;
    private int type;

    /* 개인 정보 */
    private String uid;
    private String name;
    private String profileUrl;



    public ChatMessage() {
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSendTimeStamp() {
        return sendTimeStamp;
    }

    public void setSendTimeStamp(long sendTimeStamp) {
        this.sendTimeStamp = sendTimeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
