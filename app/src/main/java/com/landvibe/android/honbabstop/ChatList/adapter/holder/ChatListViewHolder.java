package com.landvibe.android.honbabstop.ChatList.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
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
    TextView mMaxConuntTextView;

    @BindView(R.id.iv_room_detail)
    ImageView mRoomDetailImageView;

    @BindView(R.id.tv_create_time)
    TextView mCreateTimeTextView;

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
                                    .override(100,100)
                    )
                    .override(100,100)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(mTitleImageView);


            mTitleTextView.setText(chatRoom.getTitle());
            mMaxConuntTextView.setText(chatRoom.getCurrentPeople()+"/"+chatRoom.getMaxPeople());

            Glide.with(mContext)
                    .load(R.drawable.ic_info_outline_black_24dp)
                    .override(100,100)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(mRoomDetailImageView);
            // TODO mRoomDetailImageView에 클릭 리스너 등록 하기 > Info 다이얼 로그

            mCreateTimeTextView.setText(TimeFormatUtils.getPassByTimeStr(chatRoom.getStartTimeStamp()));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
