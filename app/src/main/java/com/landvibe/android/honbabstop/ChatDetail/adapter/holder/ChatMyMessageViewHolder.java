package com.landvibe.android.honbabstop.chatdetail.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatMessage;
import com.landvibe.android.honbabstop.base.utils.TimeFormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017-02-17.
 */

public class ChatMyMessageViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.tv_my_message_text)
    TextView mMessageTextView;

    @BindView(R.id.tv_my_message_timestamp)
    TextView mTimeStampTextView;

    @BindView(R.id.layout_my_message_container)
    RelativeLayout mChatMessageContainer;

    private Context mContext;

    public ChatMyMessageViewHolder(Context context, View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        mContext = context;

    }

    public void onBind(ChatMessage message){

        mMessageTextView.setText(message.getMessage());
        mTimeStampTextView.setText(TimeFormatUtils.converTimeStamp(message.getSendTimeStamp()));

       // int maxWidth = mChatMessageContainer.getWidth()*70/100;
        //mMessageTextView.setMaxWidth(maxWidth);
    }
}
