package com.landvibe.android.honbabstop.chatlist.adapter.holder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.listener.OnItemClickListener;
import com.landvibe.android.honbabstop.base.utils.TimeFormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by user on 2017-02-15.
 */


public class ChatListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_title_image)
    ImageView mTitleImageView;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_max_people)
    TextView mMaxCountTextView;

    @BindView(R.id.tv_create_time)
    TextView mCreateTimeTextView;

    @BindView(R.id.iv_my_room)
    ImageView mIsOpenedImageView;

    private Context mContext;


    private OnItemClickListener mOnItemClickListener;

    public ChatListViewHolder(Context context, ViewGroup parent, OnItemClickListener listener) {
        super(LayoutInflater.from(context).inflate(R.layout.viewholder_chat_lst,parent,false));

        mContext=context;

        mOnItemClickListener=listener;

        ButterKnife.bind(this, itemView);
    }

    public void onBind(ChatRoom chatRoom){

        try{

            if(chatRoom.getFoodImageUrl()==null){
                Glide.with(mContext)
                        .load(R.drawable.default_profile)
                        .override(140,140)
                        .centerCrop()
                        .dontAnimate()
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(mTitleImageView);
            }else {
                Glide.with(mContext)
                        .load(chatRoom.getFoodImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(140,140)
                        .placeholder(R.drawable.default_profile)
                        .centerCrop()
                        .dontAnimate()
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(mTitleImageView);
            }

            mTitleImageView.setOnClickListener(v->showImageDialog(chatRoom.getFoodImageUrl()));

            mTitleTextView.setText(chatRoom.getTitle());
            mMaxCountTextView.setText(chatRoom.getCurrentPeople()+"/"+chatRoom.getMaxPeople());


            int drawableId;
            if(chatRoom.getCurrentPeople()>=chatRoom.getMaxPeople()){
                drawableId=R.drawable.closed;
            }else {
                drawableId=R.drawable.open;
            }
            mIsOpenedImageView.setImageDrawable(
                    ContextCompat.getDrawable(mContext,drawableId));

            mCreateTimeTextView.setText(TimeFormatUtils.getPassByTimeStr(chatRoom.getStartTimeStamp()));


        }catch (Exception e){
            e.printStackTrace();
        }

        itemView.setOnClickListener(v->{
            if(chatRoom.getCurrentPeople()>=chatRoom.getMaxPeople()){{
                // TODO 입장 불가 다이얼로그
                return;
            }}

            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(chatRoom);
            }

        });
    }

    private void showImageDialog(String url){
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.image_dialog);

        ImageView iv = (ImageView) dialog.findViewById(R.id.iv_title_image_dialog);

        if(url==null){
            Glide.with(mContext)
                    .load(R.drawable.default_profile)
                    .override(140,140)
                    .into(iv);
        }else {
            Glide.with(mContext)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.default_profile)
                    .override(140,140)
                    .placeholder(R.drawable.default_profile)
                    .into(iv);
        }
        dialog.show();
    }

}
