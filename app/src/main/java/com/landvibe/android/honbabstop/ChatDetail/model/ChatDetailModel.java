package com.landvibe.android.honbabstop.ChatDetail.model;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.landvibe.android.honbabstop.base.domain.ChatMessage;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.User;

import java.util.List;

/**
 * Created by user on 2017-02-17.
 */

public class ChatDetailModel implements ChildEventListener{


    private DatabaseReference mDatabase;

    private Query mQuery;

    private ObserverChatMessageCallback mObserverChatMessageCallback;


    public interface ObserverChatMessageCallback{
        void update(ChatMessage message);
    }

    public void setObserverChatMessageListener(ObserverChatMessageCallback callback){
        mObserverChatMessageCallback=callback;
    }

    public ChatDetailModel(){
        loadDB();
    }

    /**
     * DB instance 로드
     */
    private void loadDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    /**
     *  채팅 메세지들을 load하고 리스너를 등록하여 추가되는 메세지를 감시한다
     *  push(), setMessage() 이벤트 감시
     */
    public void loadChatMessages(String roomId, int queryNum){
        if (mObserverChatMessageCallback==null){
            return;
        }

        mQuery = mDatabase.child("ChatMessage")
                .child(roomId)
                .limitToLast(queryNum)
                .orderByKey();

        mQuery.addChildEventListener(this);
    }

    public void removeChildEventListener(){
        mQuery.removeEventListener(this);
    }


    /**
     * ChildEventListener
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
        if (message!=null){
            mObserverChatMessageCallback.update(message);
            Log.d("ChatDetailModel", message.getMessage());
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    /**
     *  메세지 전송
     */
    public void sendMessage(ChatMessage message,String chatRoomId){
        if(mDatabase==null){
            return;
        }
        mDatabase.child("ChatMessage").child(chatRoomId).push().setValue(message);
    }



    /**
     *  채팅방의 User 정보 삭제 완료 콜백
     */
    private CompleteChangeUserData mCompleteChangeUserData;

    public interface CompleteChangeUserData{
        void onRemove(ChatRoom chatRoom);
        void onFailure(DatabaseError databaseError);
    }

    public void setCompleteListener(CompleteChangeUserData listener){
        mCompleteChangeUserData=listener;
    }


    /**
     *  채팅방의 User 정보 삭제
     */
    public void removeUserInfoInChatRoom(User user, String roomId){
        if(mDatabase==null){
            return;
        }

        //@TODO MyChat 데이터에서 삭제

        mDatabase.child("ChatList").child(roomId).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ChatRoom chatRoom = mutableData.getValue(ChatRoom.class);

                if(chatRoom==null){
                    return Transaction.success(mutableData);
                }

                List<String> members = chatRoom.getMembers();
                members.remove(user.getUid());

                chatRoom.setCurrentPeople(chatRoom.getCurrentPeople()-1);

                mutableData.setValue(chatRoom);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);
                if(chatRoom!=null){
                    mCompleteChangeUserData.onRemove(chatRoom);
                }else {
                    mCompleteChangeUserData.onFailure(databaseError);
                }
            }
        });

    }
}
