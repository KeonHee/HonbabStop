package com.landvibe.android.honbabstop.MyChatList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.landvibe.android.honbabstop.R;

/**
 * Created by user on 2017-02-13.
 */

public class MyChatListFragment extends Fragment {

    private final static String TAG ="MyChatListFragment";

    private int mPage;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_chat, container,false);
        Log.d(TAG, "onCreateView()");
        return view;
    }
}
