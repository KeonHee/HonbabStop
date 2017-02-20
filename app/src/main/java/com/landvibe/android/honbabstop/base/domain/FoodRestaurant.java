package com.landvibe.android.honbabstop.base.domain;

/**
 * Created by user on 2017-02-20.
 */

public class FoodRestaurant {

    String title;
    String link;
    String category;
    String description;
    String telephone;
    String address;
    String roadAddress;
    long mapx;
    long mapy;


    public FoodRestaurant() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public long getMapx() {
        return mapx;
    }

    public void setMapx(long mapx) {
        this.mapx = mapx;
    }

    public long getMapy() {
        return mapy;
    }

    public void setMapy(long mapy) {
        this.mapy = mapy;
    }
}
