package com.landvibe.android.honbabstop.search.presenter;

import android.app.Activity;
import android.util.Log;

import com.landvibe.android.honbabstop.GlobalApp;
import com.landvibe.android.honbabstop.base.domain.FoodRestaurant;
import com.landvibe.android.honbabstop.base.domain.TranscoordDTO;
import com.landvibe.android.honbabstop.search.model.SearchModel;

import java.util.List;

/**
 * Created by user on 2017-02-22.
 */

public class SearchPresenterImpl implements SearchPresenter.Presenter,
        SearchModel.TransCoordCallback, SearchModel.ModelDataChange{

    private static final String TAG="SearchPresenterImpl";

    private SearchPresenter.View view;

    private SearchModel mSearchModel;

    private FoodRestaurant foodRestaurant;

    @Override
    public void attachView(SearchPresenter.View view, Activity activity) {
        this.view=view;

        mSearchModel = new SearchModel();
        mSearchModel.setOnChangeListener(this);
        mSearchModel.setOnTransListener(this);

    }

    @Override
    public void detachView() {
        this.view=null;

        mSearchModel.setOnChangeListener(null);
        mSearchModel.setOnTransListener(null);
        mSearchModel=null;
    }

    @Override
    public void searchLocation(String query) {
        if(mSearchModel!=null){
            mSearchModel.searchRestaurant(query);
        }
    }

    @Override
    public void loadCoord(int position) {

        if(mSearchModel!=null){
            List<FoodRestaurant> foodRestaurantList = mSearchModel.getFoodRestaurantList();
            if(foodRestaurantList!=null && foodRestaurantList.size()>=0){
                foodRestaurant = foodRestaurantList.get(position);

                // 좌표계 변환
                mSearchModel.transCoord(foodRestaurant.getMapx(),foodRestaurant.getMapy());
            }
        }
    }


    /**
     * SearchModel.ModelDataChange
     */
    @Override
    public void onSuccess(List<FoodRestaurant> list) {
        Log.d(TAG,"list size : " + list.size());
        String[] suggestions = new String[list.size()];
        for(int i = 0; i <list.size();i++){
            StringBuffer restaurant = new StringBuffer();
            restaurant.append(list.get(i).getTitle().replace("<b>","").replace("</b>",""));
            restaurant.append("-");
            restaurant.append(list.get(i).getAddress());

            Log.d(TAG,"restaurant name : " +restaurant.toString());

            suggestions[i]=restaurant.toString();
        }
        view.showSuggestions(suggestions);
    }

    @Override
    public void onFailure() {

    }

    /**
     * SearchModel.TransCoordCallback
     */
    @Override
    public void onTrans(TranscoordDTO transcoordDTO) {
        if(foodRestaurant==null){
            return;
        }

        foodRestaurant.setLat(transcoordDTO.getLat());
        foodRestaurant.setLon(transcoordDTO.getLon());

        view.moveToAddChatActivity();

        GlobalApp.getGlobalApplicationContext().changeModel(foodRestaurant);
    }

    @Override
    public void onTransFailure() {

    }

}
