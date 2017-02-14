package com.landvibe.android.honbabstop.Main.page;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.landvibe.android.honbabstop.ChatList.ChatListFragment;
import com.landvibe.android.honbabstop.Friends.FriendsFragment;
import com.landvibe.android.honbabstop.MyChat.MyChatFragment;
import com.landvibe.android.honbabstop.Profile.ProfileFragment;

/**
 * Created by user on 2017-02-13.
 */

public class MainPageAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 4;

    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ChatListFragment.getInstance(0);
            case 1:
                return MyChatFragment.getInstance(1);
            case 2:
                return FriendsFragment.getInstance(1);
            case 3:
                return ProfileFragment.getInstance(1);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}