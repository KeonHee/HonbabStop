package com.landvibe.android.honbabstop.chatdetail.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatMessage;
import com.landvibe.android.honbabstop.base.utils.TimeFormatUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017-02-17.
 */

public class ChatEnterMessageViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.tv_enter_message)
    TextView mMessageTextView;

    @BindView(R.id.tv_enter_timestamp)
    TextView mTimeTextView;

    private Context mContext;

    public ChatEnterMessageViewHolder(Context context, View itemView) {
        super(itemView);

        ButterKnife.bind(this,itemView);

        mContext=context;
    }


    public void onBind(ChatMessage chatMessage){
        String enterMessage = String.format(Locale.KOREAN, "%s 님이 입장 하셨습니다.",chatMessage.getName());
        mMessageTextView.setText(enterMessage);

        String time = TimeFormatUtils.converTimeStamp(chatMessage.getSendTimeStamp());
        mTimeTextView.setText(time);
    }
}
