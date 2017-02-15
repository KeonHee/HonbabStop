package com.landvibe.android.honbabstop.ChatList.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.utils.TimeFormatUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by user on 2017-02-15.
 */


public class ChatListViewHolder extends RecyclerView.ViewHolder { //TODO 뷰홀더 이벤트 리스터

    private Context mContext;

    @BindView(R.id.iv_title_image)
    ImageView mTitleImageView;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_max_people)
    TextView mMaxCountTextView;

    @BindView(R.id.tv_create_time)
    TextView mCreateTimeTextView;

    @BindView(R.id.iv_room_detail)
    ImageView mChatInfoImageView;

    public ChatListViewHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.viewholder_chat_lst,parent,false));

        mContext=context;

        ButterKnife.bind(this, itemView);
    }

    public void onBind(ChatRoom chatRoom){
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


            //TODO info 다이얼로그 - https://github.com/javiersantos/MaterialStyledDialogs
            mChatInfoImageView.setOnClickListener(v -> Log.d("ViewHolder","이벤트발생"));

            mCreateTimeTextView.setText(TimeFormatUtils.getPassByTimeStr(chatRoom.getStartTimeStamp()));


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
