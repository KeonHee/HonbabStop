package com.landvibe.android.honbabstop.main.page;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.landvibe.android.honbabstop.chatlist.ChatListFragment;
import com.landvibe.android.honbabstop.mychatlist.MyChatListFragment;
import com.landvibe.android.honbabstop.profile.ProfileFragment;

/**
 * Created by user on 2017-02-13.
 */

public class MainPageAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;

    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ChatListFragment.getInstance(0);
            case 1:
                return MyChatListFragment.getInstance(1);
            case 2:
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
