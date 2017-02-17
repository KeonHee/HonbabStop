package com.landvibe.android.honbabstop.ChatList.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.landvibe.android.honbabstop.ChatDetail.ChatDetailActivity;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.listener.OnItemClickListener;
import com.landvibe.android.honbabstop.base.utils.TimeFormatUtils;

import java.util.Calendar;
import java.util.Locale;

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

    private MaterialStyledDialog mChatRoomInfoDialog;

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


            prepareChatRoomInfo(chatRoom);
            mChatInfoImageView.setOnClickListener(v -> showChatRoomInfo());

            mCreateTimeTextView.setText(TimeFormatUtils.getPassByTimeStr(chatRoom.getStartTimeStamp()));


        }catch (Exception e){
            e.printStackTrace();
        }

        itemView.setOnClickListener(v->{
            showChatRoomInfo();
//            if(mOnItemClickListener!=null){
//                showChatRoomInfo();
//                mOnItemClickListener.onItemClick(position);
//            }
        });
    }

    private void prepareChatRoomInfo(ChatRoom chatRoom){

        String title = chatRoom.getTitle();

        /* 만남 시간, 만남 장소, 음식 정보, 현재 인원 */
        long rawContactTime = chatRoom.getContactTime();
        Calendar contactTimeInstance = Calendar.getInstance();
        contactTimeInstance.setTimeInMillis(rawContactTime);

        //TODO 방장 정보, 연령대 추가

        StringBuffer dsec = new StringBuffer();
        dsec.append(String.format(Locale.KOREAN,
                "만난 시간 : %d시 %d분\n",
                contactTimeInstance.get(Calendar.HOUR_OF_DAY),
                contactTimeInstance.get(Calendar.MINUTE)));
        dsec.append(String.format(Locale.KOREAN,
                "만남 장소 : %s\n",chatRoom.getLocationStr()));
        dsec.append(String.format(Locale.KOREAN,
                "먹는 음식 : %s\n",chatRoom.getFoodName()));
        dsec.append(String.format(Locale.KOREAN,
                "현재 인원 : %d/%d\n",
                chatRoom.getCurrentPeople(),chatRoom.getMaxPeople()));

        mChatRoomInfoDialog = new MaterialStyledDialog.Builder(mContext)
                .setTitle(title)
                .setHeaderColor(R.color.fbutton_color_orange)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(dsec.toString())
                .setScrollable(true)
                .setPositiveText(R.string.chat_room_dialog_enter)
                .onPositive(((dialog, which) -> moveToChatDetailActivity(chatRoom.getId())))
                .setNegativeText(R.string.chat_room_dialog_return)
                .onNegative(((dialog, which) -> Log.d("MaterialStyledDialogs", "return main activity")))
                .build();
    }

    private void showChatRoomInfo(){
        if(mChatRoomInfoDialog==null){
            return;
        }
        mChatRoomInfoDialog.show();
    }

    private void moveToChatDetailActivity(String chatRoomId){
        Intent intent = new Intent(mContext, ChatDetailActivity.class);
        intent.putExtra("roomId",chatRoomId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mContext.startActivity(intent);

    }
}
