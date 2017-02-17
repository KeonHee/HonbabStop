package com.landvibe.android.honbabstop.ChatDetail.adapter.holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatMessage;
import com.landvibe.android.honbabstop.base.utils.TimeFormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by user on 2017-02-17.
 */

public class ChatOtherMessageViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.iv_other_message_avatar)
    ImageView mAvatarImageView;

    @BindView(R.id.tv_other_message_user_name)
    TextView mUserNameTextView;

    @BindView(R.id.tv_other_message_text)
    TextView mMessageTextView;

    @BindView(R.id.tv_other_message_timestamp)
    TextView mTimeStampTextView;

    @BindView(R.id.layout_other_message_container)
    RelativeLayout mMessageContainer;

    private Context mContext;

    public ChatOtherMessageViewHolder(Context context, View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        mContext = context;
    }

    public void onBind(ChatMessage message){
        if(message.getProfileUrl()==null){
            Glide.with(mContext)
                    .load(R.drawable.default_profile)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(mAvatarImageView);
        }else {
            Glide.with(mContext)
                    .load(message.getProfileUrl())
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .thumbnail(
                            Glide.with(mContext)
                                    .load(R.drawable.default_profile)
                                    .bitmapTransform(new CropCircleTransformation(mContext))
                    )
                    .into(mAvatarImageView);
        }
        mUserNameTextView.setText(message.getName());
        mMessageTextView.setText(message.getMessage());
        mTimeStampTextView.setText(TimeFormatUtils.converTimeStamp(message.getSendTimeStamp()));

       // int maxWidth = mMessageContainer.getWidth()*70/100;
       // mMessageTextView.setMaxWidth(maxWidth);
    }

}
