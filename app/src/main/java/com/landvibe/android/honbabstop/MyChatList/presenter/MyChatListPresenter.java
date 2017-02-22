package com.landvibe.android.honbabstop.mychatlist.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.mychatlist.adapter.contract.MyChatListAdapterContract;

/**
 * Created by user on 2017-02-18.
 */

public interface MyChatListPresenter {

    interface View{

        void moveToChatDetailActivity(String roomId, String title);


    }

    interface Presenter{

        void attachView(MyChatListPresenter.View view, Activity activity);

        void detachView();

        void setChatListAdapterModel(MyChatListAdapterContract.Model model);

        void setChatListAdapterView(MyChatListAdapterContract.View view);

        void loadMyChatList();

    }

}
