package com.landvibe.android.honbabstop.ChatDetail.adapter.contract;

import com.landvibe.android.honbabstop.base.domain.ChatMessage;

import java.util.List;

/**
 * Created by user on 2017-02-17.
 */

public interface ChatAdapterContract {

    interface View {
        void notifyAdapter();

        void notifyLastOne();

    }

    interface Model {

        void setListData(List<ChatMessage> messages);

        void addListData(ChatMessage message);

    }
}
