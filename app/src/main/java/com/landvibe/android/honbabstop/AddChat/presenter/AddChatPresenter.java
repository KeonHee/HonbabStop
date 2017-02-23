package com.landvibe.android.honbabstop.addchat.presenter;

import android.app.Activity;
import android.net.Uri;

import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.FoodRestaurant;

/**
 * Created by user on 2017-02-15.
 */

public interface AddChatPresenter {

    interface View{

        void moveToMainActivity();

        void moveToChatDetailActivity(String roomId);

        void moveToSearchActivity();

        void showMapMarker(FoodRestaurant foodRestaurant);

        void updateTitleImage(Uri imageUrl);

        void showLoading();

        void updateLoading(int progress);

        void hideLoading();

    }

    interface Presenter{
        void attachView(AddChatPresenter.View view, Activity activity);

        void detachView();

        void addChat(ChatRoom chatRoom, Uri imageUrl);

    }
}
