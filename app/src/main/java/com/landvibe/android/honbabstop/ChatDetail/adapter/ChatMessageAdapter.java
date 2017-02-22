package com.landvibe.android.honbabstop.chatdetail.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.landvibe.android.honbabstop.chatdetail.adapter.contract.ChatAdapterContract;
import com.landvibe.android.honbabstop.chatdetail.adapter.holder.ChatEnterMessageViewHolder;
import com.landvibe.android.honbabstop.chatdetail.adapter.holder.ChatMyMessageViewHolder;
import com.landvibe.android.honbabstop.chatdetail.adapter.holder.ChatOtherMessageViewHolder;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatMessage;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-02-17.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements ChatAdapterContract.Model, ChatAdapterContract.View{

    private List<ChatMessage> messageList = new ArrayList<>();

    private Activity mActivity;


    public ChatMessageAdapter(Activity activity){
        super();
        mActivity=activity;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageList.get(position);
        User user = UserStore.getInstance().getUser();
        if(message==null || user==null){
            return super.getItemViewType(position);
        }
        if(message.getType()==ChatMessage.TYPE_ENTER){
            return ChatMessage.TYPE_ENTER;
        }else if(message.getUid().equals(user.getUid())){
            return ChatMessage.TYPE_MY_MESSAGE;
        }else {
            return ChatMessage.TYPE_OTHER_MESSAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case ChatMessage.TYPE_ENTER:
                return new ChatEnterMessageViewHolder(parent.getContext(),
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.viewholder_chat_enter_message, parent, false));
            case ChatMessage.TYPE_MY_MESSAGE:
                return new ChatMyMessageViewHolder(
                        parent.getContext(),
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.viewholder_chat_my_message, parent, false));
            case ChatMessage.TYPE_OTHER_MESSAGE:
                return new ChatOtherMessageViewHolder(
                        parent.getContext(),
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.viewholder_chat_other_message, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = messageList.get(position);

        switch (holder.getItemViewType()) {
            case ChatMessage.TYPE_ENTER:
                ChatEnterMessageViewHolder enterMessageViewHolder = (ChatEnterMessageViewHolder) holder;
                enterMessageViewHolder.onBind(chatMessage);
                return;
            case ChatMessage.TYPE_MY_MESSAGE:
                ChatMyMessageViewHolder chatMyMessageViewHolder = (ChatMyMessageViewHolder) holder;
                chatMyMessageViewHolder.onBind(chatMessage);
                return;
            case ChatMessage.TYPE_OTHER_MESSAGE:
                ChatOtherMessageViewHolder chatOtherMessageViewHolder = (ChatOtherMessageViewHolder) holder;
                chatOtherMessageViewHolder.onBind(chatMessage);
        }
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public void setListData(List<ChatMessage> messages) {
        messageList=messages;
    }

    @Override
    public void addListData(ChatMessage message) {
        messageList.add(message);
    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public void notifyLastOne() {
        notifyItemInserted(messageList.size()-1);
    }

}
