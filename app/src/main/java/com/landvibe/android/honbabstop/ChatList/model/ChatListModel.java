package com.landvibe.android.honbabstop.ChatList.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-02-15.
 */

public class ChatListModel {

    private final static String TAG ="ChatListModel";

    private DatabaseReference mDatabase;

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
     * 채팅 리스트 DB 쿼리 콜백 등록
     */
    private ChangeChatListData mChangeChatListData;

    public interface ChangeChatListData{
        void update(List<ChatRoom> list);
    }

    public void setChangeListener(ChangeChatListData listener){
        mChangeChatListData=listener;
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


    /**
     *  채팅방의 User 정보 수정 완료 콜백
     */
    private CompleteChangeUserData mCompleteChangeUserData;

    public interface CompleteChangeUserData{
        void onComplete(ChatRoom chatRoom);
        void onFailure(String errorMessage);
    }

    public void setCompleteListener(CompleteChangeUserData listener){
        mCompleteChangeUserData=listener;
    }

    /**
     *  채팅방의 User 정보 수정
     */
    public void addUserInfoInChatRoom(String uid, String roomId, List<String> members){
        if(mDatabase==null){
            return;
        }

        //@TODO MyChat에 데이터 추가

        DatabaseReference roomRef = mDatabase.child("ChatList").child(roomId);
        if(roomRef==null){
            mCompleteChangeUserData.onFailure("없는 채팅방");
            return;
        }


        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);

                if(chatRoom==null){
                    mCompleteChangeUserData.onFailure("없는 채팅방");
                    return;
                }

                if(chatRoom.getCurrentPeople()>=chatRoom.getMaxPeople()){
                    mCompleteChangeUserData.onFailure("인원 초과");
                    return;
                }


                roomRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {

                        ChatRoom chatRoom = mutableData.getValue(ChatRoom.class);

                        if(chatRoom==null){
                            // DB 동시 접근 시, 모두 입장 불가
                            mCompleteChangeUserData.onFailure("채팅방에 접속자가 몰렸습니다 다시 시도해 주세요");
                            Log.d(TAG, "채팅방에 접속자가 몰렸습니다 다시 시도해 주세요");
                            return Transaction.abort();
                        }

                        if(chatRoom.getCurrentPeople()>=chatRoom.getMaxPeople()){
                            // 한명은 입장 가능
                            mCompleteChangeUserData.onFailure("인원 초과");
                            Log.d(TAG, "인원 초과");
                            return Transaction.abort();
                        }

                        if(!members.contains(uid)){
                            members.add(uid);
                            chatRoom.setMembers(members);
                            chatRoom.setCurrentPeople(chatRoom.getCurrentPeople()+1);
                        }

                        mutableData.setValue(chatRoom);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                        if(!committed){
                            if(databaseError!=null){
                                Log.d(TAG, databaseError.getMessage());
                            }
                            return;
                        }

                        if(databaseError!=null){
                            Log.d(TAG, databaseError.getMessage());
                            return;
                        }

                        ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);
                        if(chatRoom!=null) {
                            mCompleteChangeUserData.onComplete(chatRoom);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled : " +databaseError.getMessage());
            }
        });


    }
}
