package com.landvibe.android.honbabstop.updateprofile.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.base.domain.User;

/**
 * Created by user on 2017-02-14.
 */

public interface UpdateProfilePresenter {
    interface View{

        void moveToMainActivity();

        void updateUserInfo(User user);

    }

    interface Presenter{

        void attachView(UpdateProfilePresenter.View view, Activity activity);

        void detachView();

        void loadUserInfo();

        void saveUserInfo(User user);


    }
}
