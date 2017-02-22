package com.landvibe.android.honbabstop.MyChatList.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.landvibe.android.honbabstop.MyChatList.adapter.contract.MyChatListAdapterContract;
import com.landvibe.android.honbabstop.MyChatList.adapter.holder.MyChatViewHolder;
import com.landvibe.android.honbabstop.base.domain.MyChat;
import com.landvibe.android.honbabstop.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-02-18.
 */

public class MyChatListAdapter extends RecyclerView.Adapter<MyChatViewHolder>
    implements MyChatListAdapterContract.View, MyChatListAdapterContract.Model{

    private Activity mActivity;

    private OnItemClickListener mOnItemClickListener;
    private List<MyChat> list = new ArrayList<>();

    public MyChatListAdapter(Activity activity){
        super();
        mActivity=activity;
    }

    @Override
    public MyChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyChatViewHolder(mActivity, parent, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(MyChatViewHolder holder, int position) {
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
     * MyChatListAdapterContract.View
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
     * MyChatListAdapterContract.Model
     */
    @Override
    public void setListData(List<MyChat> listItem) {
        list.clear();
        list.addAll(listItem);
    }

    @Override
    public void addListData(MyChat item) {
        list.add(item);
    }

    @Override
    public void updateData(MyChat item, int index) {
        if (list == null || list.size() == 0) {
            return;
        }
        list.set(index,item);
    }

    @Override
    public int indexOf(MyChat myChat) {
        if (list == null || list.size() == 0) {
            return -1;
        }

        for(int i=0;i<list.size();i++){
            if(list.get(i).getId().equals(myChat.getId())){
                return i;
            }
        }
        return -1;
    }
}