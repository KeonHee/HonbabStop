package com.landvibe.android.honbabstop.base.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2017-02-15.
 */

public class ChatRoom implements Serializable {

    public final static String KEY="chatroom";

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
    private double lat;
    private double lon;
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
    private List<User> members;


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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

}
