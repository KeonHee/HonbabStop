package com.landvibe.android.honbabstop.Profile.presenter;

import android.app.Activity;

/**
 * Created by user on 2017-02-13.
 */

public interface ProfilePresenter {

    interface View{

        void updateUserProfile(String url);

        void updateUserName(String name);

        void updateUserProvider(String provider);

        void updateUserEmail(String email);

        void updateUserStatus(String status);

        void updateUserAge(String age);

        void updateUserGender(String gender);

        void updateAddress(String address);

    }

    interface Presenter{

        void attachView(ProfilePresenter.View view, Activity activity);

        void detachView();

    }

}
