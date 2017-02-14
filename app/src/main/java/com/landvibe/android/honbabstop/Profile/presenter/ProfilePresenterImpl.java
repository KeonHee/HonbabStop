package com.landvibe.android.honbabstop.Profile.presenter;

import android.app.Activity;

import com.landvibe.android.honbabstop.Profile.model.ProfileModel;
import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.User;


public class ProfilePresenterImpl implements ProfilePresenter.Presenter, ProfileModel.UserDataChange {

    ProfilePresenter.View view;

    ProfileModel mProfileModel;

    Activity mActivity;

    @Override
    public void attachView(ProfilePresenter.View view, Activity activity) {
        this.view=view;
        mActivity=activity;

        mProfileModel = new ProfileModel();
        mProfileModel.setOnChangeListener(this);

        mProfileModel.loadUser();

    }

    @Override
    public void detachView() {
        this.view=null;
        mActivity=null;
        mProfileModel.setOnChangeListener(null);
        mProfileModel=null;
    }

    @Override
    public void update(User user) {
        if (user==null){
            return;
        }

        if (user.getProfileUrl()!=null){
            view.updateUserProfile(user.getProfileUrl());
        }

        if (user.getName()!=null){
            view.updateUserName(user.getName());
        }else {
            view.updateUserName(mActivity.getString(R.string.empty_name));
        }

        if(user.getProviderId()!=null){
            view.updateUserProvider(user.getProviderId());
        }

        if(user.getEmail()!=null){
            if(user.getEmail().contains("@"+User.KAKAO)){
                view.updateUserEmail(mActivity.getString(R.string.empty_email));
            }else {
                view.updateUserEmail(user.getEmail());
            }
        }else {
            view.updateUserEmail(mActivity.getString(R.string.empty_email));
        }

        if (user.getStatus()!=null){
            view.updateUserStatus(user.getStatus());
        }else {
            view.updateUserStatus(mActivity.getString(R.string.empty_status));
        }

        if(user.getAge()!=0){
            view.updateUserAge(String.valueOf(user.getAge())+" 살");
        }else {
            view.updateUserAge(mActivity.getString(R.string.empty_age));
        }


        if(user.getGender()!=null){
            view.updateUserGender(user.getGender());
        }else {
            view.updateUserGender(mActivity.getString(R.string.empty_gender));
        }

        if(user.getAddress()!=null){
            view.updateAddress(user.getAddress());
        }else {
            view.updateAddress(mActivity.getString(R.string.empty_address));
        }
    }
}
