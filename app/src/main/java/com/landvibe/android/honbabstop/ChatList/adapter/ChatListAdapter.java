package com.landvibe.android.honbabstop.chatlist.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.landvibe.android.honbabstop.chatlist.adapter.contract.ChatListAdapterContract;
import com.landvibe.android.honbabstop.chatlist.adapter.holder.ChatListViewHolder;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-02-15.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder>
        implements ChatListAdapterContract.View, ChatListAdapterContract.Model {

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
        holder.onBind(list.get(realPosition));
    }

    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }
        return list.size();
    }


    /**
     * ChatListAdapterContract.View
     */
    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public void notifyAdapterLastIndex() {
        notifyItemChanged(0);
    }

    @Override
    public void notifyAdapterPosition(int index) {
        notifyItemChanged(list.size()-index-1);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener=listener;
    }


    /**
     * ChatListAdapterContract.Model
     */
    @Override
    public void setListData(List<ChatRoom> listItem) {
        list.clear();
        list.addAll(listItem);
    }

    @Override
    public void addListData(ChatRoom item) {
        list.add(item);
    }

    @Override
    public void updateData(ChatRoom item, int index) {
        if (list == null || list.size() == 0) {
            return;
        }
        list.set(index,item);
    }

    @Override
    public int indexOf(ChatRoom chatRoom) {
        if (list == null || list.size() == 0) {
            return -1;
        }

        for(int i=0;i<list.size();i++){
            if(list.get(i).getId().equals(chatRoom.getId())){
                return i;
            }
        }
        return -1;
    }

}
