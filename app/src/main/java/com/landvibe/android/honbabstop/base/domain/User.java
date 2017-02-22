package com.landvibe.android.honbabstop.base.domain;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017-02-09.
 */

public class User {

    public final static String KAKAO="kakao";
    public final static String FACEBOOK="facebook";
    public final static String GOOGLE="google";

    /* firebase 정보*/
    private String uid;
    private String providerId;

    /* 개인 정보 */
    private String name;
    private int age;
    private String gender;
    private String address;
    private String email;
    private String profileUrl;

    /* 상태, 직업 */
    private String status;
    private String job;

    /* 친구 리스트 */
    private List<String> friendsUid;

    /* Chat room 에서만 사용*/
    private long enteredTime;

    public User() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("providerId", providerId);
        result.put("name", name);
        result.put("age", age);
        result.put("gender", gender);
        result.put("address", address);
        result.put("email", email);
        result.put("profileUrl", profileUrl);
        result.put("status", status);
        result.put("job", job);
        result.put("friendsUid", friendsUid);

        return result;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public List<String> getFriendsUid() {
        return friendsUid;
    }

    public void setFriendsUid(List<String> friendsUid) {
        this.friendsUid = friendsUid;
    }

    public long getEnteredTime() {
        return enteredTime;
    }

    public void setEnteredTime(long enteredTime) {
        this.enteredTime = enteredTime;
    }
}
