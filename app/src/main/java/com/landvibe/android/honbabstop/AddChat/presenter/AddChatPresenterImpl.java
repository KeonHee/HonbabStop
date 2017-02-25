package com.landvibe.android.honbabstop.addchat.presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.landvibe.android.honbabstop.GlobalApp;
import com.landvibe.android.honbabstop.addchat.model.AddChatModel;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.FoodRestaurant;
import com.landvibe.android.honbabstop.base.domain.MyChat;
import com.landvibe.android.honbabstop.base.observer.CustomObserver;
import com.landvibe.android.honbabstop.base.utils.DomainConvertUtils;

import java.io.IOException;

/**
 * Created by user on 2017-02-15.
 */

public class AddChatPresenterImpl implements AddChatPresenter.Presenter,
        AddChatModel.ImageUploadCallback, CustomObserver{

    private static final String TAG ="AddChatPresenterImpl";
    private AddChatPresenter.View view;

    private AddChatModel mAddChatModel;

    private Activity mActivity;

    @Override
    public void attachView(AddChatPresenter.View view, Activity activity) {
        this.view=view;

        mActivity=activity;

        mAddChatModel = new AddChatModel();
        mAddChatModel.setOnImageUploadCallback(this);

        GlobalApp.getGlobalApplicationContext().addObserver(this);
    }

    @Override
    public void detachView() {
        this.view=null;

        mActivity=null;

        mAddChatModel.setOnImageUploadCallback(null);
        mAddChatModel=null;

        GlobalApp.getGlobalApplicationContext().removeObserver(null);
    }


    private void renewListView(ChatRoom chatRoom){
        /* 리스트뷰 갱신 */
        GlobalApp.getGlobalApplicationContext().changeModel(chatRoom);

        MyChat myChat = DomainConvertUtils.convertChatRoomToMyChat(chatRoom);
        GlobalApp.getGlobalApplicationContext().changeModel(myChat);

    }
    @Override
    public void addChat(ChatRoom chatRoom, Uri imageUrl) {
        mAddChatModel.createChat(chatRoom);
        if(imageUrl!=null){
            mAddChatModel.saveImageToStorage(chatRoom,imageUrl);
            view.showLoading();
        }else {
            renewListView(chatRoom);

            view.moveToChatDetailActivity(chatRoom.getId());
        }
    }

    /**
     * AddChatModel.ImageUploadCallback
     */
    @Override
    public void onComplete(ChatRoom chatRoom) {
        mAddChatModel.saveChatImageUrl(chatRoom);

        renewListView(chatRoom);

        view.hideLoading();
        view.moveToChatDetailActivity(chatRoom.getId());

    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onProgress(double progress) {
        view.updateLoading((int)progress);
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
            if(view!=null){
                view.showMapMarker(foodRestaurant);

            }
        }
    }
}
