package com.landvibe.android.honbabstop.AddChat.presenter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.landvibe.android.honbabstop.AddChat.model.AddChatModel;
import com.landvibe.android.honbabstop.AddChat.model.SearchModel;
import com.landvibe.android.honbabstop.GlobalApp;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.FoodRestaurant;
import com.landvibe.android.honbabstop.base.domain.TranscoordDTO;

import java.util.List;

/**
 * Created by user on 2017-02-15.
 */

public class AddChatPresenterImpl implements AddChatPresenter.Presenter,SearchModel.ModelDataChange,
        AddChatModel.ImageUploadCallback, SearchModel.TransCoordCallback{

    private static final String TAG ="AddChatPresenterImpl";
    private AddChatPresenter.View view;

    private AddChatModel mAddChatModel;

    private SearchModel mSearchModel;

    private FoodRestaurant foodRestaurant;

    @Override
    public void attachView(AddChatPresenter.View view, Activity activity) {
        this.view=view;

        mAddChatModel = new AddChatModel();
        mAddChatModel.setOnImageUploadCallback(this);

        mSearchModel = new SearchModel();
        mSearchModel.setOnChangeListener(this);
        mSearchModel.setOnTransListener(this);
    }

    @Override
    public void detachView() {
        this.view=null;

        mAddChatModel.setOnImageUploadCallback(null);
        mAddChatModel=null;

        mSearchModel.setOnTransListener(null);
        mSearchModel.setOnChangeListener(null);
        mSearchModel=null;

    }

    @Override
    public void addChat(ChatRoom chatRoom, Uri imageUrl) {
        mAddChatModel.createChat(chatRoom);
        if(imageUrl!=null){
            mAddChatModel.saveImageToStorage(chatRoom.getId(),imageUrl);
        }
        view.moveToChatDetailActivity(chatRoom.getId());

        GlobalApp.getGlobalApplicationContext().changeModel(chatRoom);
    }

    @Override
    public void searchLocation(String query) {
        if(mSearchModel!=null){
            mSearchModel.searchRestaurant(query);
        }
    }

    @Override
    public void loadPOImark(int position) {
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

        /**
     * AddChatModel.ImageUploadCallback
     */
    @Override
    public void onComplete(Uri saveUri, String roomId) {
        mAddChatModel.saveChatImageUrl(saveUri, roomId);
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onProgress(double progress) {

    }

    @Override
    public void onPause() {

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
        view.showMapMarker(foodRestaurant);
    }

    @Override
    public void onTransFailure() {

    }
}
