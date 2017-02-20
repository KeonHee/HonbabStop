package com.landvibe.android.honbabstop.base.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 2017-02-20.
 */

public class SearchDTO{


    @SerializedName("items")
    List<FoodRestaurant> foodRestaurantList;

    public SearchDTO(){};

    public List<FoodRestaurant> getFoodRestaurantList() {
        return foodRestaurantList;
    }

    public void setFoodRestaurantList(List<FoodRestaurant> foodRestaurantList) {
        this.foodRestaurantList = foodRestaurantList;
    }
}
