package com.landvibe.android.honbabstop.chatdetail.presenter;

import android.app.Activity;

import com.google.firebase.database.DatabaseError;
import com.landvibe.android.honbabstop.chatdetail.adapter.contract.ChatAdapterContract;
import com.landvibe.android.honbabstop.chatdetail.model.ChatDetailModel;
import com.landvibe.android.honbabstop.GlobalApp;
import com.landvibe.android.honbabstop.base.domain.ChatMessage;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.MyChat;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.utils.DomainConvertUtils;

/**
 * Created by user on 2017-02-16.
 */

public class ChatDetailPresenterImpl implements ChatDetailPresenter.Presenter,
        ChatDetailModel.ObserverChatMessageCallback, ChatDetailModel.CompleteChangeUserData ,
        ChatDetailModel.LoadChatRoomInfoCallback {


    private ChatDetailPresenter.View view;

    private Activity mActivity;

    private ChatAdapterContract.Model mAdapterModel;
    private ChatAdapterContract.View mAdapterView;

    private ChatDetailModel mChatDetailModel;

    private boolean isClicked=false;

    @Override
    public void attachView(ChatDetailPresenter.View view, Activity activity) {
        this.view=view;
        mActivity=activity;

        mChatDetailModel = new ChatDetailModel();
        mChatDetailModel.setObserverChatMessageListener(this);
        mChatDetailModel.setCompleteListener(this);
        mChatDetailModel.setLoadChatRoomInfoListener(this);
    }

    @Override
    public void detachView() {
        this.view=null;
        mActivity=null;

        mChatDetailModel.setLoadChatRoomInfoListener(null);
        mChatDetailModel.setCompleteListener(null);
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
    public void outOfChatRoom(User user, String roomId) {
        mChatDetailModel.removeUserInfoInChatRoom(user,roomId);
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
    public void loadChatRoomInfo(String chatRoomId) {
        mChatDetailModel.loadChatRoomInfo(chatRoomId);
    }

    @Override
    public void actionMapView() {
        if(isClicked){
            view.hideMaps();
            isClicked=false;
        }else {
            view.showMaps();
            isClicked=true;
        }
    }


    /**
     *  메세지 수신 콜백
     */
    @Override
    public void update(ChatMessage message) {
        mAdapterModel.addListData(message);
        mAdapterView.notifyLastOne();
        view.scrollToBottom();
    }


    /**
     *  User 정보 삭제 완료 콜백
     */
    @Override
    public void onRemove(ChatRoom chatRoom) {
        view.moveToMainActivity();

        GlobalApp.getGlobalApplicationContext().changeModel(chatRoom);

        MyChat myChat = DomainConvertUtils.convertChatRoomToMyChat(chatRoom);
        GlobalApp.getGlobalApplicationContext().changeModel(myChat);
    }

    @Override
    public void onFailure(DatabaseError databaseError) {


    }


    /**
     * Chat Room 데이터 로드 콜백
     */
    @Override
    public void onSuccess(ChatRoom chatRoom) {
        view.initMapFragment(chatRoom);
    }

    @Override
    public void onFailure() {

    }
}
