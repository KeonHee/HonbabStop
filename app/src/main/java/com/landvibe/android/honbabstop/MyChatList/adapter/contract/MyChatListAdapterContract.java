package com.landvibe.android.honbabstop.MyChatList.adapter.contract;

import com.landvibe.android.honbabstop.base.domain.MyChat;
import com.landvibe.android.honbabstop.base.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by user on 2017-02-22.
 */

public interface MyChatListAdapterContract {

    interface View {

        void notifyAdapter();

        void notifyAdapterLastIndex();

        void notifyAdapterPosition(int index);

        void setOnItemClickListener(OnItemClickListener listener);

    }

    interface Model {
        void setListData(List<MyChat> listItem);

        void addListData(MyChat item);

        void updateData(MyChat item, int index);

        int indexOf(MyChat myChat);
    }

}
