package com.landvibe.android.honbabstop.AddChat.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.base.domain.ChatRoom;

/**
 * Created by user on 2017-02-15.
 */

public interface AddChatPresenter {

    interface View{

        void moveToMainActivity();

        void moveToChatDetailActivity(String roomId);

    }

    interface Presenter{
        void attachView(AddChatPresenter.View view, Activity activity);

        void detachView();

        void addChat(ChatRoom chatRoom);

    }
}
