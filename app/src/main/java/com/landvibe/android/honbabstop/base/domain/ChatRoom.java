package com.landvibe.android.honbabstop.base.domain;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017-02-15.
 */

public class ChatRoom {

    /**
     * 채팅방 정보
     */

    /* key */
    private String id;

    private String title;

    /* 개설 시간 */
    private long startTimeStamp;
    private long endTimeStamp;

    /* 만남 시간 */
    private long contactTime;

    /* 참가 인원 */
    private int currentPeople;
    private int maxPeople;


    /* 음식 정보 & 만남 장소*/
    private String foodImageUrl; /* Title Image */
    private String foodName;

    private String foodTitle;
    private String foodCategory;
    private String foodDescription;
    private String foodTelephone;
    private String address;
    private String roadAddress;
    private long locationX;
    private long locationY;
    /**
     * <title>조선옥</title>
     <link />
     <category>한식&gt;육류,고기요리</category>
     <description>연탄불 한우갈비 전문점.</description>
     <telephone>02-2266-0333</telephone>
     <address>서울특별시 중구 을지로3가 229-1 </address>
     <roadAddress>서울특별시 중구 을지로15길 6-5 </roadAddress>
     <mapx>311277</mapx>
     <mapy>552097</mapy>
     */
    /* 방장 정보 */
    private User header;

    /* 참여자 Uid (방장포함) */
    private List<String> members;

    /* 채팅방 상태 */
    private int status;

    public final static int STATUS_REMAIN=0;
    public final static int STATUS_FULL=1;

    public ChatRoom(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public int getCurrentPeople() {
        return currentPeople;
    }

    public void setCurrentPeople(int currentPeople) {
        this.currentPeople = currentPeople;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getFoodImageUrl() {
        return foodImageUrl;
    }

    public void setFoodImageUrl(String foodImageUrl) {
        this.foodImageUrl = foodImageUrl;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public User getHeader() {
        return header;
    }

    public void setHeader(User header) {
        this.header = header;
    }

    public long getContactTime() {
        return contactTime;
    }

    public void setContactTime(long contactTime) {
        this.contactTime = contactTime;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFoodTitle() {
        return foodTitle;
    }

    public void setFoodTitle(String foodTitle) {
        this.foodTitle = foodTitle;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getFoodTelephone() {
        return foodTelephone;
    }

    public void setFoodTelephone(String foodTelephone) {
        this.foodTelephone = foodTelephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public long getLocationX() {
        return locationX;
    }

    public void setLocationX(long locationX) {
        this.locationX = locationX;
    }

    public long getLocationY() {
        return locationY;
    }

    public void setLocationY(long locationY) {
        this.locationY = locationY;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
