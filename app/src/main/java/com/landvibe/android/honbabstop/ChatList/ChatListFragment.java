package com.landvibe.android.honbabstop.ChatList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.landvibe.android.honbabstop.AddChat.AddChatActivity;
import com.landvibe.android.honbabstop.ChatDetail.ChatDetailActivity;
import com.landvibe.android.honbabstop.ChatList.adapter.ChatListAdapter;
import com.landvibe.android.honbabstop.ChatList.presenter.ChatListPresenter;
import com.landvibe.android.honbabstop.ChatList.presenter.ChatListPresenterImpl;
import com.landvibe.android.honbabstop.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017-02-13.
 */

public class ChatListFragment extends Fragment implements ChatListPresenter.View {

    private final static String TAG ="ChatListFragment";

    private int mPage;

    @BindView(R.id.rv_chat_list)
    RecyclerView mChatListView;

    @BindView(R.id.fab_add_chat_room)
    FloatingActionButton mAddChatRoom;

    private ChatListAdapter mChatListAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private ChatListPresenterImpl mChatListPresenter;


    public static Fragment getInstance(int page){
        ChatListFragment chatListFragment = new ChatListFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        chatListFragment.setArguments(args);
        return chatListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        mPage=getArguments().getInt("page");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_chat_list, container,false);

        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");

        init();
    }
    private void init(){

        mAddChatRoom.setOnClickListener(v->moveToAddChatActivity());

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mChatListView.setLayoutManager(mLinearLayoutManager);

        mChatListAdapter=new ChatListAdapter(getActivity());
        mChatListView.setAdapter(mChatListAdapter);

        mChatListView.setHasFixedSize(true);
        mChatListView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING){
                    mAddChatRoom.setVisibility(View.GONE);
                }
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    mAddChatRoom.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mChatListPresenter = new ChatListPresenterImpl();
        mChatListPresenter.attachView(this,getActivity());

        mChatListPresenter.setChatListAdapterModel(mChatListAdapter);
        mChatListPresenter.setChatListAdapterView(mChatListAdapter);

        mChatListPresenter.loadChatList();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChatListPresenter.detachView();
        mChatListPresenter=null;

        mChatListAdapter=null;
    }

    @Override
    public void moveToChatDetailActivity(String roomId) {
        final Activity activity = getActivity();
        final Intent intent = new Intent(activity, ChatDetailActivity.class);
        intent.putExtra("roomId",roomId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void moveToAddChatActivity() {
        final Activity activity = getActivity();
        final Intent intent = new Intent(activity, AddChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}
