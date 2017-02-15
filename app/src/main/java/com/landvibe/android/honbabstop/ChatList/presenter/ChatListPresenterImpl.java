package com.landvibe.android.honbabstop.ChatList.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.ChatList.adapter.contract.ChatListAdapterContract;
import com.landvibe.android.honbabstop.ChatList.model.ChatListModel;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;

import java.util.List;

/**
 * Created by user on 2017-02-15.
 */

public class ChatListPresenterImpl implements ChatListPresenter.Presenter, ChatListModel.ChangeChatListData{

    private ChatListPresenter.View view;

    private Activity mActivity;

    private ChatListAdapterContract.Model mAdapterModel;
    private ChatListAdapterContract.View mAdapterView;

    private ChatListModel mChatListModel;

    @Override
    public void attachView(ChatListPresenter.View view, Activity activity) {
        this.view=view;
        mActivity=activity;

        mChatListModel = new ChatListModel();
        mChatListModel.setChangeListener(this);

    }

    @Override
    public void detachView() {
        this.view=null;
        mActivity=null;


        mChatListModel.setChangeListener(null);
        mChatListModel=null;

    }

    @Override
    public void setChatListAdapterModel(ChatListAdapterContract.Model model) {
        mAdapterModel=model;
    }

    @Override
    public void setChatListAdapterView(ChatListAdapterContract.View view) {
        mAdapterView=view;
    }

    @Override
    public void loadChatList() {
        mChatListModel.loadChatList(15); //TODO 쿼리개수 핸들러 만들기 (무한로딩)
    }

    @Override
    public void update(List<ChatRoom> list) {
        if(list==null || list.size()==0){
            return;
        }

        mAdapterModel.setListData(list);
        mAdapterView.notifytAdapter();
    }
}
