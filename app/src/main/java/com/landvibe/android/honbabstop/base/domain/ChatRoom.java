package com.landvibe.android.honbabstop.base.domain;

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

    private long startTimeStamp;
    private long endTimeStamp;

    /* 참가 인원 */
    private int currentPeople;
    private int maxPeople;

    /* 만남 장소 */
    private String locationStr;
    private double locationLon;
    private double locationLat;

    /* 만남 시간 */
    private long contactTime;

    /* 음식 정보 */
    private String foodName;
    private String foodImageUrl; /* Title Image */
    private String foodRestaurant;

    /* 방장 정보 */
    private User header;

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

    public String getLocationStr() {
        return locationStr;
    }

    public void setLocationStr(String locationStr) {
        this.locationStr = locationStr;
    }

    public double getLocationLon() {
        return locationLon;
    }

    public void setLocationLon(double locationLon) {
        this.locationLon = locationLon;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImageUrl() {
        return foodImageUrl;
    }

    public void setFoodImageUrl(String foodImageUrl) {
        this.foodImageUrl = foodImageUrl;
    }

    public String getFoodRestaurant() {
        return foodRestaurant;
    }

    public void setFoodRestaurant(String foodRestaurant) {
        this.foodRestaurant = foodRestaurant;
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
}
