package com.landvibe.android.honbabstop.ChatDetail.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.ChatDetail.adapter.contract.ChatAdapterContract;
import com.landvibe.android.honbabstop.ChatDetail.model.ChatDetailModel;
import com.landvibe.android.honbabstop.base.domain.ChatMessage;
import com.landvibe.android.honbabstop.base.domain.User;

/**
 * Created by user on 2017-02-16.
 */

public class ChatDetailPresenterImpl implements ChatDetailPresenter.Presenter,
        ChatDetailModel.ObserverChatMessageCallback {


    private ChatDetailPresenter.View view;

    private Activity mActivity;

    private ChatAdapterContract.Model mAdapterModel;
    private ChatAdapterContract.View mAdapterView;


    private ChatDetailModel mChatDetailModel;

    @Override
    public void attachView(ChatDetailPresenter.View view, Activity activity) {
        this.view=view;
        mActivity=activity;

        mChatDetailModel = new ChatDetailModel();
        mChatDetailModel.setObserverChatMessageListener(this);
    }

    @Override
    public void detachView() {
        this.view=null;
        mActivity=null;

        mChatDetailModel.setObserverChatMessageListener(null);
        mChatDetailModel.removeChildEventListener();
        mChatDetailModel=null;

    }

    @Override
    public void setChatMessageAdapterModel(ChatAdapterContract.Model model) {
        mAdapterModel=model;
    }

    @Override
    public void setChatMessageAdapterViw(ChatAdapterContract.View view) {
        mAdapterView=view;
    }

    @Override
    public void sendMessage(ChatMessage message, String chatRoomId) {
        view.clearText();
        mChatDetailModel.sendMessage(message, chatRoomId);
    }

    @Override
    public void outOfChatRoom(User user) {

        /* Somethings */

        view.moveToMainActivity();
    }

    @Override
    public void backToTheMain() {
        view.moveToMainActivity();
    }

    @Override
    public void loadChatMessageList(String chatRoomId) {
        mChatDetailModel.loadChatMessages(chatRoomId, 100); //TODO 스크롤 시 추가로드 구현
    }

    @Override
    public void update(ChatMessage message) {
        mAdapterModel.addListData(message);
        mAdapterView.notifyLastOne();
        view.scrollToBottom();
    }
}
