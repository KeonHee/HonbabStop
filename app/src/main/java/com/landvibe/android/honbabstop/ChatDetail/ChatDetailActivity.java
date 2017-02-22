package com.landvibe.android.honbabstop.chatdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.landvibe.android.honbabstop.chatdetail.adapter.ChatMessageAdapter;
import com.landvibe.android.honbabstop.chatdetail.presenter.ChatDetailPresenter;
import com.landvibe.android.honbabstop.chatdetail.presenter.ChatDetailPresenterImpl;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatMessage;
import com.landvibe.android.honbabstop.base.domain.ChatRoom;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatDetailActivity extends AppCompatActivity implements ChatDetailPresenter.View,
        View.OnLayoutChangeListener {

    private final static String TAG = "ChatDetailActivity";

    @BindView(R.id.rv_chat_message_list)
    RecyclerView mChatMessageView;

    @BindView(R.id.et_send_message)
    MaterialEditText mMessageEditText;

    @BindView(R.id.iv_send_message)
    ImageView mSendMessageBtn;

    @BindView(R.id.maps_fragment_space)
    LinearLayout mMapsSpace;

    private NMapFragment mMapFragment;

    private ChatDetailPresenterImpl mChatDetailPresenter;

    private ChatMessageAdapter mChatMessageAdapter;

    private LinearLayoutManager mLinearLayoutManager;

    private User mUser;

    private String roomId;

    private MenuItem mMapMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        Intent intent = getIntent();
        setActionBar(intent.getStringExtra("title"));

        mUser= UserStore.getInstance().getUser();

        mLinearLayoutManager = new LinearLayoutManager(this);
        mChatMessageView.setLayoutManager(mLinearLayoutManager);

        mChatMessageAdapter = new ChatMessageAdapter(this);
        mChatMessageView.setAdapter(mChatMessageAdapter);

        mChatMessageView.setHasFixedSize(false);
        mChatMessageView.addOnLayoutChangeListener(this);

        mChatDetailPresenter = new ChatDetailPresenterImpl();
        mChatDetailPresenter.attachView(this,this);
        mChatDetailPresenter.setChatMessageAdapterModel(mChatMessageAdapter);
        mChatDetailPresenter.setChatMessageAdapterViw(mChatMessageAdapter);

        roomId = intent.getStringExtra("roomId");
        mChatDetailPresenter.loadChatMessageList(roomId);
        mChatDetailPresenter.loadChatRoomInfo(roomId);

        mSendMessageBtn.setOnClickListener(v->{
            if(mMessageEditText.getText().toString().length()==0){
                return;
            }

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUid(mUser.getUid());
            chatMessage.setName(mUser.getName());
            chatMessage.setProfileUrl(mUser.getProfileUrl());
            chatMessage.setMessage(mMessageEditText.getText().toString());
            chatMessage.setSendTimeStamp(Calendar.getInstance().getTimeInMillis());
            chatMessage.setType(ChatMessage.TYPE_MY_MESSAGE);

            mChatDetailPresenter.sendMessage(chatMessage,roomId);
        });
    }

    private void setActionBar(String title){
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatDetailPresenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_detail,menu);
        Log.d(TAG, "onCreateOptionsMenu() 호출");
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMapMenuItem = menu.findItem(R.id.action_maps);
        Log.d(TAG, "onPrepareOptionsMenu() 호출");
        //mMapMenuItem.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mChatDetailPresenter.backToTheMain();
                return true;
            case R.id.action_room_out:
                mChatDetailPresenter.outOfChatRoom(mUser, roomId);
                return true;
            case R.id.action_maps:
                /* show maps */
                mChatDetailPresenter.actionMapView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void moveToMainActivity() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void clearText() {
        mMessageEditText.setText("");
    }

    @Override
    public void scrollToBottom() {
        if(mChatMessageView==null || mChatMessageAdapter==null || mChatMessageAdapter.getItemCount()==0){
            return;
        }
        mChatMessageView.smoothScrollToPosition(mChatMessageAdapter.getItemCount()-1);
    }


    @Override
    public void initMapFragment(ChatRoom chatRoom){
        //mMapMenuItem.setEnabled(true);

        mMapFragment = new NMapFragment();
        Bundle arg = new Bundle();
        arg.putSerializable(ChatRoom.KEY, chatRoom);
        mMapFragment.setArguments(arg);
    }

    @Override
    public void showMaps() {
        if(mMapFragment==null){
            return;
        }
        mMapsSpace.setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.maps_fragment_space, mMapFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void hideMaps() {
        mMapsSpace.setVisibility(View.GONE);

        if(mMapFragment==null){
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.hide(mMapFragment);
    }

    @Override
    public void onLayoutChange(View v,
                               int left, int top, int right, int bottom,
                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(bottom<oldBottom){
            mChatMessageView.postDelayed(() -> scrollToBottom(),100);
        }
    }
}
