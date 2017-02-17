package com.landvibe.android.honbabstop.ChatDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.landvibe.android.honbabstop.ChatDetail.adapter.ChatMessageAdapter;
import com.landvibe.android.honbabstop.ChatDetail.presenter.ChatDetailPresenter;
import com.landvibe.android.honbabstop.ChatDetail.presenter.ChatDetailPresenterImpl;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.ChatMessage;
import com.landvibe.android.honbabstop.base.domain.User;
import com.landvibe.android.honbabstop.base.domain.UserStore;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatDetailActivity extends AppCompatActivity implements ChatDetailPresenter.View{


    private final static String TAG = "ChatDetailActivity";

    @BindView(R.id.rv_chat_message_list)
    RecyclerView mChatMessageView;

    @BindView(R.id.et_send_message)
    MaterialEditText mMessageEditText;

    @BindView(R.id.iv_send_message)
    ImageView mSendMessageBtn;

    private ChatDetailPresenterImpl mChatDetailPresenter;

    private ChatMessageAdapter mChatMessageAdapter;

    private LinearLayoutManager mLinearLayoutManager;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        setActionBar();

        mUser= UserStore.getInstance().getUser();

        mLinearLayoutManager = new LinearLayoutManager(this);
        mChatMessageView.setLayoutManager(mLinearLayoutManager);

        mChatMessageAdapter = new ChatMessageAdapter(this);
        mChatMessageView.setAdapter(mChatMessageAdapter);

        mChatMessageView.setHasFixedSize(false);

        mChatDetailPresenter = new ChatDetailPresenterImpl();
        mChatDetailPresenter.attachView(this,this);
        mChatDetailPresenter.setChatMessageAdapterModel(mChatMessageAdapter);
        mChatDetailPresenter.setChatMessageAdapterViw(mChatMessageAdapter);


        Intent intent = getIntent();
        String roomId = intent.getStringExtra("roomId");
        mChatDetailPresenter.loadChatMessageList(roomId);

        mSendMessageBtn.setOnClickListener(v->{
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

    private void setActionBar(){
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatDetailPresenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mChatDetailPresenter.backToTheMain();
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
        if(mChatMessageView==null || mChatMessageAdapter==null){
            return;
        }
        mChatMessageView.smoothScrollToPosition(mChatMessageAdapter.getItemCount()-1);
    }
}
