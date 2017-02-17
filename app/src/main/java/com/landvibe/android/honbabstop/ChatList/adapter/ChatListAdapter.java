package com.landvibe.android.honbabstop.ChatList.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.landvibe.android.honbabstop.ChatList.adapter.contract.ChatListAdapterContract;
import com.landvibe.android.honbabstop.ChatList.adapter.holder.ChatListViewHolder;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-02-15.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> implements ChatListAdapterContract.View, ChatListAdapterContract.Model {

    private Activity mActivity;

    private OnItemClickListener mOnItemClickListener;
    private List<ChatRoom> list = new ArrayList<>();

    public ChatListAdapter(Activity activity){
        super();
        mActivity=activity;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ChatListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatListViewHolder(mActivity,parent,mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(ChatListViewHolder holder, int position) {
        int realPosition = list.size()-holder.getAdapterPosition()-1; //reverse
        holder.onBind(list.get(realPosition), realPosition);
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
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener=listener;
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
