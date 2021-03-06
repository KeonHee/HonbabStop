package com.landvibe.android.honbabstop.chatdetail.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.chatdetail.adapter.contract.ChatAdapterContract;
import com.landvibe.android.honbabstop.base.domain.ChatMessage;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.User;


/**
 * Created by user on 2017-02-16.
 */

public interface ChatDetailPresenter {

    interface View{

        void moveToMainActivity();

        void clearText();

        void scrollToBottom();

        void initMapFragment(ChatRoom chatRoom);

        void showMaps();

        void hideMaps();


    }

    interface Presenter{
        void attachView(ChatDetailPresenter.View view, Activity activity);

        void detachView();

        void setChatMessageAdapterModel(ChatAdapterContract.Model model);

        void setChatMessageAdapterViw(ChatAdapterContract.View view);

        void sendMessage(ChatMessage message, String chatRoomId);

        void outOfChatRoom(User user, String roomId);

        void backToTheMain();

        void loadChatMessageList(String chatRoomId);

        void loadChatRoomInfo(String chatRoomId);

        void actionMapView();
    }
}
