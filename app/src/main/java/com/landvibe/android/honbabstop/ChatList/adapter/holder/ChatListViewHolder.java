package com.landvibe.android.honbabstop.ChatList.adapter.holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;
import com.landvibe.android.honbabstop.base.listener.OnItemClickListener;
import com.landvibe.android.honbabstop.base.utils.TimeFormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by user on 2017-02-15.
 */


public class ChatListViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    @BindView(R.id.iv_title_image)
    ImageView mTitleImageView;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_max_people)
    TextView mMaxCountTextView;

    @BindView(R.id.tv_create_time)
    TextView mCreateTimeTextView;

    @BindView(R.id.iv_my_room)
    ImageView mMyRoomImageView;

    private OnItemClickListener mOnItemClickListener;

    public ChatListViewHolder(Context context, ViewGroup parent, OnItemClickListener listener) {
        super(LayoutInflater.from(context).inflate(R.layout.viewholder_chat_lst,parent,false));

        mContext=context;

        mOnItemClickListener=listener;

        ButterKnife.bind(this, itemView);
    }

    public void onBind(ChatRoom chatRoom, int position){

        try{
            Glide.with(mContext)
                    .load(chatRoom.getFoodImageUrl())
                    .thumbnail(
                            Glide.with(mContext)
                                    .load(R.drawable.default_profile)
                                    .bitmapTransform(new CropCircleTransformation(mContext))
                                    .override(150,150)
                    )
                    .override(150,150)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(mTitleImageView);


            mTitleTextView.setText(chatRoom.getTitle());
            mMaxCountTextView.setText(chatRoom.getCurrentPeople()+"/"+chatRoom.getMaxPeople());

            //TODO 무슨 정보 넣을지?
            //mMyRoomImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_star_not_selected_100dp));

            mCreateTimeTextView.setText(TimeFormatUtils.getPassByTimeStr(chatRoom.getStartTimeStamp()));


        }catch (Exception e){
            e.printStackTrace();
        }

        itemView.setOnClickListener(v->{
            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(chatRoom);
            }
        });
    }

}
