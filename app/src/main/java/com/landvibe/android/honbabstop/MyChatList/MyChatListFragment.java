package com.landvibe.android.honbabstop.mychatlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.landvibe.android.honbabstop.base.listener.OnRefreshListener;
import com.landvibe.android.honbabstop.chatdetail.ChatDetailActivity;
import com.landvibe.android.honbabstop.mychatlist.adapter.MyChatListAdapter;
import com.landvibe.android.honbabstop.mychatlist.presenter.MyChatListPresenter;
import com.landvibe.android.honbabstop.mychatlist.presenter.MyChatListPresenterImpl;
import com.landvibe.android.honbabstop.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017-02-13.
 */

public class MyChatListFragment extends Fragment implements MyChatListPresenter.View{

    private final static String TAG ="MyChatListFragment";

    private int mPage;

    @BindView(R.id.rv_my_chat_list)
    RecyclerView mMyChatListView;

    private MyChatListAdapter mMyChatListAdapter;

    private LinearLayoutManager mLinearLayoutManager;

    private MyChatListPresenterImpl mMyChatListPresenter;

    public static Fragment getInstance(int page){
        MyChatListFragment myChatListFragment = new MyChatListFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        myChatListFragment.setArguments(args);
        return myChatListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        mPage=getArguments().getInt("page");
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_chat, container,false);
        Log.d(TAG, "onCreateView()");

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

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mMyChatListView.setLayoutManager(mLinearLayoutManager);

        mMyChatListAdapter=new MyChatListAdapter(getActivity());
        mMyChatListView.setAdapter(mMyChatListAdapter);

        mMyChatListView.setHasFixedSize(true);

        mMyChatListPresenter = new MyChatListPresenterImpl();
        mMyChatListPresenter.attachView(this,getActivity());

        mMyChatListPresenter.setChatListAdapterModel(mMyChatListAdapter);
        mMyChatListPresenter.setChatListAdapterView(mMyChatListAdapter);

        mMyChatListPresenter.loadMyChatList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMyChatListPresenter.detachView();
        mMyChatListPresenter=null;

        mMyChatListAdapter=null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                mMyChatListPresenter.loadMyChatList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void moveToChatDetailActivity(String roomId, String title) {
        final Activity activity = getActivity();
        final Intent intent = new Intent(activity, ChatDetailActivity.class);
        intent.putExtra("roomId",roomId);
        intent.putExtra("title", title);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
