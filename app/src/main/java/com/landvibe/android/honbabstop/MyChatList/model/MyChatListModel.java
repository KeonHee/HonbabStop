package com.landvibe.android.honbabstop.mychatlist.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.landvibe.android.honbabstop.base.domain.MyChat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-02-18.
 */

public class MyChatListModel {


    private final static String TAG = "MyChatListModel";

    private DatabaseReference mDatabase;

    private List<MyChat> mMyChatList = new ArrayList<>();

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
        if(mDatabase==null){
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mMyChatList.clear();

        mDatabase.child("MyChatList").child(uid)
                .limitToLast(queryNum)
                .orderByKey()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            MyChat myChat = child.getValue(MyChat.class);
                            mMyChatList.add(myChat);
                        }
                        if(mChangeChatListData!=null){
                            mChangeChatListData.update(mMyChatList);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



}
