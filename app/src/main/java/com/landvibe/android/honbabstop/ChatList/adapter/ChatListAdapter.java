package com.landvibe.android.honbabstop.ChatList.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.landvibe.android.honbabstop.ChatList.adapter.contract.ChatListAdapterContract;
import com.landvibe.android.honbabstop.ChatList.adapter.holder.ChatListViewHolder;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-02-15.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> implements ChatListAdapterContract.View, ChatListAdapterContract.Model {

    private Activity mActivity;

    private List<ChatRoom> list = new ArrayList<>();

    public ChatListAdapter(Activity activity){
        super();
        mActivity=activity;
    }

    @Override
    public ChatListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatListViewHolder(mActivity,parent);
    }

    @Override
    public void onBindViewHolder(ChatListViewHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }
        return list.size();
    }


    @Override
    public void notifytAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public void setListData(List<ChatRoom> listItem) {
        list=listItem;
    }

    @Override
    public void addListData(ChatRoom item) {
        list.add(item);
    }

}