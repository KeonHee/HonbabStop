package com.landvibe.android.honbabstop.MyChatList.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.MyChat;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;

import java.util.List;

/**
 * Created by user on 2017-02-18.
 */

public class MyChatListModel {


    private final static String TAG = "MyChatListModel";

    private DatabaseReference mDatabase;

    private List<MyChat> mMyChatList;

    public MyChatListModel(){
        loadDB();
    }

    /**
     * DB instance 로드
     */
    private void loadDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    /**
     * 나의 채팅 리스트 DB 쿼리 콜백 등록
     */
    private ChangeChatListData mChangeChatListData;

    public interface ChangeChatListData{
        void update(List<MyChat> list);
    }

    public void setChangeListener(ChangeChatListData listener) {
        mChangeChatListData = listener;
    }


    /**
     * 나의 채팅 리스트 DB 쿼리
     */
    public void loadMyChatList(int queryNum){
        if(mDatabase==null || mChangeChatListData==null){
            return;
        }

        User user = UserStore.getInstance().getUser();

        mMyChatList.clear();

        mDatabase.child("MyChatList").child(user.getUid())
                .limitToLast(queryNum)
                .orderByKey()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            MyChat myChat = child.getValue(MyChat.class);
                            mMyChatList.add(myChat);
                        }
                        mChangeChatListData.update(mMyChatList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



}
