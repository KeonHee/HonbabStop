package com.landvibe.android.honbabstop.addchat.presenter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.landvibe.android.honbabstop.GlobalApp;
import com.landvibe.android.honbabstop.addchat.model.AddChatModel;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.FoodRestaurant;
import com.landvibe.android.honbabstop.base.observer.CustomObserver;

/**
 * Created by user on 2017-02-15.
 */

public class AddChatPresenterImpl implements AddChatPresenter.Presenter,
        AddChatModel.ImageUploadCallback, CustomObserver{

    private static final String TAG ="AddChatPresenterImpl";
    private AddChatPresenter.View view;

    private AddChatModel mAddChatModel;

    @Override
    public void attachView(AddChatPresenter.View view, Activity activity) {
        this.view=view;

        mAddChatModel = new AddChatModel();
        mAddChatModel.setOnImageUploadCallback(this);

        GlobalApp.getGlobalApplicationContext().addObserver(this);
    }

    @Override
    public void detachView() {
        this.view=null;

        mAddChatModel.setOnImageUploadCallback(null);
        mAddChatModel=null;

        GlobalApp.getGlobalApplicationContext().removeObserver(null);
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
     * CustomObserver
     */
    @Override
    public void update(Object object) {
        if(object instanceof FoodRestaurant){
            FoodRestaurant foodRestaurant = (FoodRestaurant) object;
            Log.d(TAG, "foodRestaurant : " + foodRestaurant);
            Log.d(TAG, "view : " + view);
            view.showMapMarker(foodRestaurant);
        }
    }
}
