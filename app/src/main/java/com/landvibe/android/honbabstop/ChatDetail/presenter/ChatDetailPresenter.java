package com.landvibe.android.honbabstop.ChatDetail.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.ChatDetail.adapter.contract.ChatAdapterContract;
import com.landvibe.android.honbabstop.base.domain.ChatMessage;
import com.landvibe.android.honbabstop.base.domain.User;


/**
 * Created by user on 2017-02-16.
 */

public interface ChatDetailPresenter {

    interface View{

        void moveToMainActivity();

        void clearText();

        void scrollToBottom();

    }

    interface Presenter{
        void attachView(ChatDetailPresenter.View view, Activity activity);

        void detachView();

        void setChatMessageAdapterModel(ChatAdapterContract.Model model);

        void setChatMessageAdapterViw(ChatAdapterContract.View view);

        void sendMessage(ChatMessage message, String chatRoomId);

        void outOfChatRoom(User user);

        void backToTheMain();

        void loadChatMessageList(String chatRoomId);


    }
}
