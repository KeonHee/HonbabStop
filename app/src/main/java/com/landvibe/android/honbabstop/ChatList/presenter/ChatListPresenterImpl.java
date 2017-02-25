package com.landvibe.android.honbabstop.chatlist.presenter;

import android.app.Activity;
import android.util.Log;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.landvibe.android.honbabstop.chatlist.adapter.contract.ChatListAdapterContract;
import com.landvibe.android.honbabstop.chatlist.model.ChatListModel;
import com.landvibe.android.honbabstop.GlobalApp;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.MyChat;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;
import com.landvibe.android.honbabstop.base.listener.OnItemClickListener;
import com.landvibe.android.honbabstop.base.observer.CustomObserver;
import com.landvibe.android.honbabstop.base.utils.DomainConvertUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 2017-02-15.
 */

public class ChatListPresenterImpl implements ChatListPresenter.Presenter,
        ChatListModel.ChangeChatListData, ChatListModel.CompleteChangeUserData, OnItemClickListener, CustomObserver{


    private final static String TAG = "ChatListPresenterImpl";
    private ChatListPresenter.View view;

    private Activity mActivity;

    private ChatListAdapterContract.Model mAdapterModel;
    private ChatListAdapterContract.View mAdapterView;

    private ChatListModel mChatListModel;

    @Override
    public void attachView(ChatListPresenter.View view, Activity activity) {
        this.view=view;
        mActivity=activity;

        mChatListModel = new ChatListModel();
        mChatListModel.setChangeListener(this);
        mChatListModel.setCompleteListener(this);

        GlobalApp.getGlobalApplicationContext().addObserver(this);

    }

    @Override
    public void detachView() {
        this.view=null;
        mActivity=null;


        mChatListModel.setCompleteListener(null);
        mChatListModel.setChangeListener(null);
        mChatListModel=null;

        GlobalApp.getGlobalApplicationContext().removeObserver(this);

    }

    @Override
    public void setChatListAdapterModel(ChatListAdapterContract.Model model) {
        mAdapterModel=model;
    }

    @Override
    public void setChatListAdapterView(ChatListAdapterContract.View view) {
        mAdapterView=view;
        mAdapterView.setOnItemClickListener(this);
    }

    @Override
    public void loadChatList() {
        //view.showLoading();
        mChatListModel.loadChatList(15); //TODO 쿼리개수 핸들러 만들기 (무한로딩)
    }

    @Override
    public void update(List<ChatRoom> list) {
        view.hideLoading();

        if(list==null || list.size()==0){
            return;
        }

        mAdapterModel.setListData(list);
        mAdapterView.notifyAdapter();
    }

    @Override
    public void onItemClick(Object object) {
        showChatRoomInfo((ChatRoom)object);
    }

    private void showChatRoomInfo(ChatRoom chatRoom){
        User user = UserStore.getInstance().getUser();

        String title = chatRoom.getTitle();

        /* 만남 시간, 만남 장소, 음식 정보, 현재 인원 */
        long rawContactTime = chatRoom.getContactTime();
        Calendar contactTimeInstance = Calendar.getInstance();
        contactTimeInstance.setTimeInMillis(rawContactTime);

        //TODO 방장 정보, 연령대 추가

        StringBuffer dsec = new StringBuffer();
        dsec.append(String.format(Locale.KOREAN,
                "만남 시간 : %d시 %d분\n",
                contactTimeInstance.get(Calendar.HOUR_OF_DAY),
                contactTimeInstance.get(Calendar.MINUTE)));
        dsec.append(String.format(Locale.KOREAN,
                "만남 장소 : %s\n",chatRoom.getAddress()));
        dsec.append(String.format(Locale.KOREAN,
                "음식점 : %s\n",chatRoom.getFoodTitle()));
        dsec.append(String.format(Locale.KOREAN,
                "메뉴 이름 : %s\n",chatRoom.getFoodName()));
        dsec.append(String.format(Locale.KOREAN,
                "현재 인원 : %d/%d\n",
                chatRoom.getCurrentPeople(),chatRoom.getMaxPeople()));

        new MaterialStyledDialog.Builder(mActivity)
                .setTitle(title)
                .setHeaderColor(R.color.fbutton_color_orange)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(dsec.toString())
                .setScrollable(true)
                .setPositiveText(R.string.chat_room_dialog_enter)
                .onPositive(((dialog, which) ->
                    mChatListModel.addUserInfoInChatRoom(
                            user, chatRoom.getId(), chatRoom.getMembers())))
                .setNegativeText(R.string.chat_room_dialog_return)
                .onNegative(((dialog, which) -> Log.d("MaterialStyledDialogs", "return main activity")))
                .show();
    }


    /**
     * 옵저버 등록
     */
    @Override
    public void update(Object object) {
        if(object instanceof ChatRoom){
            if(mAdapterModel==null || mAdapterView==null){
                return;
            }


            ChatRoom chatRoom = (ChatRoom) object;
            int index = mAdapterModel.indexOf(chatRoom);
            if(index<0){
                /* Not Found */
                mAdapterModel.addListData(chatRoom);
                mAdapterView.notifyAdapter();
            }else {
                mAdapterModel.updateData(chatRoom,index);
                mAdapterView.notifyAdapterPosition(index);
            }
        }
    }


    /**
     *  채팅방 입장 여부 콜백
     */
    @Override
    public void onComplete(ChatRoom chatRoom) {
        view.moveToChatDetailActivity(chatRoom.getId(),chatRoom.getTitle());

        GlobalApp.getGlobalApplicationContext().changeModel(chatRoom);

        MyChat myChat = DomainConvertUtils.convertChatRoomToMyChat(chatRoom);
        GlobalApp.getGlobalApplicationContext().changeModel(myChat);
    }

    @Override
    public void onFailure(String errorMessage) {
        Log.d(TAG, errorMessage);

        String title = String.format(mActivity.getResources().getString(R.string.enter_fail_title));
        String desc = String.format(mActivity.getResources().getString(R.string.enter_fail_desc),errorMessage);

        mActivity.runOnUiThread(()->
                new MaterialStyledDialog.Builder(mActivity)
                .setTitle(title)
                .setHeaderColor(R.color.fbutton_color_orange)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(desc)
                .setScrollable(true)
                .setPositiveText(R.string.chat_room_dialog_enter)
                .show());
    }
}
