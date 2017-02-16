package com.landvibe.android.honbabstop.AddChat.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.AddChat.model.AddChatModel;
import com.landvibe.android.honbabstop.GlobalApp;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;

/**
 * Created by user on 2017-02-15.
 */

public class AddChatPresenterImpl implements AddChatPresenter.Presenter {

    private AddChatPresenter.View view;

    private AddChatModel mAddChatModel;

    @Override
    public void attachView(AddChatPresenter.View view, Activity activity) {
        this.view=view;

        mAddChatModel = new AddChatModel();

    }

    @Override
    public void detachView() {
        this.view=null;

        mAddChatModel=null;

    }

    @Override
    public void addChat(ChatRoom chatRoom) {
        mAddChatModel.createChat(chatRoom);
        view.moveToMainActivity();

        GlobalApp.getGlobalApplicationContext().changeModel(chatRoom);
    }
}
