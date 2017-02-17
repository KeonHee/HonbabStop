package com.landvibe.android.honbabstop.ChatList.adapter.contract;

import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by user on 2017-02-15.
 */

public interface ChatListAdapterContract {

    interface View {
        void notifyAdapter();
        void setOnItemClickListener(OnItemClickListener listener);

    }

    interface Model {
        void setListData(List<ChatRoom> listItem);
        void addListData(ChatRoom item);
    }

}
