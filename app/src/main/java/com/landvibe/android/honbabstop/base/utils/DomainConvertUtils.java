package com.landvibe.android.honbabstop.base.utils;

import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.MyChat;

/**
 * Created by user on 2017-02-22.
 */

public class DomainConvertUtils {

    public static MyChat convertChatRoomToMyChat(ChatRoom chatRoom){
        MyChat myChat = new MyChat();
        myChat.setId(chatRoom.getId());
        myChat.setTitle(chatRoom.getTitle());
        myChat.setStartTimeStamp(chatRoom.getStartTimeStamp());
        myChat.setEndTimeStamp(chatRoom.getEndTimeStamp());
        myChat.setContactTime(chatRoom.getContactTime());
        myChat.setCurrentPeople(chatRoom.getCurrentPeople());
        myChat.setMaxPeople(chatRoom.getMaxPeople());
        myChat.setHeader(chatRoom.getHeader());
        myChat.setMembers(chatRoom.getMembers());
        return myChat;
    }
}
