package com.landvibe.android.honbabstop.AddChat.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.landvibe.android.honbabstop.ChatList.model.ChatListModel;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017-02-15.
 */

public class AddChatModel {

    private final static String TAG ="AddChatModel";

    private DatabaseReference mDatabase;

    public AddChatModel(){
        loadDB();
    }

    /**
     * DB instance 로드
     */
    private void loadDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Chat 추가
     */
    public void createChat(ChatRoom chatRoom){
        String key = mDatabase.child("ChatList").push().getKey();
        chatRoom.setId(key);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, chatRoom);
        mDatabase.child("ChatList").updateChildren(childUpdates);
    }
}
