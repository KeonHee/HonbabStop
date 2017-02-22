package com.landvibe.android.honbabstop.AddChat.model;

import android.util.Log;

import com.landvibe.android.honbabstop.base.domain.FoodRestaurant;
import com.landvibe.android.honbabstop.base.domain.SearchDTO;
import com.landvibe.android.honbabstop.base.domain.TranscoordDTO;
import com.landvibe.android.honbabstop.base.network.GeoService;
import com.landvibe.android.honbabstop.base.network.SearchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 2017-02-20.
 */

public class SearchModel {


    private final static String TAG="SearchModel";

    private final static int QUERY_DISPLAY=10;
    private final static int QUERY_START=1;
    private final static String QUERY_SORT_RANDOM="random";
    private final static String QUERY_SORT_COMMENT="comment";

    private List<FoodRestaurant> foodRestaurantList = new ArrayList<>();


    private ModelDataChange mModelDataChange;

    public interface ModelDataChange {
        void onSuccess(List<FoodRestaurant> list);
        void onFailure();
    }

    public void setOnChangeListener(ModelDataChange modelDataChange){
        mModelDataChange=modelDataChange;
    }


    private  TransCoordCallback mTransCoordCallback;

    public interface TransCoordCallback {
        void onTrans(TranscoordDTO transcoordDTO);
        void onTransFailure();
    }

    public void setOnTransListener(TransCoordCallback callback){
        mTransCoordCallback=callback;
    }


    public void searchRestaurant(String query){

        SearchService searchService = SearchService.retrofit.create(SearchService.class);

        Map<String, String> header = new HashMap<>();
        header.put(SearchService.CLIENT_ID,"o8uc6MRnWiJu0aHOOYmf");
        header.put(SearchService.CLIENT_SECRET,"PHqdHD0cHI");

        Call<SearchDTO> call =  searchService.searchRestaurant(
                query,QUERY_DISPLAY,QUERY_START,QUERY_SORT_RANDOM,header
        );

        call.enqueue(callbackListener);

    }

    private Callback<SearchDTO> callbackListener = new Callback<SearchDTO>() {
        @Override
        public void onResponse(Call<SearchDTO> call, Response<SearchDTO> response) {
            if (response.isSuccessful()) {
                foodRestaurantList.clear();
                foodRestaurantList.addAll(response.body().getFoodRestaurantList());

                if(mModelDataChange!=null){
                    mModelDataChange.onSuccess(foodRestaurantList);
                    Log.d(TAG, "onSuccess");
                }
            }else {
                Log.d(TAG, "response error code : "+response.code());
            }
        }

        @Override
        public void onFailure(Call<SearchDTO> call, Throwable t) {
            if (mModelDataChange!=null){
                mModelDataChange.onFailure();
                Log.d(TAG, "onFailure : " + t.toString());
            }
        }
    };


    public void transCoord(long x, long y){

        GeoService geoService = GeoService.retrofit.create(GeoService.class);

        Call<TranscoordDTO> call = geoService.transCoord(
                GeoService.API_KEY,
                GeoService.FROMCOORD,
                GeoService.TOCOORD,
                y,
                x,
                GeoService.OUTPUT
        );

        call.enqueue(transCallbackListener);

    }

    private Callback<TranscoordDTO> transCallbackListener = new Callback<TranscoordDTO>() {
        @Override
        public void onResponse(Call<TranscoordDTO> call, Response<TranscoordDTO> response) {
            if (response.isSuccessful()) {
                TranscoordDTO transcoordDTO = response.body();
                if(mTransCoordCallback!=null){
                    mTransCoordCallback.onTrans(transcoordDTO);
                    Log.d(TAG, "onSuccess");
                }
            }else {
                Log.d(TAG, "response error code : "+response.code());
            }
        }

        @Override
        public void onFailure(Call<TranscoordDTO> call, Throwable t) {
            mTransCoordCallback.onTransFailure();
        }
    };

    public List<FoodRestaurant> getFoodRestaurantList() {
        return foodRestaurantList;
    }
}
