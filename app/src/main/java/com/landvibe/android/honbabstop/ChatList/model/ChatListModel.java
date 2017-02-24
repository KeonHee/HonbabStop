package com.landvibe.android.honbabstop.chatlist.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.MyChat;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.utils.DomainConvertUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 2017-02-15.
 */

public class ChatListModel {

    private final static String TAG ="ChatListModel";

    private DatabaseReference mDatabase;

    private List<ChatRoom> mChatList = new ArrayList<>();

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

        mChatList.clear();
        mDatabase.child("ChatList")
                //.orderByChild("startTimeStamp") /*key값이 timestamp 기반으로 생성됨*/
                .limitToLast(queryNum) /* 쿼리 */
                .orderByKey()
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    ChatRoom chatRoom = child.getValue(ChatRoom.class);
                    mChatList.add(chatRoom);
                }
                mChangeChatListData.update(mChatList);
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
    public void addUserInfoInChatRoom(User user, String roomId, List<User> members){
        if(mDatabase==null){
            return;
        }

        DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference().child("ChatList").child(roomId);
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

                boolean isContains=false;
                for (User member : members){
                    if(member.getUid().equals(user.getUid())){
                        isContains=true;
                        break;
                    }
                }

                if(!isContains){
                    user.setEnteredTime(Calendar.getInstance().getTimeInMillis()); // 입장 시간
                    members.add(user);
                    chatRoom.setMembers(members);
                    chatRoom.setCurrentPeople(chatRoom.getCurrentPeople()+1);

                    saveChat(chatRoom);

                    /* My Chat 저장 */
                    MyChat myChat = DomainConvertUtils.convertChatRoomToMyChat(chatRoom);
                    saveMyChat(myChat,chatRoom.getMembers());
                }

                mCompleteChangeUserData.onComplete(chatRoom);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled : " +databaseError.getMessage());
            }
        });
    }

    public void saveChat(ChatRoom chatRoom){
        mDatabase.child("ChatList")
                .child(chatRoom.getId())
                .setValue(chatRoom);
    }

    /**
     * my Chat 저장
     */
    public void saveMyChat(MyChat myChat, List<User> memebers){
        for(User member : memebers){
            mDatabase.child("MyChatList")
                    .child(member.getUid())
                    .child(myChat.getId())
                    .setValue(myChat);
        }
    }
}
