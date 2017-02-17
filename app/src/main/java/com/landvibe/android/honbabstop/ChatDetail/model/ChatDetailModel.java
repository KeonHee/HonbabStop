package com.landvibe.android.honbabstop.ChatDetail.model;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.landvibe.android.honbabstop.base.domain.ChatMessage;

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
}
