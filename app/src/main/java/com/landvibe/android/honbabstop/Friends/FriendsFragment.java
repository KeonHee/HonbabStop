package com.landvibe.android.honbabstop.Friends;

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

public class FriendsFragment extends Fragment{

    private final static String TAG ="FriendsFragment";

    private int mPage;

    public static Fragment getInstance(int page){
        FriendsFragment friendsFragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        friendsFragment.setArguments(args);
        return friendsFragment;
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
        View view = inflater.inflate(R.layout.fragment_friends, container,false);
        Log.d(TAG, "onCreateView()");
        return view;
    }
}
