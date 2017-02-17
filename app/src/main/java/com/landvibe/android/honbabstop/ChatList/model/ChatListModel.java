package com.landvibe.android.honbabstop.ChatList.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-02-15.
 */

public class ChatListModel {

    private final static String TAG ="ChatListModel";

    private DatabaseReference mDatabase;

    private ChangeChatListData mChangeChatListData;

    public interface ChangeChatListData{
        void update(List<ChatRoom> list);
    }

    public void setChangeListener(ChangeChatListData listener){
        mChangeChatListData=listener;
    }

    public ChatListModel(){
        loadDB();
    }

    /**
     * DB instance 로드
     */
    private void loadDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    /**
     * 채팅 리스트 DB 쿼리
     */
    public void loadChatList(int queryNum){
        if(mDatabase==null || mChangeChatListData==null){
            return;
        }

        List<ChatRoom> chatList = new ArrayList<>();
        mDatabase.child("ChatList")
                //.orderByChild("startTimeStamp") /*key값이 timestamp 기반으로 생성됨*/
                .limitToLast(queryNum) /* 쿼리 */
                .orderByKey()
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    ChatRoom chatRoom = child.getValue(ChatRoom.class);
                    chatList.add(chatRoom);
                }
                mChangeChatListData.update(chatList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





}
