package com.landvibe.android.honbabstop.profile.presenter;

import android.app.Activity;
import android.net.Uri;

/**
 * Created by user on 2017-02-13.
 */

public interface ProfilePresenter {

    interface View{

        void showLoading();

        void hideLoading();

        void updateUserProfile(String url);

        void updateUserName(String name);

        void updateUserProvider(String provider);

        void updateUserEmail(String email);

        void updateUserStatus(String status);

        void updateUserAge(String age);

        void updateUserGender(String gender);

        void updateAddress(String address);

        void moveToUpdateProfileActivity();

    }

    interface Presenter{

        void attachView(ProfilePresenter.View view, Activity activity);

        void detachView();

        void loadUser();

        void renewUserInfo();

        void uploadImageToStorage(Uri url);

    }

}
