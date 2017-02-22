package com.landvibe.android.honbabstop.MyChatList.adapter.holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.MyChat;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;
import com.landvibe.android.honbabstop.base.listener.OnItemClickListener;
import com.landvibe.android.honbabstop.base.utils.TimeFormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by user on 2017-02-22.
 */

public class MyChatViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.iv_title_image)
    ImageView mTitleImageView;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_max_people)
    TextView mMaxCountTextView;

    @BindView(R.id.tv_create_time)
    TextView mCreateTimeTextView;

    @BindView(R.id.iv_my_room)
    ImageView mIsHeaderImageView;

    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public MyChatViewHolder(Context context, ViewGroup parent, OnItemClickListener listener) {
        super(LayoutInflater.from(context).inflate(R.layout.viewholder_my_chat,parent,false));

        mContext=context;

        mOnItemClickListener=listener;

        ButterKnife.bind(this,itemView);

    }

    public void onBind(MyChat myChat){

        try{

            User user = UserStore.getInstance().getUser();

            if(myChat.getFoodImageUrl()==null){
                Glide.with(mContext)
                        .load(R.drawable.default_profile)
                        .override(150,150)
                        .centerCrop()
                        .dontAnimate()
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(mTitleImageView);
            }else {
                Glide.with(mContext)
                        .load(myChat.getFoodImageUrl())
                        .override(150,150)
                        .centerCrop()
                        .dontAnimate()
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(mTitleImageView);
            }

            mTitleTextView.setText(myChat.getTitle());
            mMaxCountTextView.setText(myChat.getCurrentPeople()+"/"+myChat.getMaxPeople());


            int drawableId;
            if(myChat.getHeader().getUid().equals(user.getUid())){
                drawableId = R.drawable.crown;
            }else {
                drawableId = R.drawable.target;
            }
            mIsHeaderImageView.setImageDrawable(
                    ContextCompat.getDrawable(mContext,drawableId));


            mCreateTimeTextView.setText(TimeFormatUtils.getPassByTimeStr(myChat.getStartTimeStamp()));


        }catch (Exception e){
            e.printStackTrace();
        }

        itemView.setOnClickListener(v->{
            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(myChat);
            }
        });
    }

}
