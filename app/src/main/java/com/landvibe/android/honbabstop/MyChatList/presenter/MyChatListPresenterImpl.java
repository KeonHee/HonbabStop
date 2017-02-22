package com.landvibe.android.honbabstop.MyChatList.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.ChatList.model.ChatListModel;
import com.landvibe.android.honbabstop.GlobalApp;
import com.landvibe.android.honbabstop.MyChatList.adapter.contract.MyChatListAdapterContract;
import com.landvibe.android.honbabstop.MyChatList.model.MyChatListModel;
import com.landvibe.android.honbabstop.base.domain.MyChat;
import com.landvibe.android.honbabstop.base.listener.OnItemClickListener;
import com.landvibe.android.honbabstop.base.observer.CustomObserver;

import java.util.List;

/**
 * Created by user on 2017-02-18.
 */

public class MyChatListPresenterImpl implements MyChatListPresenter.Presenter,
        MyChatListModel.ChangeChatListData,OnItemClickListener, CustomObserver {

    private final static String TAG="MyChatListPresenterImpl";
    private MyChatListPresenter.View view;

    private Activity mActivity;

    private MyChatListAdapterContract.Model mAdapterModel;
    private MyChatListAdapterContract.View mAdapterView;

    private MyChatListModel mMyChatListModel;

    @Override
    public void attachView(MyChatListPresenter.View view, Activity activity) {
        this.view=view;
        mActivity=activity;

        mMyChatListModel = new MyChatListModel();
        mMyChatListModel.setChangeListener(this);


        GlobalApp.getGlobalApplicationContext().addObserver(this);
    }

    @Override
    public void detachView() {
        this.view=null;
        mActivity=null;

        mMyChatListModel.setChangeListener(null);
        mMyChatListModel = null;

        GlobalApp.getGlobalApplicationContext().removeObserver(this);
    }

    @Override
    public void setChatListAdapterModel(MyChatListAdapterContract.Model model) {
        mAdapterModel=model;

    }

    @Override
    public void setChatListAdapterView(MyChatListAdapterContract.View view) {
        mAdapterView=view;
        mAdapterView.setOnItemClickListener(this);
    }

    @Override
    public void loadMyChatList() {
        mMyChatListModel.loadMyChatList(15); //TODO 쿼리개수 핸들러 만들기 (무한로딩)
    }

    @Override
    public void onItemClick(Object object) {
        MyChat myChat = (MyChat) object;
        view.moveToChatDetailActivity(myChat.getId(),myChat.getTitle());
    }

    @Override
    public void update(List<MyChat> list) {
        if(list==null || list.size()==0){
            return;
        }
        mAdapterModel.setListData(list);
        mAdapterView.notifyAdapter();
    }

    @Override
    public void update(Object object) {
        if(object instanceof  MyChat){
            MyChat myChat = (MyChat) object;

            int index = mAdapterModel.indexOf(myChat);
            if(index<0){
                mAdapterModel.addListData(myChat);
                mAdapterView.notifyAdapter();
            }else {
                mAdapterModel.updateData(myChat,index);
                mAdapterView.notifyAdapterPosition(index);
            }
        }
    }

}
