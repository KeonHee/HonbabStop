package com.landvibe.android.honbabstop.ChatList.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.ChatList.adapter.contract.ChatListAdapterContract;

/**
 * Created by user on 2017-02-15.
 */

public interface ChatListPresenter {

    interface View{

        void moveToChatDetailActivity();

        void moveToAddChatActivity();

    }

    interface Presenter{

        void attachView(ChatListPresenter.View view, Activity activity);

        void detachView();

        void setChatListAdapterModel(ChatListAdapterContract.Model model);

        void setChatListAdapterView(ChatListAdapterContract.View view);

        void loadChatList();

    }
}
