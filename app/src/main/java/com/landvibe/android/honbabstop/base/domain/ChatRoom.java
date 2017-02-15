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

    /* 만남 장소 */
    private String locationStr;
    private double locationLon;
    private double locationLat;

    /* 음식 정보 */
    private String foodName;
    private String foodImageUrl; /* Title Image */
    private String foodRestaurant;

    /* 방장 정보 */
    private User header;

    /* 참여자 Uid (방장포함) */
    private List<String> members;


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        result.put("startTimeStamp", startTimeStamp);
        result.put("endTimeStamp", endTimeStamp);
        result.put("currentPeople", currentPeople);
        result.put("maxPeople", maxPeople);
        result.put("locationStr", locationStr);
        result.put("locationLon", locationLon);
        result.put("locationLat", locationLat);
        result.put("contactTime", contactTime);
        result.put("foodName", foodName);
        result.put("foodImageUrl", foodImageUrl);
        result.put("foodRestaurant", foodRestaurant);
        result.put("header", header);
        result.put("members", members);

        return result;
    }


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

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
